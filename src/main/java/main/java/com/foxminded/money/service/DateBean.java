package main.java.com.foxminded.money.service;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class DateBean {
    private Clock clock;

    public DateBean() {
        this.clock =  Clock.system(ZoneId.of("Etc/UTC"));;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public Clock getClock() {
        return clock;
    }

    public LocalDate getDate() {
        return LocalDate.now(clock);
    }
}
