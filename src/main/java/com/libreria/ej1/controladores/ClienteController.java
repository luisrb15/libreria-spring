package com.libreria.ej1.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.libreria.ej1.entidades.Cliente;
import com.libreria.ej1.servicios.ClienteServicio;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	private ClienteServicio clienteServicio;

	@GetMapping("/listar")
	public String listarClientes(ModelMap model) {

		model.addAttribute("clientes", clienteServicio.listarClientes());
		return "clientes.html";
	}

	@GetMapping("/form")
	public String formulario() {
		return "form-cliente.html";
	}

	@PostMapping("/form")
	public String agregarClientes(ModelMap model, @RequestParam Long documento, @RequestParam String nombre,
			@RequestParam String apellido, @RequestParam String domicilio, @RequestParam String telefono) {

		try {
			if (clienteServicio.contieneCliente(documento)) {
				Cliente cliente = clienteServicio.modificar(documento, nombre, apellido, domicilio, telefono);
				model.put("exito", "Cliente editado correctamente.");
				model.put("cliente", cliente);
				return "form-cliente.html";
			} else {
				clienteServicio.crear(documento, nombre, apellido, domicilio, telefono);
				model.put("exito", "Cliente añadido correctamente.");
				return "form-cliente.html";				
			}
		} catch (Exception e) {
			if (documento == null) {
				model.put("error", "el cliente no pudo ser modificado");
				return "form-cliente.html";
			} else {				
				model.put("error", "el cliente no pudo ser añadido");
				return "form-cliente.html";
			}
		}
	}
	
	@GetMapping("/eliminar/{documento}")
	public String eliminarCliente(ModelMap model, @PathVariable String documento) throws Exception {

		try {
			clienteServicio.eliminar(Long.valueOf(documento));
			return "redirect:/cliente/listar";
		} catch (Exception e) {
			model.put("error", "el cliente no pudo ser eliminado");
			throw new Exception("No se pudo eliminar el cliente");
		}
	}

	@GetMapping("/modificar/{documento}")
	public String formularioModificar(@PathVariable("documento") String documento, ModelMap model ) {
		try {
			Cliente cliente = clienteServicio.getById(Long.valueOf(documento));
			model.addAttribute("cliente", cliente);
			return "form-cliente.html";
		} catch (Exception e) {
			model.put("error", "el cliente no pudo ser añadido");
			return "form-cliente.html";
		}
	}
}
