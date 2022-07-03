package com.beerstock.springbootbeerstock.service;

import com.beerstock.springbootbeerstock.builder.BeerDTOBuilder;
import com.beerstock.springbootbeerstock.dto.BeerDTO;
import com.beerstock.springbootbeerstock.entity.Beer;
import com.beerstock.springbootbeerstock.exception.BeerAlreadyRegisteredException;
import com.beerstock.springbootbeerstock.exception.BeerNotFoundException;
import com.beerstock.springbootbeerstock.exception.BeerStockExceededException;
import com.beerstock.springbootbeerstock.mapper.BeerMapper;
import com.beerstock.springbootbeerstock.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {
    private static final long INVALID_BEER_ID = 1L;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;
    @Mock
    private BeerRepository beerRepository;
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

    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers()
    throws BeerNotFoundException {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedFoundBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        when(beerRepository.findAll()).thenReturn(
                Collections.singletonList(expectedFoundBeer));

        //then
        List<BeerDTO> foundBeerDTOList = beerService.listAll();
        assertThat(foundBeerDTOList, is(not(empty())));
        assertThat(foundBeerDTOList.get(0), is(equalTo(expectedBeerDTO)));
    }

    @Test
    void whenListBeerIsCalledThenReturnAnEmptyListOfBeers()
    throws BeerNotFoundException {
        //when
        when(beerRepository.findAll()).thenReturn(
                Collections.EMPTY_LIST);

        //then
        List<BeerDTO> foundBeerDTOList = beerService.listAll();
        assertThat(foundBeerDTOList, is(empty()));
    }


    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted()
    throws BeerNotFoundException {
        // given
        BeerDTO expectedDeletedBeerDTO = BeerDTOBuilder.builder().build()
                .toBeerDTO();
        Beer expectedDeletedBeer = beerMapper.toModel(expectedDeletedBeerDTO);

        // when
        when(beerRepository.findById(
                expectedDeletedBeerDTO.getId())).thenReturn(
                Optional.of(expectedDeletedBeer));
        doNothing().when(beerRepository)
                .deleteById(expectedDeletedBeerDTO.getId());

        // then
        beerService.deleteById(expectedDeletedBeerDTO.getId());

        verify(beerRepository, times(1)).findById(
                expectedDeletedBeerDTO.getId());
        verify(beerRepository, times(1)).deleteById(
                expectedDeletedBeerDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementBeerStock()
    throws BeerNotFoundException, BeerStockExceededException,
            BeerStockExceededException {
        //given
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        //when
        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(
                Optional.of(expectedBeer));
        when(beerRepository.save(expectedBeer)).thenReturn(expectedBeer);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement =
                expectedBeerDTO.getQuantity() + quantityToIncrement;

        // then
        BeerDTO incrementedBeerDTO = beerService.increment(
                expectedBeerDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement,
                equalTo(incrementedBeerDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement,
                lessThan(expectedBeerDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreaterThanMaxThenThrowException() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(
                Optional.of(expectedBeer));

        int quantityToIncrement = 80;
        assertThrows(BeerStockExceededException.class,
                () -> beerService.increment(expectedBeerDTO.getId(),
                        quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreaterThanMaxThenThrowException() {
        BeerDTO expectedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer expectedBeer = beerMapper.toModel(expectedBeerDTO);

        when(beerRepository.findById(expectedBeerDTO.getId())).thenReturn(
                Optional.of(expectedBeer));

        int quantityToIncrement = 45;
        assertThrows(BeerStockExceededException.class,
                () -> beerService.increment(expectedBeerDTO.getId(),
                        quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(
                Optional.empty());

        assertThrows(BeerNotFoundException.class,
                () -> beerService.increment(INVALID_BEER_ID,
                        quantityToIncrement));
    }
}
