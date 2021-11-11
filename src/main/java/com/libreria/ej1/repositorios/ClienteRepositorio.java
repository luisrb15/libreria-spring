package com.libreria.ej1.repositorios;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.libreria.ej1.entidades.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long>{

	@Query(value = "SELECT c FROM Cliente c")
	List<Cliente> findAllClientes(Sort sort);
}
