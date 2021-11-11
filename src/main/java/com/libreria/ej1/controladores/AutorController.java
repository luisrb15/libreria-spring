package com.libreria.ej1.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.libreria.ej1.entidades.Autor;
import com.libreria.ej1.servicios.AutorServicio;

@RequestMapping("/autor")
@Controller
public class AutorController {

	@Autowired
	private AutorServicio autorServicio;

	@GetMapping("/listar")
	public String listarAutores(ModelMap model) {

		model.addAttribute("autores", autorServicio.listarAutores());
		return "autores.html";
	}

	@GetMapping("/form")
	public String formulario() {
		return "form-autor.html";
	}

	@PostMapping("/form")
	public String agregarAutores(ModelMap model, @RequestParam String nombre, @RequestParam(required = false) String id) {

		try {
			if (id == null) {
				autorServicio.crear(nombre);
				model.put("exito", "Autor añadido correctamente.");
				return "form-autor.html";
			} else {
				Autor autor = autorServicio.modificar(id, nombre);
				model.put("exito", "Autor editado correctamente.");
				model.put("autor", autor);
				return "form-autor.html";
			}
		} catch (Exception e) {
			if (id == null) {
				model.put("error", "el autor no pudo ser añadido");
				return "form-autor.html";
			} else {
				model.put("error", "el autor no pudo ser modificado");
				return "form-autor.html";
			}
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarAutor(ModelMap model, @PathVariable String id) throws Exception {

		try {
			autorServicio.eliminar(id);
			return "redirect:/autor/listar";
		} catch (Exception e) {
			model.put("error", "el autor no pudo ser eliminado");
			throw new Exception("No se pudo eliminar el autor");
		}
	}

	@GetMapping("/modificar/{id}")
	public String formularioModificar(@PathVariable("id") String id, ModelMap model ) {
		try {
			Autor autor = autorServicio.getById(id);
			model.addAttribute("autor", autor);
			return "form-autor.html";
		} catch (Exception e) {
			model.put("error", "el autor no pudo ser añadido");
			return "form-autor.html";
		}
	}
}
