package com.cguerrero.peliculas.dao;

import org.springframework.data.repository.CrudRepository;

import com.cguerrero.peliculas.entities.Actor;

public interface IActorRepository extends CrudRepository<Actor, Long>{

}
