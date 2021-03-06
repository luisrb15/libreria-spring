package com.libreria.ej1.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libreria.ej1.entidades.Editorial;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String>{

}
