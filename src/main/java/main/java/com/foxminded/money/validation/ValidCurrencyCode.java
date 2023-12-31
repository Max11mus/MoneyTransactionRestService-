package main.java.com.foxminded.money.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = CurrencyCodeValidator.class)
@Documented
public @interface ValidCurrencyCode {
    String message() default "${validatedValue}  isn't correct " +
            " currency code. See https://www.iban.com/currency-codes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
