package com.crewmeister.cmcodingchallenge.exchangerate.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import com.crewmeister.cmcodingchallenge.exchangerate.persistence.entity.CurrencyExchangeRateEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * JpaRepository for [ExchangeRate] entity
 *
 * @author jeevan
 */
@Repository
public interface CurrencyExchangeRateRepository {// extends JpaRepository<CurrencyExchangeRateEntity, String> {
    /**
     *
     * @return
     */
    @Query("SELECT DISTINCT currencyCode FROM CurrencyExchangeRateEntity")
    List<String> findDistinctCurrencyCode();

    /**
     *
     * @param date
     * @return
     */
    List<CurrencyExchangeRateEntity> findByDate(LocalDate date);

    /**
     *
     * @param date
     * @param currencyCode
     * @return
     */
    CurrencyExchangeRateEntity findByDateAndCurrencyCode(LocalDate date, String currencyCode);

    List<CurrencyExchangeRateEntity> findAll();

    List<CurrencyExchangeRateEntity> saveAll(List<CurrencyExchangeRateEntity> currencyExchangeRateEntities);

    CurrencyExchangeRateEntity save(CurrencyExchangeRateEntity entity);
}
