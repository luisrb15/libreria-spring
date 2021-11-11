package com.libreria.ej1.servicios;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libreria.ej1.entidades.Autor;
import com.libreria.ej1.entidades.Editorial;
import com.libreria.ej1.entidades.Libro;
import com.libreria.ej1.errores.ErrorServicio;
import com.libreria.ej1.repositorios.AutorRepositorio;
import com.libreria.ej1.repositorios.EditorialRepositorio;
import com.libreria.ej1.repositorios.LibroRepositorio;

@Service
public class LibroServicio {

	@Autowired
	private LibroRepositorio libroRepositorio;
	@Autowired
	private AutorRepositorio autorRepositorio;
	@Autowired
	private EditorialRepositorio editorialRepositorio;

	@Transactional
	public Libro crear(String idAutor, String idEditorial, Long isbn, String titulo, Integer anio, Integer ejemplares)
			throws ErrorServicio {
		Autor autor = autorRepositorio.findById(idAutor).get();
		Editorial editorial = editorialRepositorio.findById(idEditorial).get();

		validar(isbn, titulo, anio, ejemplares);

		Libro libro = new Libro();
		libro.setIsbn(isbn);
		libro.setTitulo(titulo);
		libro.setAnio(anio);
		libro.setEjemplares(ejemplares);
		libro.setPrestados(0);
		libro.setAutor(autor);
		libro.setEditorial(editorial);

		return libroRepositorio.save(libro);
	}

	@Transactional
	public Libro modificar(String idAutor, String idEditorial, Long isbn, String titulo, Integer anio,
			Integer ejemplares) throws ErrorServicio {

		validar(isbn, titulo, anio, ejemplares);

		Optional<Libro> respuesta = libroRepositorio.findById(isbn);
		if (respuesta.isPresent()) {
			Libro libro = respuesta.get();
			if (libro.getEditorial().getId().equals(idEditorial) && libro.getAutor().getId().equals(idAutor)) {
				libro.setIsbn(isbn);
				libro.setTitulo(titulo);
				libro.setAnio(anio);
				libro.setEjemplares(ejemplares);

				return libroRepositorio.save(libro);
			} else {
				throw new ErrorServicio("El libro no corresponde a la Editorial / Autor.");
			}
		} else {
			throw new ErrorServicio("No se encontró el libro.");
		}
	}

	@Transactional
	public Libro consultar(Long isbn) throws ErrorServicio {
		Optional<Libro> respuesta = libroRepositorio.findById(isbn);
		if (respuesta.isPresent()) {
			Libro libro = respuesta.get();
			return libro;
		} else {
			throw new ErrorServicio("No se encontró el libro.");
		}
	}

	@Transactional
	public void eliminar(Long isbn) throws ErrorServicio {
		Optional<Libro> respuesta = libroRepositorio.findById(isbn);
		if (respuesta.isPresent()) {
			Libro libro = respuesta.get();
			libroRepositorio.delete(libro);
		} else {
			throw new ErrorServicio("No se encontró el libro.");
		}
	}

	@Transactional
	public List<Libro> listarTodos() {
		return libroRepositorio.findAll();
	}

	public Libro getById(Long isbn) throws ErrorServicio{
		Optional<Libro> respuesta = libroRepositorio.findById(isbn);
		if (respuesta.isPresent()) {
			Libro libro = respuesta.get();
			return libro;
		} else {
			throw new ErrorServicio ("No se encontró el libro.");
		}
	}
	
	public Boolean contieneLibro(Long isbn) {
		Optional<Libro> respuesta = libroRepositorio.findById(isbn);
		if (respuesta.isPresent()) {
			return true;
		}else {
			return false;
		}
		
	}

	private void validar(Long isbn, String titulo, Integer anio, Integer ejemplares) throws ErrorServicio {
		if (isbn == null || isbn.equals(0)) {
			throw new ErrorServicio("El ISBN no puede estar vacío.");
		}
		if (titulo == null || titulo.isEmpty()) {
			throw new ErrorServicio("El título no puede estar vacío.");
		}
		if (anio == null || anio.equals(0)) {
			throw new ErrorServicio("El año no puede estar vacío.");
		}
		if (ejemplares == null || ejemplares.equals(0)) {
			throw new ErrorServicio("Los ejemplares no pueden estar vacío.");
		}
	}
}
