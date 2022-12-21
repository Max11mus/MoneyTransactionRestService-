
package main.java.com.foxminded.money.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.java.com.foxminded.money.validation.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MoneyTransactionDto {
    private java.util.UUID id;
    private LocalDateTime dateTime;

    @UUID
    @NotNull
    @NotBlank
    private String ownerId;

    @UUID
    @NotNull
    @NotBlank
    private String receiverId;

    private String currencyCode;

    private BigDecimal amount;
}
