package main.java.com.foxminded.money.validation;

import org.springframework.stereotype.Component;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

@Component
public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {
    final static Collection<CurrencyUnit> allCurrencies = Monetary.getCurrencies();
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (allCurrencies.stream()
                .anyMatch(currency -> currency.getCurrencyCode().equals(value))) {
            return true;
        }

        return false;
    }
}
