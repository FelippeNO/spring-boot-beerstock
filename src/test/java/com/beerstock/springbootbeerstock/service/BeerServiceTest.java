package com.beerstock.springbootbeerstock.service;

import com.beerstock.springbootbeerstock.builder.BeerDTOBuilder;
import com.beerstock.springbootbeerstock.dto.BeerDTO;
import com.beerstock.springbootbeerstock.entity.Beer;
import com.beerstock.springbootbeerstock.exception.BeerAlreadyRegisteredException;
import com.beerstock.springbootbeerstock.exception.BeerNotFoundException;
import com.beerstock.springbootbeerstock.mapper.BeerMapper;
import com.beerstock.springbootbeerstock.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {
    private static final long INVALID_BEER_ID = 1L;

    @Mock
    private BeerRepository beerRepository;
    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @InjectMocks
    private BeerService beerService;

    @Test
    void whenBeerInformedThenItShouldBeCreated()
    throws BeerAlreadyRegisteredException {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedSavedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(
                Optional.empty());
        when(beerRepository.save(expectedSavedBeer)).thenReturn(
                expectedSavedBeer);

        //then
        BeerDTO createdBeerDTO = beerService.createBeer(expectedBeerDTO);

        assertThat(createdBeerDTO.getId(),
                is(equalTo(expectedBeerDTO.getId())));
        assertThat(createdBeerDTO.getName(),
                is(equalTo(expectedBeerDTO.getName())));
        assertThat(createdBeerDTO.getQuantity(),
                is(equalTo(expectedBeerDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrow()
    throws BeerAlreadyRegisteredException {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer duplicatedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(
                Optional.of(duplicatedBeer));

        //then
        assertThrows(BeerAlreadyRegisteredException.class,
                () -> beerService.createBeer(expectedBeerDTO));
    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer()
    throws BeerNotFoundException {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        when(beerRepository.findByName(expectedFoundBeer.getName())).thenReturn(
                Optional.of(expectedFoundBeer));

        //then
        BeerDTO foundBeerDTO =
                beerService.findByName(expectedBeerDTO.getName());

        assertThat(foundBeerDTO, is(equalTo(expectedBeerDTO)));
    }

    @Test
    void whenNotRegisteredNameIsGivenThenThrowAnException()
    throws BeerNotFoundException {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        //when
        when(beerRepository.findByName(expectedBeerDTO.getName())).thenReturn(
                Optional.empty());

        //then
        assertThrows(BeerNotFoundException.class,
                () -> beerService.findByName(expectedBeerDTO.getName()));
    }
}
