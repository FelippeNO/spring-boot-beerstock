package com.beerstock.springbootbeerstock.controller;

/*
 * Passo 4: Onde acontece toda a operação inicial do Padrão Rest.
 * */

import com.beerstock.springbootbeerstock.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class BeerController implements BeerControllerDocs {

    private final BeerService beerService;

}
