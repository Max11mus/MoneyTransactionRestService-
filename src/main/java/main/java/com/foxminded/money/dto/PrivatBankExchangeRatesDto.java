package main.java.com.foxminded.money.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PrivatBankExchangeRatesDto {
    String date;
    String baseCurrencyLit;
    List<PrivatBankExchangeRateDto> ExchangeRate;
}

