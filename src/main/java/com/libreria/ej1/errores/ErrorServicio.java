package com.libreria.ej1.errores;

@SuppressWarnings("serial")
public class ErrorServicio extends Exception {
	
	public ErrorServicio(String msn) {
		super(msn);
	}
}
