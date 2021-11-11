package com.libreria.ej1.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Libro {

	@Id
	private Long isbn;
	private String titulo;
	private Integer anio;
	private Integer ejemplares;
	private Integer prestados;
	//cuántos de esos ejemplares se encuentran prestados en este momento.
	
	@ManyToOne
	private Autor autor;
	@ManyToOne
	private Editorial editorial;
}
