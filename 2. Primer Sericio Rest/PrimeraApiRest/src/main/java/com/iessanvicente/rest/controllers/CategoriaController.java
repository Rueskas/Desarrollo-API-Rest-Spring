package com.iessanvicente.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iessanvicente.rest.error.CategoriaNotFoundException;
import com.iessanvicente.rest.models.Categoria;
import com.iessanvicente.rest.models.CategoriaRepositorio;


@RestController
public class CategoriaController {

	@Autowired
	CategoriaRepositorio categoriaRepositorio;
	
	@GetMapping("/categoria")
	public List<Categoria> categorias(){
		List<Categoria> categorias = categoriaRepositorio.findAll();
		if(categorias.isEmpty()) {
			throw new CategoriaNotFoundException(null);
		} else {
			return categorias;
		}
	}
	
	@GetMapping("/categoria/{id}")
	public Categoria categoria(@PathVariable long id) {
		return categoriaRepositorio.findById(id)
				.orElseThrow(() -> new CategoriaNotFoundException(id));
	}
	
	@PostMapping("/categoria")
	public Categoria newCategoria(@RequestBody Categoria categoria) {
		return categoriaRepositorio.save(categoria);
	}
	
	@PutMapping("/categoria/{id}")
	public Categoria putCategoria(@RequestBody Categoria categoria, @PathVariable long id) {
		if(categoriaRepositorio.existsById(id)) {
			categoria.setId(id);
			return categoriaRepositorio.save(categoria);
		} else {
			throw new CategoriaNotFoundException(id);
		}
	}
	
	@DeleteMapping("/categoria/{id}")
	public ResponseEntity<?> deleteCategoria(@PathVariable long id) {
		if(categoriaRepositorio.existsById(id)) {
			categoriaRepositorio.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			throw new CategoriaNotFoundException(id);
		}
	}
}
