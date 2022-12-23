package main.java.com.foxminded.money.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PrivatBankExchangeRateDto {
    String currency;
    String saleRateNB;
    String PurchaseRateNB;
}
