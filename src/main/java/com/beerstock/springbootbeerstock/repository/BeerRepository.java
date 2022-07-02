package com.beerstock.springbootbeerstock.repository;

import com.beerstock.springbootbeerstock.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Passo 2: É um DAO. Classe responsável de
 * conversar com o banco de dados.
 * */


public interface BeerRepository extends JpaRepository<Beer, Long> {
    Optional<Beer> findByName(String name);
}
