package com.libreria.ej1.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libreria.ej1.entidades.Libro;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Long>{

}
