package com.crewmeister.cmcodingchallenge.controller;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;

@RestController()
@RequestMapping("/api")
public class CurrencyController {

    @Autowired
    private CurrencyService service;

    /*
     * @return -> list of all available currencies
     */

    @GetMapping("/currencies")
    public ResponseEntity<List<Currency>> getCurrencies() {
       List<Currency> currencies = service.getCurrencies();
       return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    /*
     * @param -> from (start Date -> YYYY-MM-DD)
     * @param -> to   (end Date -> YYYY-MM-DD)
     * @return -> collection of all the EU-FX rates against Euro at all available dates between the time period given by the User.
     */

    @GetMapping("/exchange-rates/from/{from}/to/{to}")
    public ResponseEntity<List<CurrencyConversionRates>> getExchangeRates(@PathVariable("from") String from,
                                                                          @PathVariable("to") String to) throws ParseException {
        return new ResponseEntity<>(service.getExchangeRates(from, to), HttpStatus.OK);
    }

    /*
     * @param -> YYYY
     * @param -> MM
     * @param -> DD
     * This method takes 3 parameters and gives the EU-FX rates at a particular Date(format YYYY-MM-DD) given by the User.
     * @return -> exchanges rates of all available currencies against Euro.
     */
    @GetMapping("/exchange-rates/{YYYY}-{MM}-{DD}")
    public ResponseEntity<CurrencyConversionRates> getExchangeRatesAtParticularDay(@PathVariable("YYYY") String year,
                                                                                   @PathVariable("MM") String month, @PathVariable("DD") String day) {
        return new ResponseEntity<>(service.getConversionRatesAtParticularDay(year, month, day), HttpStatus.OK);
    }

    /*
     * @param -> from (given currency to be converted into EU)
     * @param -> amount of given currency
     * @params -> YYYY-MM-DD (at particular date)
     * @return -> converted amount (from given currency to EU)
     */
   @GetMapping("/convert/from/{from}/amount/{amount}/{YYYY}-{MM}-{DD}")
    public ResponseEntity<Double> getConvertedAmount(@PathVariable("from") String from, @PathVariable("amount") String amount,
                                                     @PathVariable("YYYY") String year, @PathVariable("MM") String month, @PathVariable("DD") String day) {
        return new ResponseEntity<>(service.convertCurrency(year ,month ,day ,from ,amount), HttpStatus.OK);
    }
}
