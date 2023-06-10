package com.cguerrero.peliculas.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cguerrero.peliculas.entities.Pelicula;

public interface IPeliculaRepository extends JpaRepository<Pelicula, Long>{

}
