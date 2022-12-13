package com.crewmeister.cmcodingchallenge.exchangerate.service.impl;

import static java.lang.Double.parseDouble;
import static java.time.Duration.of;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newWorkStealingPool;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.io.FileUtils.copyURLToFile;
import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import com.crewmeister.cmcodingchallenge.exchangerate.persistence.entity.CurrencyExchangeRateEntity;
import com.crewmeister.cmcodingchallenge.exchangerate.persistence.repository.CurrencyExchangeRateRepository;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Service handling the complexity of loading the remote source, transforming and
 * persisting the exchange-rates locally.
 *
 * @author jeevan
 */
@Slf4j
@Component
public class ExchangeRateLoadingService {
    private final List<String> csvUrls;

    private final String tempLocation;

    private final CurrencyExchangeRateRepository repository;

    private final Integer maxThreads;

    /**
     * @param csvUrls
     * @param tempLocation
     * @param maxThreads
     * @param repository
     */
    public ExchangeRateLoadingService(
            @Value("${crewmeister.csv.source.urls}") List<String> csvUrls,
            @Value("${crewmeister.csv.templocation}") String tempLocation,
            @Value("${crewmeister.csv.maxthreads:-1}") Integer maxThreads,
            CurrencyExchangeRateRepository repository) {

        this.csvUrls = csvUrls;
        this.tempLocation = tempLocation;
        this.maxThreads = maxThreads;
        this.repository = repository;
    }

    public List<String> sources() {
        return csvUrls;
    }

    /**
     * Loads the remote files in the database. This is done once on start-up
     * and can be also scheduled to refresh the DB
     */
    @PostConstruct
    public void loadSources() throws InterruptedException {
        // Lets load all files in parallel
        ExecutorService executor;
        if (maxThreads != null && maxThreads > 0) {
            executor = newFixedThreadPool(maxThreads);
        } else {
            executor = newWorkStealingPool();
        }

        CountDownLatch wg = new CountDownLatch(csvUrls.size());
        for (String csvUrl : csvUrls) {
            executor.submit(() -> {
                try {
                    loadSource(csvUrl);
                } catch (IOException e) {
                    log.error(e.toString());
                }finally {
                    wg.countDown();
                }
            });
        }

        wg.await();
        executor.shutdown();
    }

    /**
     * Loads a given url - containing the echange rates in csv format
     *
     * @param csvURL
     * @throws IOException
     */
    private void loadSource(String csvURL) throws IOException {
        String currencyCode = null;
        List<CurrencyExchangeRateEntity> conversionRates = new ArrayList<>();
        List<CSVRecord> records = retrieveCSVContents(csvURL);

        for (CSVRecord record : records) {
            String key = record.get(0);
            String val = record.get(1);

            if (isBlank(key) || isBlank(val)) {
                continue;
            }

            if (record.getRecordNumber() == 7) {
                currencyCode = val;
                continue;
            }

            if (record.getRecordNumber() < 10 || ".".equals(val)) {
                continue;
            }

            conversionRates.add(new CurrencyExchangeRateEntity(
                    LocalDate.parse(key, ISO_DATE),
                    currencyCode,
                    parseDouble(val)));
        }

        log.info("Persisting {} records...", conversionRates.size());
        repository.saveAll(conversionRates);
        log.info("Done persisting {} records", conversionRates.size());
    }

    /**
     * Reads the raw contents and converts it into List of CSVRecord
     *
     * @param csvURL
     * @return
     * @throws IOException
     */
    private List<CSVRecord> retrieveCSVContents(String csvURL) throws IOException {
        URL source = new URL(csvURL);
        File tempFile = new File(tempLocation, randomUUID().toString());
        int timeout = of(1L, ChronoUnit.SECONDS).toMillisPart();

        log.info("Downloading {} -> {}...", csvURL, tempFile.getAbsolutePath());
        copyURLToFile(source, tempFile, timeout, timeout);

        try (FileReader in = new FileReader(tempFile);
             BufferedReader fileReader = new BufferedReader(in)) {

            CSVParser csvParser = parse(fileReader, DEFAULT);
            return csvParser.getRecords();

        } finally {
            log.info("temp-file deleted: {}", tempFile.delete());
        }
    }
}
