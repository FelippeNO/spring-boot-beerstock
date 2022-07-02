package com.beerstock.springbootbeerstock.controller;

/*
 * Passo 4: Onde acontece toda a operação inicial do Padrão Rest.
 * */

import com.beerstock.springbootbeerstock.dto.BeerDTO;
import com.beerstock.springbootbeerstock.exception.BeerAlreadyRegisteredException;
import com.beerstock.springbootbeerstock.exception.BeerNotFoundException;
import com.beerstock.springbootbeerstock.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class BeerController implements BeerControllerDocs {

    private final BeerService beerService;

    @Override
    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        return null;
    }

    @Override
    public BeerDTO findByName(String name) throws BeerNotFoundException {
        return null;
    }

    @Override
    public List<BeerDTO> listBeers() {
        return null;
    }

    @Override
    public void deleteById(Long id) throws BeerNotFoundException {

    }
}
