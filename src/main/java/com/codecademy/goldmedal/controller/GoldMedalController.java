package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.data.CountryRepository;
import com.codecademy.goldmedal.data.GoldMedalRepository;
import com.codecademy.goldmedal.model.*;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
public class GoldMedalController {

     private final CountryRepository countryRepository;
     private final GoldMedalRepository goldMedalRepository;

    public GoldMedalController(final CountryRepository countryRepository, final GoldMedalRepository goldMedalRepository) {
         this.countryRepository = countryRepository;
         this.goldMedalRepository = goldMedalRepository;
    }

     @GetMapping
     public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
         var ascendingOrder = ascending.toLowerCase().equals("y");
         return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
     }

     @GetMapping("/{country}")
     public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
         String countryName = WordUtils.capitalizeFully(country);
         return getCountryDetailsResponse(countryName);
     }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
             case "year":
                 medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByYearAsc(countryName) : this.goldMedalRepository.findByCountryOrderByYearDesc(countryName);
                 break;
             case "season":
                 medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderBySeasonAsc(countryName) : this.goldMedalRepository.findByCountryOrderBySeasonDesc(countryName);
                 break;
             case "city":
                 medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByCityAsc(countryName) : this.goldMedalRepository.findByCountryOrderByCityDesc(countryName);
                 break;
             case "name":
                 medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByNameAsc(countryName) : this.goldMedalRepository.findByCountryOrderByNameDesc(countryName);
                 break;
             case "event":
                 medalsList = ascendingOrder ? this.goldMedalRepository.findByCountryOrderByEventAsc(countryName) : this.goldMedalRepository.findByCountryOrderByEventDesc(countryName);
                 break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

     private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
         var countryOptional = this.countryRepository.findByName(countryName);
         if (countryOptional.isEmpty()) {
             return new CountryDetailsResponse(countryName);
         }

         var country = countryOptional.get();
         var goldMedalCount = this.goldMedalRepository.countByCountry(countryName);

         var summerWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName, "Summer");
         var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
         var totalSummerEvents = this.goldMedalRepository.countBySeason("Summer");
         var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
         var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

         var winterWins = this.goldMedalRepository.findByCountryAndSeasonOrderByYearAsc(countryName, "Winter");
         var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
         var totalWinterEvents = this.goldMedalRepository.countBySeason("Winter");
         var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
         var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

         var numberEventsWonByFemaleAthletes = this.goldMedalRepository.countByCountryAndGender(countryName, "Women");
         var numberEventsWonByMaleAthletes = this.goldMedalRepository.countByCountryAndGender(countryName, "Men");

         return new CountryDetailsResponse(
                 countryName,
                 country.getGdp(),
                 country.getPopulation(),
                 goldMedalCount,
                 numberSummerWins,
                 percentageTotalSummerWins,
                 yearFirstSummerWin,
                 numberWinterWins,
                 percentageTotalWinterWins,
                 yearFirstWinterWin,
                 numberEventsWonByFemaleAthletes,
                 numberEventsWonByMaleAthletes);
     }

     private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
         List<Country> countries;
         switch (sortBy) {
             case "name":
                 countries = ascendingOrder ? this.countryRepository.findAllByOrderByNameAsc() : this.countryRepository.findAllByOrderByNameDesc();
                 break;
             case "gdp":
                 countries = ascendingOrder ? this.countryRepository.findAllByOrderByGdpAsc() : this.countryRepository.findAllByOrderByGdpDesc();
                 break;
             case "population":
                 countries = ascendingOrder ? this.countryRepository.findAllByOrderByPopulationAsc() : this.countryRepository.findAllByOrderByPopulationDesc();
                 break;
             case "medals":
             default:
                 countries = (List<Country>) this.countryRepository.findAll();
                 break;
         }

         var countrySummaries = getCountrySummariesWithMedalCount(countries);

         if (sortBy.equalsIgnoreCase("medals")) {
             countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
         }

         return countrySummaries;
     }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
         for (Country country : countries) {
             var goldMedalCount = this.goldMedalRepository.countByCountry(country.getName());
             countrySummaries.add(new CountrySummary(country, goldMedalCount));
         }
        return countrySummaries;
    }
}
