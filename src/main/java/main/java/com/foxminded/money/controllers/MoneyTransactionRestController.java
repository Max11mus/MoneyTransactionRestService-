package main.java.com.foxminded.money.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import main.java.com.foxminded.money.dto.MoneyTransactionDto;
import main.java.com.foxminded.money.exeptions.ErrorResponce;
import main.java.com.foxminded.money.service.MoneyTransactionService;
import main.java.com.foxminded.money.validation.UUID;
import main.java.com.foxminded.money.validation.ValidCurrencyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping(value = "/money-transactions")
@Tag(name = "money-transactions", description = "MoneyTransaction API")
public class MoneyTransactionRestController {
    @Autowired
    private MoneyTransactionService moneyTransactionService;

    @Operation(summary = "Show List of Owner MoneyTransactions Show Amounts In Currency",
            description = "Show List of Owner MoneyTransactions Show Amounts In Currency",
            tags = {"money-transactions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
            @Content(array = @ArraySchema(schema = @Schema(implementation = MoneyTransactionDto.class)))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content =
            @Content(schema = @Schema(implementation = ErrorResponce.class))),
            @ApiResponse(responseCode = "503", description = "SERVICE UNAVAILABLE", content =
            @Content(schema = @Schema(implementation = ErrorResponce.class)))
    })
    @RequestMapping(value = "", method = RequestMethod.GET, params = {"ownerId", "currency"},
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<MoneyTransactionDto>> findMoneyTransactionsByOwner(
            @Parameter(description = "Owner UUID. Cannot null or empty",required = true)
            @RequestParam(value = "ownerId") @UUID String ownerId,
            @Parameter(description = "Currency Code. Cannot null or empty. See https://www.iban.com/currency-codes",
                    required = true)
            @RequestParam(value = "currency") @ValidCurrencyCode String currency) {

        List<MoneyTransactionDto> moneyTransactionsDtos = moneyTransactionService
                .findMoneyTransactionsByOwnerInCurrency(java.util.UUID.fromString(ownerId), currency);

        return ResponseEntity.ok().body(moneyTransactionsDtos);
    }

}
