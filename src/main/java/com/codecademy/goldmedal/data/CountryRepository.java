package com.codecademy.goldmedal.data;

import com.codecademy.goldmedal.model.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Integer> {
}
