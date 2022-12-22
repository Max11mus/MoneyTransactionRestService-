package main.java.com.foxminded.money.dto;

import main.java.com.foxminded.money.entities.MoneyTransaction;
import main.java.com.foxminded.money.service.CurrencyExchangeRateService;
import org.javamoney.moneta.Money;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class MoneyTransactionMapper {

    @Autowired
    private CurrencyExchangeRateService currencyExchangeRateService;

    public MoneyTransactionDto entityToDto(MoneyTransaction entity) {
        MoneyTransactionDto dto = new MoneyTransactionDto();

        dto.setId(entity.getId());
        dto.setDateTime(entity.getDateTime());

        dto.setOwnerId(entity.getOwnerId().toString());

        dto.setReceiverId(entity.getReceiverId().toString());

        dto.setAmount(entity.getMonetaryAmount().getNumber().numberValueExact(BigDecimal.class));
        dto.setCurrencyCode(entity.getMonetaryAmount().getCurrency().toString());

        return dto;
    }

    public MoneyTransaction dtoToEntity(MoneyTransactionDto dto){
        MoneyTransaction entity = new MoneyTransaction();
        entity.setId(dto.getId());
        entity.setDateTime(dto.getDateTime());

        entity.setOwnerId(UUID.fromString(dto.getOwnerId()));
        entity.setReceiverId(UUID.fromString(dto.getReceiverId()));

        MonetaryAmount moneyAmount = Money.of(dto.getAmount(), dto.getCurrencyCode());
        entity.setMonetaryAmount(moneyAmount);

        return entity;
    }

    public MoneyTransactionDto convertMoneyTransactionInCurrency(MoneyTransaction entity,
                                                                    String currencyCode){
        if (currencyCode.equals(entity.getMonetaryAmount().getCurrency().toString())) {
            return entityToDto(entity);
        }

        MoneyTransactionDto result = new MoneyTransactionDto();
        result.setId(entity.getId());

        result.setDateTime(entity.getDateTime());

        result.setOwnerId(entity.getOwnerId().toString());
        result.setReceiverId(entity.getReceiverId().toString());

        result.setCurrencyCode(currencyCode);

        String baseCurrencyCode = currencyExchangeRateService.getBaseCurrency();
        if (baseCurrencyCode.equals(currencyCode)) {
            result.setAmount(entity.getMonetaryAmount().getNumber().numberValueExact(BigDecimal.class)
                    .multiply(BigDecimal.valueOf(
                            currencyExchangeRateService.getPurchaseExchangeRate(
                                    entity.getMonetaryAmount().getCurrency().toString()))));
        }
        else {
            result.setAmount(entity.getMonetaryAmount().getNumber().numberValueExact(BigDecimal.class)
                    .divide(BigDecimal.valueOf(currencyExchangeRateService.getSellExchangeRate(currencyCode)),
                            2, RoundingMode.HALF_EVEN));
        }

        return result;
    }

}