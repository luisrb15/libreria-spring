package com.libreria.ej1.servicios;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.libreria.ej1.entidades.Cliente;
import com.libreria.ej1.errores.ErrorServicio;
import com.libreria.ej1.repositorios.ClienteRepositorio;

@Service
public class ClienteServicio implements UserDetailsService {

	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Transactional
	public void crear(Long documento, String nombre, String apellido, String domicilio, String telefono) throws ErrorServicio{
		validar(documento, nombre, apellido, domicilio, telefono);
		Cliente cliente = new Cliente();
		cliente.setDocumento(documento);
		cliente.setNombre(nombre);
		cliente.setApellido(apellido);
		cliente.setDomicilio(domicilio);
		cliente.setTelefono(telefono);
		
		clienteRepositorio.save(cliente);
	}
	
	public void consultar(Long documento, String nombre, String apellido, String domicilio, String telefono) throws ErrorServicio{
		validar(documento, nombre, apellido, domicilio, telefono);
		Optional<Cliente>respuesta = clienteRepositorio.findById(documento);
		if (respuesta.isPresent()) {
			Cliente cliente = respuesta.get();
			cliente.getNombre();
			cliente.getApellido();
			cliente.getDomicilio();
			cliente.getTelefono();
			
			clienteRepositorio.save(cliente);
		} else {
			throw new ErrorServicio("El cliente no existe.");
		}
	}
	
	@Transactional
	public Cliente modificar(Long documento, String nombre, String apellido, String domicilio, String telefono) throws ErrorServicio{
		validar(documento, nombre, apellido, domicilio, telefono);
		Optional<Cliente>respuesta = clienteRepositorio.findById(documento);
		if (respuesta.isPresent()) {
			Cliente cliente = respuesta.get();
			cliente.setNombre(nombre);
			cliente.setApellido(apellido);
			cliente.setDomicilio(domicilio);
			cliente.setTelefono(telefono);
			
			return clienteRepositorio.save(cliente);
		} else {
			throw new ErrorServicio("El cliente no existe.");
		}
	}
	
	@Transactional
	public Cliente getById (Long documento)throws ErrorServicio {
		
		Optional<Cliente> respuesta = clienteRepositorio.findById(documento);
		if (respuesta.isPresent()) {
			Cliente cliente = respuesta.get();
			return cliente;
		} else {
			throw new ErrorServicio("No se encontró al autor.");
		}
	}
	
	@Transactional
	public void eliminar(Long documento) throws ErrorServicio{
		Optional<Cliente>respuesta = clienteRepositorio.findById(documento);
		if (respuesta.isPresent()) {
			Cliente cliente = respuesta.get();
			cliente = null;
			
			clienteRepositorio.save(cliente);
		}
	}
	
	@Transactional
	public List<Cliente> listarClientes(){
		return clienteRepositorio.findAllClientes(Sort.by("nombre"));
	}
	
	public Boolean contieneCliente(Long documento) {
		Optional<Cliente> respuesta = clienteRepositorio.findById(documento);
		if (respuesta.isPresent()) {
			return true;
		}else {
			return false;
		}	
	}
	
	public void validar (Long documento, String nombre, String apellido, String domicilio, String telefono) throws ErrorServicio{
		if (apellido == null || apellido.isEmpty()) {
			throw new ErrorServicio("El apellido no puede estar vacío.");
		}
		if (documento == null || documento.equals(0)) {
			throw new ErrorServicio("El documento no puede estar vacío.");
		}
		if (domicilio == null || domicilio.isEmpty()) {
			throw new ErrorServicio("El domicilio no puede estar vacío.");
		}
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("El nombre no puede estar vacío.");
		}
		if (telefono == null || telefono.isEmpty()) {
			throw new ErrorServicio("El telefono no puede estar vacío.");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
	
