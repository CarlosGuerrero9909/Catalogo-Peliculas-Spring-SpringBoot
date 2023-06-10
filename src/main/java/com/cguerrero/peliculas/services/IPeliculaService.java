package com.cguerrero.peliculas.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cguerrero.peliculas.entities.Pelicula;

public interface IPeliculaService {
	public void save(Pelicula pelicula);

	public Pelicula findById(Long id);

	public void delete(Long id);

	public List<Pelicula> findAll();

	public Page<Pelicula> findAll(Pageable pageable);
}
