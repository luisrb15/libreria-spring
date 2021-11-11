package com.libreria.ej1.servicios;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.libreria.ej1.entidades.Cliente;
import com.libreria.ej1.entidades.Libro;
import com.libreria.ej1.entidades.Prestamo;
import com.libreria.ej1.errores.ErrorServicio;
import com.libreria.ej1.repositorios.ClienteRepositorio;
import com.libreria.ej1.repositorios.LibroRepositorio;
import com.libreria.ej1.repositorios.PrestamoRepositorio;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PrestamoServicio implements UserDetailsService {

	private LibroRepositorio libroRepositorio;
	private ClienteRepositorio clienteRepositorio;
	private PrestamoRepositorio prestamoRepositorio;

	@Transactional
	public Prestamo crear(Long idLibro, Long idCliente) throws ErrorServicio {

		Libro libro = libroRepositorio.findById(idLibro).get();
		Cliente cliente = clienteRepositorio.findById(idCliente).get();

		try {
			Prestamo prestamo = new Prestamo();
			prestamo.setFecha(new Date());
			prestamo.setDevolucion(sumarDiasFecha(prestamo.getFecha()));
			prestamo.setMulta(0.0);
			prestamo.setLibro(libro);
			prestamo.setCliente(cliente);

			if (libro.getEjemplares() > libro.getPrestados()) {
				libro.setPrestados(libro.getPrestados()+1);
				return prestamoRepositorio.save(prestamo);
			} else {
				throw new ErrorServicio("No quedan más copias.");
				//return null;
			}
		} catch (Exception e) {
			throw new ErrorServicio("Error en crear préstamo.");
		}
	}

	private Date sumarDiasFecha(Date fecha) throws ErrorServicio {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha); // Configuramos la fecha que se recibe calendar.add(Calendar.DAY_OF_YEAR, dias);
			calendar.add(Calendar.DAY_OF_YEAR, 7);// numero de días a añadir, o restar en caso de días<0
			
			return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
		} catch (Exception e) {
			throw new ErrorServicio("Error en sumar días");
		}
	}

	public void renovarPrestamo(String id) throws ErrorServicio {
		Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Prestamo prestamo = respuesta.get();
			prestamo.setDevolucion(sumarDiasFecha(prestamo.getDevolucion()));
			prestamoRepositorio.save(prestamo);
		} else {
			throw new ErrorServicio("No se encontró el préstamo a renovar");
		}
	}

	public Prestamo getById(String id) throws ErrorServicio {
		Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Prestamo prestamo = respuesta.get();
			return prestamo;
		} else {
			throw new ErrorServicio("No se encontró el prestamo.");
		}
	}

	@Transactional
	public void modificar(Long idLibro, Long idCliente, String idPrestamo, Date fecha, Date devolucion, Double multa)
			throws ErrorServicio {
		validar(fecha, devolucion);
		Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
		if (respuesta.isPresent()) {
			Prestamo prestamo = respuesta.get();
			if (prestamo.getLibro().getIsbn().equals(idLibro)
					&& prestamo.getCliente().getDocumento().equals(idCliente)) {
				prestamo.setFecha(fecha);
				prestamo.setDevolucion(devolucion);
				prestamo.setMulta(multa);
			} else {
				throw new ErrorServicio("El préstamo no corresponde al cliente / libro.");
			}
		} else {
			throw new ErrorServicio("No existe el prestamo.");
		}
	}

	public void consultar(String idPrestamo) throws ErrorServicio {
		Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
		if (respuesta.isPresent()) {
			Prestamo prestamo = respuesta.get();

			prestamoRepositorio.save(prestamo);
		} else {
			throw new ErrorServicio("No existe el préstamo solicitado.");
		}
	}

	@Transactional
	public void eliminar(String id) throws ErrorServicio {
		Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
		if (respuesta.isPresent()) {
			Prestamo prestamo = respuesta.get();
			prestamo.getLibro().setPrestados(prestamo.getLibro().getPrestados()-1);
			prestamoRepositorio.delete(prestamo);

		} else {
			throw new ErrorServicio("No existe el préstamo solicitado.");
		}
	}

	@Transactional
	public List<Prestamo> listarPrestamos() throws ErrorServicio {
		try {
			return prestamoRepositorio.findAllPrestamos();
		} catch (Exception e) {
			throw new ErrorServicio("No se pudo listar los prestamos.");
		}
	}

	public void validar(Date fecha, Date devolucion) throws ErrorServicio {
		if (fecha == null) {
			throw new ErrorServicio("La fecha no puede estar vacía.");
		}
		if (devolucion == null) {
			throw new ErrorServicio("La fecha de devolución no puede estar vacía.");
		}
		if (devolucion.compareTo(fecha) <= 0) {
			throw new ErrorServicio("La fecha de devolucion no puede ser anterior a la de préstamo.");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
