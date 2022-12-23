package main.java.com.foxminded.money.entities;

import com.vladmihalcea.hibernate.type.money.MonetaryAmountType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.TypeDef;

import javax.money.MonetaryAmount;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "transaction_ums", schema = "ums")
@TypeDef(
        typeClass = MonetaryAmountType.class,
        defaultForType = MonetaryAmount.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MoneyTransaction {
    @Id
    @GeneratedValue
    @Column(name = "transaction_id", nullable = false)
    private UUID id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "receiver_id", nullable = false)
    private UUID receiverId;

    @Columns(columns = {
            @Column(name = "amount", nullable = false),
            @Column(name = "currency", nullable = false)})
    private MonetaryAmount monetaryAmount; // using Moneta (JSR 354 RI)
}