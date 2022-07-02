package com.beerstock.springbootbeerstock.entity;

import com.beerstock.springbootbeerstock.enums.BeerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
* Passo 1: Cadastro das entidades com as devidas anotações
* para que o banco possa criá-las.
* */

@Data // Gera getters and setters.
@Entity //Descreve a quantidade para o mapeamento na JPA.
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private int max;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BeerType type; //ENUM
}
