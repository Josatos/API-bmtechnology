package com.bmtechnology.api.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.FileTypeMap;

import com.bmtechnology.api.interfaceService.IUsuarioService;
import com.bmtechnology.api.model.Rol;
import com.bmtechnology.api.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = { "*" })
public class UsuarioRestController {

    @Autowired
    IUsuarioService uService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Secured({ "ROLE_ADMIN", "ROLE_ALMACEN" })
    @GetMapping("/page/{page}")
    public Page<Usuario> listarUsuarios(@PathVariable Integer page) {
        return uService.paginacionUsuarios(PageRequest.of(page, 15));
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_ALMACEN" })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Integer id) {
        Usuario u = null;
        Map<String, Object> response = new HashMap<>();
        try {
            u = uService.buscaUsuario(id);
            if (u == null) {
                response.put("mensaje", "El producto no se encuentra en la base de datos");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(u, HttpStatus.OK);
            }
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta a la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_ALMACEN" })
    @GetMapping("/foto/{id}")
    public ResponseEntity<?> obtenerImagenUsuario(@PathVariable Integer id) throws IOException {
        Usuario u = null;
        String foto = null;
        Map<String, Object> response = new HashMap<>();
        try {
            u = uService.buscaUsuario(id);
            if (u == null) {
                response.put("mensaje", "El usuario no se encuentra en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            } else {
                foto = u.getFoto();
                if (foto == null) {
                    response.put("mensaje", "El usuario que seleccionó no cuenta con foto");
                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
                } else {
                    File img = new File("uploads/fotos/usuarios/" + foto);
                    return ResponseEntity.ok()
                            .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img)))
                            .body(Files.readAllBytes(img.toPath()));
                }
            }
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta a la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_ALMACEN" })
    @PostMapping("/upload")
    public ResponseEntity<?> subirFotoUsuario(@RequestParam("foto") MultipartFile foto,
            @RequestParam("id") Integer id) {
        Usuario u = uService.buscaUsuario(id);
        Map<String, Object> response = new HashMap<>();
        if (!foto.isEmpty()) {
            String nombreFoto = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename().replace(" ", "");
            Path rutaFoto = Paths.get("uploads\\fotos\\usuarios").resolve(nombreFoto).toAbsolutePath();
            try {
                Files.copy(foto.getInputStream(), rutaFoto);
            } catch (Exception e) {
                response.put("mensaje", "Error al subir la imagen");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String nombreFotoAnterior = u.getFoto();
            if (nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
                Path rutaFotoAnterior = Paths.get("uploads\\fotos\\usuarios").resolve(nombreFotoAnterior)
                        .toAbsolutePath();
                File archivoFotoAnterior = rutaFotoAnterior.toFile();
                if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
                    archivoFotoAnterior.delete();
                }
            }
            u.setFoto(nombreFoto);
            uService.guardarUsuario(u);
            response.put("mensaje", "Ha subido correctamente la imagen " + nombreFoto);
        } else {
            response.put("mensaje", "El campo foto no puede estar vacío");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PostMapping("/registro/cliente")
    public ResponseEntity<?> registrarCliente(@RequestBody Usuario u) {
        Usuario nuevoUsuario = null;
        List<Rol> roles = new ArrayList<>();
        roles.add(new Rol(2, "ROLE_USER", null));
        String password = passwordEncoder.encode(u.getPassword());
        Map<String, Object> response = new HashMap<>();
        try {
            u.setPassword(password);
            u.setRoles(roles);
            nuevoUsuario = uService.guardarUsuario(u);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el registro a la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("usuario", nuevoUsuario);
        response.put("mensaje", "El usuario fue registrado correctamente");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
