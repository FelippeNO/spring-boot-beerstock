package com.beerstock.springbootbeerstock.controller;

/*
 * Passo 4: Onde acontece toda a operação inicial do Padrão Rest.
 * */

import com.beerstock.springbootbeerstock.dto.BeerDTO;
import com.beerstock.springbootbeerstock.dto.QuantityDTO;
import com.beerstock.springbootbeerstock.exception.BeerAlreadyRegisteredException;
import com.beerstock.springbootbeerstock.exception.BeerNotFoundException;
import com.beerstock.springbootbeerstock.exception.BeerStockExceededException;
import com.beerstock.springbootbeerstock.service.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController implements BeerControllerDocs {

    private final BeerService beerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDTO createBeer(@RequestBody @Valid BeerDTO beerDTO)
    throws BeerAlreadyRegisteredException {
        return beerService.createBeer(beerDTO);
    }

    @GetMapping("/{name}")
    public BeerDTO findByName(@PathVariable String name)
    throws BeerNotFoundException {
        return beerService.findByName(name);
    }

    @GetMapping
    public List<BeerDTO> listBeers() {
        return beerService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws BeerNotFoundException {
        beerService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public BeerDTO increment(@PathVariable Long id,
                             @RequestBody @Valid QuantityDTO quantityDTO)
    throws BeerNotFoundException, BeerStockExceededException,
            BeerStockExceededException {
        return beerService.increment(id, quantityDTO.getQuantity());
    }

}
