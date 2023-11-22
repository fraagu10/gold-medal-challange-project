package com.codecademy.goldmedal.data;

import com.codecademy.goldmedal.model.GoldMedal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer> {
    Optional<GoldMedal> findByName(String country);
    List<GoldMedal> findByCountryOrderByYearDesc(String country);
    List<GoldMedal> findByCountryOrderByYearAsc(String country);
    List<GoldMedal> findByCountryOrderBySeasonDesc(String country);
    List<GoldMedal> findByCountryOrderBySeasonAsc(String country);
    List<GoldMedal> findByCountryOrderByCityDesc(String country);
    List<GoldMedal> findByCountryOrderByCityAsc(String country);
    List<GoldMedal> findByCountryOrderByNameDesc(String country);
    List<GoldMedal> findByCountryOrderByNameAsc(String country);
    List<GoldMedal> findByCountryOrderByEventDesc(String country);
    List<GoldMedal> findByCountryOrderByEventAsc(String country);

    Integer countByCountry(String country);
    List<GoldMedal> findByCountryAndSeasonOrderByYearAsc(String country, String season);
    Integer countBySeason(String season);
    Integer countByCountryAndGender(String country, String gender);
}
