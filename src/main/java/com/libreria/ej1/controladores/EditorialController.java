package com.libreria.ej1.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.libreria.ej1.entidades.Editorial;
import com.libreria.ej1.errores.ErrorServicio;
import com.libreria.ej1.servicios.EditorialServicio;

@Controller
@RequestMapping("/editorial")
public class EditorialController {

	@Autowired
	private EditorialServicio editorialServicio;
	
	@GetMapping("/listar")
	public String listarEditoriales(ModelMap model) throws ErrorServicio{
		
		model.addAttribute("editoriales", editorialServicio.listarEditoriales());
		return "editoriales.html";
	}
	@GetMapping("/form")
	public String formulario() {
		return "form-editorial.html";
	}

	@PostMapping("/form")
	public String agregarEditoriales(ModelMap model, @RequestParam String nombre, @RequestParam(required = false) String id) {

		try {
			if (id == null) {
				editorialServicio.crear(nombre);
				model.put("exito", "Editorial añadida correctamente.");
				return "form-editorial.html";
			} else {
				Editorial editorial = editorialServicio.modificar(id, nombre);
				model.put("exito", "Editorial editada correctamente.");
				model.put("editorial", editorial);
				return "form-editorial.html";
			}
		} catch (Exception e) {
			if (id == null) {
				model.put("error", "la editorial no pudo ser añadida");
				return "form-editorial.html";
			} else {
				model.put("error", "la editorial no pudo ser modificada");
				return "form-editorial.html";
			}
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarEditorial(ModelMap model, @PathVariable String id) {

		try {
			editorialServicio.eliminar(id);
			model.put("exito", "editorial eliminada correctamente");
			return "redirect:/editorial/listar";
		} catch (Exception e) {
			model.put("error", "la editorial no pudo ser eliminada");
			return "editoriales.html";
		}
	}

	@GetMapping("/modificar/{id}")
	public String formularioModificar(@PathVariable("id") String id, ModelMap model ) {
		try {
			Editorial editorial = editorialServicio.getById(id);
			model.addAttribute("editorial", editorial);
			return "form-editorial.html";
		} catch (Exception e) {
			model.put("error", "la editorial no pudo ser añadida");
			return "form-editorial.html";
		}
	}

}
