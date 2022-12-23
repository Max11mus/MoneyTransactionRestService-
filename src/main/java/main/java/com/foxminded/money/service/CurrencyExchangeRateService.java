package main.java.com.foxminded.money.service;

import main.java.com.foxminded.money.dto.PrivatBankExchangeRateDto;
import main.java.com.foxminded.money.dto.PrivatBankExchangeRatesDto;
import main.java.com.foxminded.money.exeptions.ServiceUnavailableException;
import main.java.com.foxminded.money.validation.ValidCurrencyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
public class CurrencyExchangeRateService {
    Logger LOGGER = LoggerFactory.getLogger(CurrencyExchangeRateService.class);

    @Autowired
    private DateService dateService;

    private RestTemplate restTemplate = new RestTemplate();

    @Value( "${privat.bank.exchange.rate.service.url}" )
    private String url;
    private String path = "/p24api/exchange_rates";
    private String jsonParamName = "json";
    private String dateParamName = "date";

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private String baseCurrency;
    private String rateDate;
    private Map<String, Double> sellRates = new HashMap<String, Double>();
    private Map<String, Double> purchaseRates = new HashMap<String, Double>();

    public Double getSellExchangeRate(@ValidCurrencyCode String currency) {
        getExchangeRates();

        if (currency.equals(baseCurrency)) {
            return new Double(1);
        }

        if (!sellRates.containsKey(currency)) {
            throw new IllegalStateException("There is no exchange rate for currency "
                    + currency + " from service " + url);
        }

        return sellRates.get(currency);
    }

    public Double getPurchaseExchangeRate(@ValidCurrencyCode String currency) {
        getExchangeRates();

        if (currency.equals(baseCurrency)) {
            return new Double(1);
        }

        if (!sellRates.containsKey(currency)) {
            throw new IllegalStateException("There is no exchange rate for currency "
                    + currency + " from service " + url);
        }

        return purchaseRates.get(currency);
    }

    public String getBaseCurrency() {
        getExchangeRates();

        return baseCurrency;
    }

    private PrivatBankExchangeRatesDto getDtoFromUrl() {

        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl(url)
                .path(path)
                .queryParam(jsonParamName)
                .queryParam(dateParamName, rateDate)
                .build()
                .encode();


        LOGGER.info("Send GET request to " + uriComponents.toUriString());
        ResponseEntity<PrivatBankExchangeRatesDto> response = restTemplate.getForEntity(uriComponents.toUriString(),
                PrivatBankExchangeRatesDto.class);


        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            rateDate = "";
            LOGGER.error("Service unavailable " + uriComponents.toUriString());
            throw new ServiceUnavailableException(response.getStatusCode().toString()
                    + uriComponents.toUriString() + System.lineSeparator()
                    + response.getHeaders().toString());
        }

        PrivatBankExchangeRatesDto result = response.getBody();
        LOGGER.info("Result " + result.toString());
        return result;
    }

    private void parseDto(PrivatBankExchangeRatesDto dto) {
        baseCurrency = dto.getBaseCurrencyLit();

        sellRates.clear();
        purchaseRates.clear();

        List<PrivatBankExchangeRateDto> exchangeRates = dto.getExchangeRate();

        exchangeRates.forEach((rate) -> {
            sellRates.put(rate.getCurrency(), Double.valueOf(rate.getSaleRateNB()));
            purchaseRates.put(rate.getCurrency(), Double.valueOf(rate.getPurchaseRateNB()));
        });
    }

    private void getExchangeRates() {
        String currentDate = dateTimeFormatter.format(dateService.getDate());
        if (!currentDate.equals(rateDate)) {
            rateDate = currentDate;
            parseDto(getDtoFromUrl());
        }
    }
}
