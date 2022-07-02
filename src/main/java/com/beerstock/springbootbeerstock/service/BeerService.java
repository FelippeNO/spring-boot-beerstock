package com.beerstock.springbootbeerstock.service;

/*
 * Passo 3: Classe gerenciada pelo Spring. Classe para injeção
 * nos Controllers.
 * */

import com.beerstock.springbootbeerstock.dto.BeerDTO;
import com.beerstock.springbootbeerstock.entity.Beer;
import com.beerstock.springbootbeerstock.exception.BeerAlreadyRegisteredException;
import com.beerstock.springbootbeerstock.mapper.BeerMapper;
import com.beerstock.springbootbeerstock.repository.BeerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//Lombok gera o Autowired automaticamente.
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(beerDTO.getName());
        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(savedBeer);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<Beer> optSavedBeer = beerRepository.findByName(name);
        if(optSavedBeer.isPresent()){
            throw new BeerAlreadyRegisteredException(name);
        }
    }

}
