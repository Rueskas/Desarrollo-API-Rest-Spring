package com.iessanvicente.rest.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductoNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3863739378913611710L;

	public ProductoNotFoundException(Long id) {
		super("No encontrado el producto con id: " + id);
	}
}
