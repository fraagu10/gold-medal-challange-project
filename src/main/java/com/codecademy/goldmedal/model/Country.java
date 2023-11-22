package com.codecademy.goldmedal.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "gdp")
    private BigDecimal gdp;
    @Column(name = "population")
    private Integer population;

    public Country() {
    }

    public Country(Country country) {
        this.name = country.getName();
        this.code = country.getCode();
        this.gdp = country.getGdp();
        this.population = country.getPopulation();
    }

    public Country(String name, String code, BigDecimal gdp, Integer population) {
        this.name = name;
        this.code = code;
        this.gdp = gdp;
        this.population = population;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getGdp() {
        return this.gdp;
    }

    public void setGdp(BigDecimal gdp) {
        this.gdp = gdp;
    }

    public Integer getPopulation() {
        return this.population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }
}
