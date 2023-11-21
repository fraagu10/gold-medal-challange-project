package com.codecademy.goldmedal.data;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer> {
    List<GoldMedal> findByCountryOrderByYearDesc(String country);
    List<GoldMedal> findByCountryOrderByYearAsc(String country);
    List<GoldMedal> findByCountryOrderBySeasonDesc(String country);
    List<GoldMedal> findByCountryOrderBySeasonAsc(String country);
    List<GoldMedal> findByCountryOrderByCityDesc(String country);
    List<GoldMedal> findByCountryOrderByCityAsc(String country);
}
