package main.java.com.foxminded.money.service;

import main.java.com.foxminded.money.dto.MoneyTransactionDto;
import main.java.com.foxminded.money.dto.MoneyTransactionMapper;
import main.java.com.foxminded.money.repository.MoneyTransactionRepository;
import main.java.com.foxminded.money.validation.ValidCurrencyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
public class MoneyTransactionService {
    @Autowired
    private MoneyTransactionRepository moneyTransactionRepository;

    @Autowired
    private MoneyTransactionMapper mapper;

    public List<MoneyTransactionDto> findMoneyTransactionsByOwnerInCurrency(UUID senderId,
                                                                            @ValidCurrencyCode String currencyCode) {
        List<MoneyTransactionDto> moneyTransactionDtos =
                moneyTransactionRepository.findByownerId(senderId).stream()
                        .map((m) -> mapper.convertMoneyTransactionInCurrency(m, currencyCode))
                        .collect(Collectors.toList());
        return moneyTransactionDtos;
    }

}