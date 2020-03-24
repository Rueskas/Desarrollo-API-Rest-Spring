package com.iessanvicente.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.iessanvicente.rest.converter.ProductoDTOConverter;
import com.iessanvicente.rest.dto.CreateProductoDTO;
import com.iessanvicente.rest.dto.ProductoDTO;
import com.iessanvicente.rest.error.ProductoNotFoundException;
import com.iessanvicente.rest.models.Categoria;
import com.iessanvicente.rest.models.CategoriaRepositorio;
import com.iessanvicente.rest.models.Producto;
import com.iessanvicente.rest.models.ProductoRepositorio;

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
	@GetMapping("/producto/{id}")
	public ProductoDTO obtenerUno(@PathVariable Long id) {
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
	@PostMapping("/producto")
	public ProductoDTO nuevoProducto(@RequestBody CreateProductoDTO nuevo) {
		
		if(categoriaRepositorio.existsById(nuevo.getCategoriaId())) {
			Categoria c = categoriaRepositorio.findById(nuevo.getCategoriaId()).orElse(null);
			
			Producto p = new Producto(null, nuevo.getNombre(), nuevo.getPrecio(), c);
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