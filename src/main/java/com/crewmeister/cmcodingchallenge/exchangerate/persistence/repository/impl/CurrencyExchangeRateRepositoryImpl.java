package com.crewmeister.cmcodingchallenge.exchangerate.persistence.repository.impl;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.MapUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.crewmeister.cmcodingchallenge.exchangerate.persistence.entity.CurrencyExchangeRateEntity;
import com.crewmeister.cmcodingchallenge.exchangerate.persistence.repository.CurrencyExchangeRateRepository;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * Temp in-mem database
 *
 * @author jeevan
 */
@Slf4j
@Repository
public class CurrencyExchangeRateRepositoryImpl implements CurrencyExchangeRateRepository {

    private Map<LocalDate, Map<String, CurrencyExchangeRateEntity>> base = new ConcurrentHashMap<>();

    /**
     * @return
     */
    @Override
    public List<String> findDistinctCurrencyCode() {
        return base.values()
                .stream().map(Map::values)
                .flatMap(Collection::stream)
                .map(CurrencyExchangeRateEntity::getCurrencyCode)
                .distinct()
                .collect(toList());
    }

    /**
     * @param date
     * @return
     */
    @Override
    public List<CurrencyExchangeRateEntity> findByDate(LocalDate date) {
        notNull(date, "Date cannot be null");
        log.debug("Dates: {}", base.keySet());

        Map<String, CurrencyExchangeRateEntity> rateMap = base.get(date);
        if (isEmpty(rateMap)) {
            return emptyList();
        }

        return new ArrayList<>(rateMap.values());
    }

    /**
     * @param date
     * @param currencyCode
     * @return
     */
    @Override
    public CurrencyExchangeRateEntity findByDateAndCurrencyCode(LocalDate date, String currencyCode) {
        notNull(date, "Date cannot be null");
        isTrue(isNotBlank(currencyCode), "currencyCode cannot be empty");

        Optional<CurrencyExchangeRateEntity> entity = findByDate(date).stream()
                .filter(c -> currencyCode.equals(c.getCurrencyCode())).findFirst();
        if (entity.isEmpty()) {
            return null;
        }

        return entity.get();
    }

    /**
     * @return
     */
    @Override
    public List<CurrencyExchangeRateEntity> findAll() {
        return base.values()
                .stream().map(Map::values)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    /**
     * @param entityList
     * @return
     */
    @Override
    public List<CurrencyExchangeRateEntity> saveAll(List<CurrencyExchangeRateEntity> entityList) {
        for (CurrencyExchangeRateEntity entity : entityList) {
            save(entity);
        }
        return findAll();
    }

    @Override
    public CurrencyExchangeRateEntity save(CurrencyExchangeRateEntity entity) {
        LocalDate date = entity.getDate();
        Map<String, CurrencyExchangeRateEntity> map = base.get(date);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            base.put(date, map);
        }

        map.put(entity.getCurrencyCode(), entity);

        return entity;
    }
}
