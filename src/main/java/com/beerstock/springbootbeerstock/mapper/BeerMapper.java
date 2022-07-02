package com.beerstock.springbootbeerstock.mapper;

import com.beerstock.springbootbeerstock.dto.BeerDTO;
import com.beerstock.springbootbeerstock.entity.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    Beer toModel(BeerDTO beerDTO);

    BeerDTO toDTO(Beer beer);
}