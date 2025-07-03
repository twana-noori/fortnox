package com.example.fortnox.utils;

import com.example.fortnox.config.HighSeasonProperties;
import com.example.fortnox.domain.CarModel;
import com.example.fortnox.domain.CarPricePeriod;
import com.example.fortnox.domain.PriceType;
import com.example.fortnox.domain.RentalPeriod;
import com.example.fortnox.exceptions.PricePlanNotFoundException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Comparator;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class PriceCalculator {

    private final HighSeasonProperties highSeason;

    public PriceCalculator(HighSeasonProperties highSeason) {
        this.highSeason = highSeason;
    }

    public CarPricePeriod choosePricePeriod(final CarModel model, final RentalPeriod period) {

        final long days = DAYS.between(period.startDate().date(), period.endDate().date()) + 1;

        return model.pricePeriods().stream()
                .filter(p -> isApplicable(p.priceType(), period, days))
                .min(Comparator.comparing(CarPricePeriod::priority))
                .orElseThrow(() -> new PricePlanNotFoundException("No price plan was matched " + model.model()));
    }

    private boolean isApplicable(PriceType type, RentalPeriod period, long days) {

        return switch (type) {
            case HIGH_SEASON -> highSeason.isHighSeason(period.startDate().date(), period.endDate().date());
            case WEEKEND -> isWeekendOnly(period);
            case WEEK -> days >= 7 && days < 30;
            case MONTH -> days >= 30;
            case REGULAR -> true;
        };
    }

    private boolean isWeekendOnly(RentalPeriod p) {
        return p.startDate().date().getDayOfWeek() == DayOfWeek.SATURDAY
                && p.endDate().date().getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
