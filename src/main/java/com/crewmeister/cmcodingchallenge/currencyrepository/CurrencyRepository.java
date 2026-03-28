package com.crewmeister.cmcodingchallenge.currencyrepository;


import com.crewmeister.cmcodingchallenge.currency.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CurrencyRepository extends JpaRepository<Currency,Long> {

    @Query("SELECT c FROM Currency c WHERE c.currencyName = :currency")
    Optional<Currency> findCurrencyName(@Param("currency") String currency);
}
