package com.bmtechnology.api.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.activation.FileTypeMap;

import com.bmtechnology.api.interfaceService.IProductoService;
import com.bmtechnology.api.model.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = { "*" })
public class ProductoRestController {

    @Autowired
    IProductoService pService;

    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_ALMACEN" })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Integer id) {
        Producto p = null;
        Map<String, Object> response = new HashMap<>();
        try {
            p = pService.buscarProducto(id);
            if (p == null) {
                response.put("mensaje", "El producto no se encuentra en la base de datos");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(p, HttpStatus.OK);
            }
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta a la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_ALMACEN" })
    @GetMapping("/foto/{id}")
    public ResponseEntity<?> obtenerImagenProducto(@PathVariable Integer id) throws IOException {
        Producto p = null;
        String foto = null;
        Map<String, Object> response = new HashMap<>();
        try {
            p = pService.buscarProducto(id);
            if (p == null) {
                response.put("mensaje", "El producto no se encuentra en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            } else {
                foto = p.getFoto();
                if (foto == null) {
                    response.put("mensaje", "El producto que seleccionó no cuenta con foto");
                    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
                } else {
                    File img = new File("/uploads/fotos/productos/" + foto);
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

    @Secured({ "ROLE_ADMIN", "ROLE_ALMACEN" })
    @PostMapping("/registro")
    public ResponseEntity<Map<String, Object>> registrarProducto(@RequestBody Producto p) {
        Producto nuevoProducto = null;
        Map<String, Object> response = new HashMap<>();
        try {
            nuevoProducto = pService.guardarProducto(p);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el registro a la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("producto", nuevoProducto);
        response.put("mensaje", "El producto fue creado correctamente.");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Secured({ "ROLE_ADMIN", "ROLE_ALMACEN" })
    @PostMapping("/upload")
    public ResponseEntity<?> subirFotoProducto(@RequestParam("foto") MultipartFile foto,
            @RequestParam("id") Integer id) {
        Producto p = pService.buscarProducto(id);
        Map<String, Object> response = new HashMap<>();
        if (!foto.isEmpty()) {
            String nombreFoto = foto.getOriginalFilename().replace(" ", "");
            Path rutaFoto = Paths.get("uploads\\fotos\\productos").resolve(nombreFoto).toAbsolutePath();
            try {
                Files.copy(foto.getInputStream(), rutaFoto);
            } catch (Exception e) {
                response.put("mensaje", "Error al subir la imagen");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String nombreFotoAnterior = p.getFoto();
            if (nombreFotoAnterior != null && nombreFotoAnterior.length() > 0) {
                Path rutaFotoAnterior = Paths.get("uploads\\fotos\\productos").resolve(nombreFotoAnterior)
                        .toAbsolutePath();
                File archivoFotoAnterior = rutaFotoAnterior.toFile();
                if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
                    archivoFotoAnterior.delete();
                }
            }
            p.setFoto(nombreFoto);
            pService.guardarProducto(p);
            response.put("mensaje", "Ha subido correctamente la imagen " + nombreFoto);
        } else {
            response.put("mensaje", "El campo foto no puede estar vacío");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Secured({ "ROLE_ADMIN", "ROLE_ALMACEN" })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminarProducto(@PathVariable Integer id) {
        Producto p = null;
        Map<String, Object> response = new HashMap<>();
        try {
            p = pService.buscarProducto(id);
            if (p == null) {
                response.put("mensaje", "El producto con id " + id.toString() + " no existe en la base de datos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            } else {
                String nombreFoto = p.getFoto();
                if (nombreFoto != null && nombreFoto.length() > 0) {
                    Path rutaFoto = Paths.get("uploads\\fotos\\productos").resolve(nombreFoto).toAbsolutePath();
                    File archivoFoto = rutaFoto.toFile();
                    if (archivoFoto.exists() && archivoFoto.canRead()) {
                        archivoFoto.delete();
                    }
                }
                pService.eliminarProducto(id);
            }
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la eliminación del registro.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Producto eliminado correctamente");
        response.put("Producto eliminado", p);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
