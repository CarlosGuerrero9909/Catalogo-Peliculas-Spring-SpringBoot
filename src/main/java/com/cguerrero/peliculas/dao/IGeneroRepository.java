package com.cguerrero.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.cguerrero.peliculas.entities.Genero;

public interface IGeneroRepository extends CrudRepository<Genero, Long> {

}
