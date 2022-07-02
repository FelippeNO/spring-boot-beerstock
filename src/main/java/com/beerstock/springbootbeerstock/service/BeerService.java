package com.beerstock.springbootbeerstock.service;

/*
 * Passo 3: Classe gerenciada pelo Spring. Classe para injeção
 * nos Controllers.
 * */

import com.beerstock.springbootbeerstock.entity.Beer;
import com.beerstock.springbootbeerstock.repository.BeerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//Lombok gera o Autowired automaticamente.
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeetDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.toDTIO(savedBeer);
    }

}
