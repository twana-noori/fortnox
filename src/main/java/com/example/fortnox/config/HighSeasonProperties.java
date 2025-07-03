package com.example.fortnox.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "highseason")
public class HighSeasonProperties {

    private List<Period> periods;

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(final List<Period> periods) {
        this.periods = periods;
    }

    public boolean isHighSeason(final LocalDate start, final LocalDate end) {
        return periods.stream().anyMatch(p ->
                !start.isAfter(p.end) && !end.isBefore(p.start)
        );
    }

    public static class Period {
        private LocalDate start;
        private LocalDate end;

        public LocalDate getStart() {
            return start;
        }

        public void setStart(final LocalDate start) {
            this.start = start;
        }

        public LocalDate getEnd() {
            return end;
        }

        public void setEnd(final LocalDate end) {
            this.end = end;
        }
    }
}
