package com.libreria.ej1.repositorios;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.libreria.ej1.entidades.Autor;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String>{
	
	/*@Query("SELECT a from Autor a WHERE a.nombre LIKE :nombre ORDER BY :nombre ASC")
	public List<Autor> searchAssets(@Param("nombre") String nombre);*/

	@Query(value = "SELECT a FROM Autor a")
	List<Autor> findAllAutores(Sort sort);
}
