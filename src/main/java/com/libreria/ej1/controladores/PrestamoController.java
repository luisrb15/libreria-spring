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

import com.libreria.ej1.entidades.Cliente;
import com.libreria.ej1.entidades.Libro;
import com.libreria.ej1.entidades.Prestamo;
import com.libreria.ej1.errores.ErrorServicio;
import com.libreria.ej1.servicios.ClienteServicio;
import com.libreria.ej1.servicios.LibroServicio;
import com.libreria.ej1.servicios.PrestamoServicio;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {

	@Autowired
	private PrestamoServicio prestamoServicio;

	@Autowired
	private LibroServicio libroServicio;

	@Autowired
	private ClienteServicio clienteServicio;

	@GetMapping("/listar")
	public String listarPrestamo(ModelMap model) throws ErrorServicio {
		try {
		model.addAttribute("prestamos", prestamoServicio.listarPrestamos());
		return "prestamos.html";
		}catch(Exception e) {
			throw new ErrorServicio("Error en controller.");
		}
	}

	@GetMapping("/form")
	public String formulario(ModelMap model) {
		try {
			List<Libro> libros = libroServicio.listarTodos();
			model.put("libros", libros);
			List<Cliente> clientes = clienteServicio.listarClientes();
			model.put("clientes", clientes);
			return "form-prestamo.html";
		} catch (Exception e) {
			model.put("error", "No se encontraron libros o clientes.");
			return "form-prestamo.html";
		}
	}

	@PostMapping("/form")
	public String cargarPrestamo(ModelMap model, @RequestParam(required = false) String id,
			@RequestParam String idLibro, @RequestParam String idCliente) {
		try {
			if (id == null) {
				try {
					Prestamo prestamo = prestamoServicio.crear(Long.valueOf(idLibro), Long.valueOf(idCliente));
					model.put("exito", "Libro prestado al cliente");
					model.put("prestamo", prestamo);
					return "form-prestamo.html";
				} catch (Exception e) {
					List<Libro> libros = libroServicio.listarTodos();
					model.put("libros", libros);
					List<Cliente> clientes = clienteServicio.listarClientes();
					model.put("clientes", clientes);
					model.put("error", "No quedan más copias disponibles.");
					return "form-prestamo.html";
				}
			} else {
				model.put("error", "El préstamo no pudo ser completado");
				return "form-prestamo.html";
			}
		} catch (Exception e) {
			return "form-prestamo.html";
		}
	}

	@GetMapping("/renovar/{id}")
	public String renovarPrestamo(ModelMap model, @PathVariable("id") String id) {
		try {
			prestamoServicio.renovarPrestamo(id);
			model.addAttribute("prestamos", prestamoServicio.listarPrestamos());
			model.put("exito", "préstamo renovado");
			return "redirect:/prestamo/listar";
		} catch (Exception e) {
			model.put("error", "No se renovó el préstamo.");
			return "prestamos.html";
		}
	}

	@GetMapping("/devolver/{id}")
	public String devolverPrestamo(ModelMap model, @PathVariable("id") String id) {
		try {
			prestamoServicio.eliminar(id);
			model.put("exito", "Libro devuelto exitosamente.");
			return "redirect:/prestamo/listar";
		} catch (Exception e) {
			model.put("error", "No se pudo devolver el libro.");
			return "prestamos.html";
		}
	}
}
