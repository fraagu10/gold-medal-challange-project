package com.codecademy.goldmedal.data;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    List<Country> findAllByOrderByNameAsc();
    List<Country> findAllByOrderByNameDesc();
    List<Country> findAllByOrderByGdpAsc();
    List<Country> findAllByOrderByGdpDesc();
    List<Country> findAllByOrderByPopulationAsc();
    List<Country> findAllByOrderByPopulationDesc();
    Optional<Country> findByName(String name);
}
