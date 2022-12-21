package main.java.com.foxminded.money.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.foxminded.money.validation.ValidCurrencyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Validated
public class CurrencyExchangeRateService {
    @Autowired
    private DateBean dateBean;

    private RestTemplate restTemplate = new RestTemplate();
    private String url = "https://api.privatbank.ua/p24api/exchange_rates?json&date="; // date format - "dd.MM.yyyy"
    private DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private String baseCurrency = "";
    private String rateDate = "";
    private Map<String, Double> sellRates = new HashMap<String, Double>();
    private Map<String, Double> purchaseRates = new HashMap<String, Double>();
    private ObjectMapper mapper = new ObjectMapper();

    private JsonNode getJsonFromUrl() {

        JsonNode jsonObject = null;
            ResponseEntity<String> response = restTemplate.getForEntity(url + rateDate, String.class);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                rateDate="";
                throw new ResponseStatusException(response.getStatusCode(),
                        url + rateDate + System.lineSeparator() + response.getHeaders().toString());
            }

            String body = response.getBody();
            try {
                jsonObject = mapper.readTree(body);
            } catch (JsonProcessingException e) {
                rateDate="";
                throw new RuntimeException(e);
            }
            return jsonObject;
    }

    private void parseJson(JsonNode json) {
        baseCurrency = json.get("baseCurrencyLit").asText();

        sellRates.clear();
        purchaseRates.clear();

        JsonNode exchangeRates = json.get("exchangeRate");

        if (exchangeRates.isArray()) {
            for (final JsonNode exchangeRate : exchangeRates) {
                String currency = exchangeRate.get("currency").asText();
                Double sellRate = exchangeRate.get("saleRateNB").asDouble();
                Double purchaseRate = exchangeRate.get("purchaseRateNB").asDouble();

                sellRates.put(currency, sellRate);
                purchaseRates.put(currency, purchaseRate);
            }
        }
    }

    private void getExchangeRates() {
        String currentDate = dateTimeFormatter.format(dateBean.getDate());
        if (!currentDate.equals(rateDate)) {
            rateDate = currentDate;
            parseJson(getJsonFromUrl());
        }
    }

    public Double getSellExchangeRate(@ValidCurrencyCode String currency) {
        getExchangeRates();

        if (currency.equals(baseCurrency)) {
            return new Double(1);
        }

        if (!sellRates.containsKey(currency)) {
            throw new IllegalStateException("There is no exchange rate for currency "
                    + currency + " from service " + url + rateDate);
        }

        return sellRates.get(currency);
    }

    public Double getPurschaseExchangeRate(@ValidCurrencyCode String currency) {
        getExchangeRates();

        if (currency.equals(baseCurrency)) {
            return new Double(1);
        }

        if (!sellRates.containsKey(currency)) {
            throw new IllegalStateException("There is no exchange rate for currency "
                    + currency + " from service " + url + rateDate);
        }

        return purchaseRates.get(currency);
    }

    public String getBaseCurrency() {
        getExchangeRates();

        return baseCurrency;
    }
}
