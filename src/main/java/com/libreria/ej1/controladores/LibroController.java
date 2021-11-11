package com.libreria.ej1.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.libreria.ej1.entidades.Autor;
import com.libreria.ej1.entidades.Editorial;
import com.libreria.ej1.entidades.Libro;
import com.libreria.ej1.servicios.AutorServicio;
import com.libreria.ej1.servicios.EditorialServicio;
import com.libreria.ej1.servicios.LibroServicio;

@Controller
@RequestMapping("/libro")
public class LibroController {

	@Autowired
	private LibroServicio libroServicio;

	@Autowired
	private AutorServicio autorServicio;

	@Autowired
	private EditorialServicio editorialServicio;

	@GetMapping("/listar")
	public String lista(ModelMap modelo) {
		modelo.addAttribute("libros", libroServicio.listarTodos());
		return "libros.html";
	}

	@GetMapping("/form")
	public String formulario(ModelMap model) {
		try {
			List<Autor> autores = autorServicio.listarAutores();
			model.put("autores", autores);
			List<Editorial> editoriales = editorialServicio.listarEditoriales();
			model.put("editoriales", editoriales);
			return "form-libro.html";
		} catch (Exception e) {
			model.put("error", "No se encontraron autores o editoriales.");
			return "form-libro.html";
		}
	}

	@GetMapping("/form-modificar")
	public String formularioModificar(ModelMap model) {
		try {
			List<Autor> autores = autorServicio.listarAutores();
			model.put("autores", autores);
			List<Editorial> editoriales = editorialServicio.listarEditoriales();
			model.put("editoriales", editoriales);
			return "form-libro-modificar.html";
		} catch (Exception e) {
			model.put("error", "No se encontraron autores o editoriales.");
			return "form-libro-modificar.html";
		}
	}
	
	@PostMapping("/form")
	public String agregarLibro(ModelMap model, @RequestParam String isbn, @RequestParam String titulo,
			@RequestParam String anio, @RequestParam String ejemplares, @RequestParam String idAutor,
			@RequestParam String idEditorial) {

		try {
				libroServicio.crear(idAutor, idEditorial, Long.valueOf(isbn), titulo, Integer.valueOf(anio),
						Integer.valueOf(ejemplares));
				model.put("exito", "Libro añadido correctamente.");
				return "form-libro.html";
		} catch (Exception e) {
				model.put("error", "el libro no pudo ser añadido");
				return "form-libro.html";
		}
	}
	
	@PostMapping("/form-modificar")
	public String modificarLibro(ModelMap model, @RequestParam String isbn, @RequestParam String titulo,
			@RequestParam String anio, @RequestParam String ejemplares, @RequestParam String idAutor,
			@RequestParam String idEditorial) {

		try {
				Libro libro = libroServicio.modificar(idAutor, idEditorial, Long.valueOf(isbn), titulo,
						Integer.valueOf(anio), Integer.valueOf(ejemplares));
				model.put("exito", "Libro editado correctamente.");
				model.put("libro", libro);
				return "form-libro-modificar.html";
		} catch (Exception e) {
				model.put("error", "el libro no pudo ser modificado");
				return "form-libro.html";
		}
	}

	@GetMapping("/eliminar/{isbn}")
	public String eliminarLibro(ModelMap model, @PathVariable("isbn") String isbn) {
		try {
			libroServicio.eliminar(Long.valueOf(isbn));
			return "redirect:/libro/listar";
		} catch (Exception e) {
			model.put("error", "El libro no pudo ser eliminado");
			return "libros.html";
		}
	}

	@GetMapping("/modificar/{isbn}")
	public String modificarLibro(ModelMap model, @PathVariable("isbn") String isbn) {
		try {
			Libro libro = libroServicio.getById(Long.valueOf(isbn));
			model.addAttribute("libro", libro);
			List<Autor> autores = autorServicio.listarAutores();
			model.put("autores", autores);
			List<Editorial> editoriales = editorialServicio.listarEditoriales();
			model.put("editoriales", editoriales);
			return "form-libro-modificar.html";
		} catch (Exception e) {
			model.put("error", "El libro no pudo ser modificado.");
			return "form-libro-modificar.html";
		}
	}
}
