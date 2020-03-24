package com.iessanvicente.rest.dto;

import lombok.Data;

@Data
public class CreateProductoDTO {

	private String nombre;
	private float precio;
	private long categoriaId;
}
