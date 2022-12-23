package main.java.com.foxminded.money.repository;

import main.java.com.foxminded.money.entities.MoneyTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MoneyTransactionRepository extends CrudRepository<MoneyTransaction, UUID> {
    List<MoneyTransaction> findByownerId(UUID ownerId);

}