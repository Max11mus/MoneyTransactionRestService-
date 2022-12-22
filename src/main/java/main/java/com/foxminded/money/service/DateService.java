package main.java.com.foxminded.money.service;

import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class DateService {
    private Clock clock;

    public DateService() {
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
