package com.libreria.ej1.servicios;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.libreria.ej1.entidades.Editorial;
import com.libreria.ej1.errores.ErrorServicio;
import com.libreria.ej1.repositorios.EditorialRepositorio;

@Service
public class EditorialServicio implements UserDetailsService{

	@Autowired
	private EditorialRepositorio editorialRepositorio;

	@Transactional
	public void crear(String nombre) throws ErrorServicio {
		validarNombre(nombre);
		Editorial editorial = new Editorial();
		editorial.setNombre(nombre);

		editorialRepositorio.save(editorial);
	}

	@Transactional
	public void consultar(String id) throws ErrorServicio {
		Optional<Editorial> respuesta = editorialRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Editorial editorial = respuesta.get();
			validarNombre(editorial.getNombre());
			editorial.getNombre();

			editorialRepositorio.save(editorial);
		} else {
			throw new ErrorServicio("No se encontró la editorial");
		}
	}

	@Transactional
	public Editorial modificar(String id, String nombre) throws ErrorServicio {
		validarNombre(nombre);
		Optional<Editorial> respuesta = editorialRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Editorial editorial = respuesta.get();
			editorial.setNombre(nombre);

			return editorialRepositorio.save(editorial);
		} else {
			throw new ErrorServicio("No se encontró la editorial.");
		}
	}

	@Transactional
	public void eliminar(String id) throws ErrorServicio {
		Optional<Editorial> respuesta = editorialRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Editorial editorial = respuesta.get();
			
			editorialRepositorio.delete(editorial);
		} else {
			throw new ErrorServicio("No se encontró la editorial.");
		}
	}

	@Transactional
	public Editorial getById (String id)throws ErrorServicio {
		
		Optional<Editorial> respuesta = editorialRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Editorial editorial = respuesta.get();
			return editorial;
		} else {
			throw new ErrorServicio("No se encontró la editorial.");
		}
	}
	
	
	@Transactional
	public List<Editorial>listarEditoriales() throws ErrorServicio{
		try {
			return editorialRepositorio.findAll();
		} catch (Exception e) {
			throw new ErrorServicio("No se pudo listar las editoriales.");
		}
		
	}
	
	private void validarNombre(String nombre) throws ErrorServicio {
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("El nombre no puede estar vacío.");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
