package com.iessanvicente.rest.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Producto {

	@ApiModelProperty(value="ID del producto", dataType="long", example="1", position=1)
	@Id @GeneratedValue
	private Long id;
	
	@ApiModelProperty(value="Nombre del producto", dataType="String", example="Café", position=2)
	private String nombre;
	
	@ApiModelProperty(value="Precio del producto", dataType="float", example="0.99", position=3)
	private float precio;
	
	@ApiModelProperty(value="Imagen del producto", dataType="String", example="https://mydominio.com/f.jpg", position=4)
	private String imagen;
	

	@ApiModelProperty(value="Categoria del producto", dataType="Categoria", position=5)
	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;
	
}
