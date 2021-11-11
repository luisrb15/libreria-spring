package com.libreria.ej1.servicios;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.libreria.ej1.entidades.Autor;
import com.libreria.ej1.errores.ErrorServicio;
import com.libreria.ej1.repositorios.AutorRepositorio;

@Service
public class AutorServicio {
	
	@Autowired
	private AutorRepositorio autorRepositorio;
	
	@Transactional
	public void crear (String nombre) throws ErrorServicio{
		validar(nombre);
		Autor autor = new Autor();
		autor.setNombre(nombre);
	
		autorRepositorio.save(autor);
	}
	
	@Transactional
	public void consultar (String id, String nombre)throws ErrorServicio {
		validar(nombre);
		Optional<Autor> respuesta = autorRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Autor autor = respuesta.get();
			autor.getNombre();
			autorRepositorio.save(autor);
		} else {
			throw new ErrorServicio("No se encontró al autor.");
		}
	}
	
	@Transactional
	public Autor getById (String id)throws ErrorServicio {
		
		Optional<Autor> respuesta = autorRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Autor autor = respuesta.get();
			return autor;
		} else {
			throw new ErrorServicio("No se encontró al autor.");
		}
	}
	
	@Transactional
	public Autor modificar(String id, String nombre)throws ErrorServicio{
		validar(nombre);
		Optional<Autor> respuesta = autorRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Autor autor = respuesta.get();
			autor.setNombre(nombre);
			
			return autorRepositorio.save(autor);
		} else {
			throw new ErrorServicio("No se encontró el autor.");
		}
	}
	
	@Transactional
	public void eliminar(String id) throws ErrorServicio{
		Optional<Autor> respuesta = autorRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Autor autor = respuesta.get();
			autorRepositorio.delete(autor);
		} else {
			throw new ErrorServicio("No se encontró el autor.");
		}
	}
	
	@Transactional
	public List<Autor> listarAutores(){
		return autorRepositorio.findAllAutores(Sort.by("nombre"));
	}
	
	private void validar(String nombre) throws ErrorServicio{
		if (nombre==null||nombre.isEmpty()) {
			throw new ErrorServicio("El nombre no puede estar vacío");
		}
	}
}
