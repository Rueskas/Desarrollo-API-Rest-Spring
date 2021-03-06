package com.iessanvicente.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.iessanvicente.rest.converter.ProductoDTOConverter;
import com.iessanvicente.rest.dto.CreateProductoDTO;
import com.iessanvicente.rest.dto.ProductoDTO;
import com.iessanvicente.rest.error.ApiError;
import com.iessanvicente.rest.error.ProductoNotFoundException;
import com.iessanvicente.rest.models.Categoria;
import com.iessanvicente.rest.models.CategoriaRepositorio;
import com.iessanvicente.rest.models.Producto;
import com.iessanvicente.rest.models.ProductoRepositorio;
import com.iessanvicente.rest.upload.StorageService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class ProductoController {
	
	@Autowired
	private final ProductoDTOConverter dtoConverter;
	@Autowired
	private ProductoRepositorio productoRepositorio;
	@Autowired
	private CategoriaRepositorio categoriaRepositorio;
	@Autowired
	private StorageService storageService;

	/**
	 * Obtenemos todos los productos
	 * 
	 * @return
	 */
	@CrossOrigin(origins="http://localhost:9001")
	@GetMapping("/producto")
	public List<ProductoDTO> obtenerTodos() {
		List<Producto> products = productoRepositorio.findAll();
		if(products.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe ningun producto");
		} else {
			return products.stream()
					.map(dtoConverter::convertToDto)
					.collect(Collectors.toList());
		}
	}

	/**
	 * Obtenemos un producto en base a su ID
	 * 
	 * @param id
	 * @return Null si no encuentra el producto
	 */
	@ApiOperation(value="Obtener un producto por su ID", notes="Provee un mecanismo para obtener todos los datos de un producto mediante su ID")
	@ApiResponses(value = {
			@ApiResponse(code=200, message="OK", response=Producto.class),
			@ApiResponse(code=404, message="NOT FOUND", response=ApiError.class),
			@ApiResponse(code=500, message="Internal Server Error", response=ApiError.class)
	})
	@GetMapping("/producto/{id}")
	public ProductoDTO obtenerUno(@ApiParam(value="ID del producto", required=true, type="long") @PathVariable Long id) {
		/*
		Producto p = productoRepositorio.findById(id).orElse(null);
		if(p == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(dtoConverter.convertToDto(p));
		}
		*/
		return dtoConverter.convertToDto(productoRepositorio.findById(id).orElseThrow(() -> new ProductoNotFoundException(id)));
	}

	/**
	 * Insertamos un nuevo producto
	 * 
	 * @param nuevo
	 * @return producto insertado
	 */
	@PostMapping(value = "/producto", 
			consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProductoDTO nuevoProducto(
			@RequestPart("nuevo") CreateProductoDTO nuevo,
			@RequestPart("file") MultipartFile file ) {
		
		String urlImage = null;
		
		if(!file.isEmpty()) {
			urlImage = storageService.store(file);
			urlImage = MvcUriComponentsBuilder
					.fromMethodName(FicheroController.class, "serveFile", urlImage, null)
					.build().toUriString();
		}
		if(categoriaRepositorio.existsById(nuevo.getCategoriaId())) {
			Categoria c = categoriaRepositorio.findById(nuevo.getCategoriaId()).orElse(null);
			
			Producto p = new Producto(null, nuevo.getNombre(), nuevo.getPrecio(), urlImage, c);
			Producto result = productoRepositorio.save(p);
			if(result != null) {
				return dtoConverter.convertToDto(result);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la categoria");
		}
		return null;
	}

	/**
	 * 
	 * @param editar
	 * @param id
	 * @return
	 */
	@PutMapping("/producto/{id}")
	public ProductoDTO editarProducto(@RequestBody Producto editar, @PathVariable Long id) {
		return productoRepositorio.findById(id).map(p -> {
			editar.setId(id);
			Producto saved = productoRepositorio.save(editar);
			if(saved != null) {
				return dtoConverter.convertToDto(saved);
			}
			return null;
		}).orElseThrow(() ->  new ProductoNotFoundException(id));
	}

	/**
	 * Borra un producto del catálogo en base a su id
	 * @param id
	 * @return
	 */
	@DeleteMapping("/producto/{id}")
	public ResponseEntity<?> borrarProducto(@PathVariable Long id) {
		Producto p = productoRepositorio.findById(id).orElseThrow(() -> new ProductoNotFoundException(id));
		productoRepositorio.deleteById(id);
		return ResponseEntity.noContent().build();
	}


}
