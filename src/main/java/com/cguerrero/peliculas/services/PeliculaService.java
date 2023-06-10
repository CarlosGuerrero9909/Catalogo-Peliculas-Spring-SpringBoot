package com.cguerrero.peliculas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cguerrero.peliculas.dao.IPeliculaRepository;
import com.cguerrero.peliculas.entities.Pelicula;

@Service
public class PeliculaService implements IPeliculaService {
	
	@Autowired
	private IPeliculaRepository peliculaRepository;
	
	@Override
	public void save(Pelicula pelicula) {
		peliculaRepository.save(pelicula);
	}

	@Override
	public Pelicula findById(Long id) {
		return peliculaRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		peliculaRepository.deleteById(id);
	}

	@Override
	public List<Pelicula> findAll() {
		return (List<Pelicula>) peliculaRepository.findAll();
	}

	@Override
	public Page<Pelicula> findAll(Pageable pageable) {
		return peliculaRepository.findAll(pageable);
	}

}
