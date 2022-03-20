package com.crewmeister.cmcodingchallenge.service;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.CurrencyConversionRates;
import com.crewmeister.cmcodingchallenge.model.JasonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CurrencyService {

    @Autowired
    private RestTemplate restTemplate;

    /*
     * This method gets the response in Json format from the web service(using url)
     * and maps all the same attributes of our response(from the web) to the same properties of class
     * @JasonResponse, in return we will get a new instance of "JasonResponse".
     * @return -> list of available currencies
     */
    public List<Currency> getCurrencies() {

        JasonResponse response = getResponse();
        Set<Map.Entry<String, Double>> entries = response.getRates().entrySet();

        List<Currency> list = entries.stream().map(entry -> new Currency(entry.getKey())).collect(Collectors.toList());
        list.add(new Currency("EUR"));

        return list;
    }

    /*
     * @param -> givenCurrency(Currency, has to be converted in euro)
     * @param -> amount, which has to be converted in euro
     * @params -> year, month, date -> YYYY-MM-DD
     * @return -> converted amount in euro.
     */
    public double convertCurrency(String year, String month, String day,String givenCurrency, String amount) {

        JasonResponse response = getResponse(year,month,day);
        double fromFX = response.getRates().get(givenCurrency);

        return Math.round(((1/fromFX) * Integer.parseInt(amount)) * 100) / 100.0;
    }

    /*
     * @param -> from(startDate)
     * @param -> to(endDate)
     * @return -> all EU-FX rates in all available dates.
     * It takes 2 parameters and returns all the EU-FX rates in all available dates between the given time period
     * by the User.
     */

    public List<CurrencyConversionRates> getExchangeRates(String from, String to) throws ParseException {

        List<CurrencyConversionRates> rates = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter.parse(from);
        Date endDate = formatter.parse(to);

        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            rates.add(getConversionRatesAtParticularDay(String.valueOf(date.getYear()), String.valueOf(date.getMonth().getValue()), String.valueOf(date.getDayOfMonth())));
        }

        return rates;
    }

    /*
     * It gets the response in Json format from webservice and maps all the same attributes present in
     * @JasonResponse class and gives us a new instance of JasonResponse.
     * @params -> YYYY-MM-DD
     * @return -> all exchange rates against euro at a particular day.
     */
    public CurrencyConversionRates getConversionRatesAtParticularDay(String year, String month, String day) {

        JasonResponse response = getResponse(year,month,day);

        return new CurrencyConversionRates(response.getDate(),response.getBase(), response.getRates());
    }

    //helping methods
    private JasonResponse getResponse() {
        return restTemplate.getForObject("http://api.exchangeratesapi.io/v1/latest?access_key=cb4ca8e82f5dc9a28c092f11cfae8491", JasonResponse.class);
    }

    private JasonResponse getResponse(String year, String month, String day) {
        return restTemplate.getForObject("http://api.exchangeratesapi.io/v1/" + year + "-" + month + "-" + day + "?access_key=cb4ca8e82f5dc9a28c092f11cfae8491", JasonResponse.class);
    }
}
