package com.libreria.ej1.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Cliente {

	@Id
	private Long documento;
	private String nombre;
	private String apellido;
	private String domicilio;
	private String telefono;
	
}
