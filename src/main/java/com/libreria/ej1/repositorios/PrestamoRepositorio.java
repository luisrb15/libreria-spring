package com.libreria.ej1.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.libreria.ej1.entidades.Prestamo;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String>{

	@Query(value = "SELECT p FROM Prestamo p")
	List<Prestamo> findAllPrestamos();
}
