package com.example.fortnox;

import com.example.fortnox.config.HighSeasonProperties;
import com.example.fortnox.domain.CarModel;
import com.example.fortnox.domain.CarPricePeriod;
import com.example.fortnox.domain.PriceType;
import com.example.fortnox.domain.RentalDate;
import com.example.fortnox.domain.RentalPeriod;
import com.example.fortnox.exceptions.PricePlanNotFoundException;
import com.example.fortnox.utils.PriceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class PriceCalculatorTest {

    @Autowired
    private HighSeasonProperties highSeason;
    @Autowired
    private PriceCalculator calculator;

    @BeforeEach
    void setUp() {
        highSeason = new HighSeasonProperties();
        calculator = new PriceCalculator(highSeason);
        HighSeasonProperties.Period period = new HighSeasonProperties.Period();
        HighSeasonProperties.Period period2 = new HighSeasonProperties.Period();
        period.setStart(LocalDate.of(2025, 7, 1));
        period.setEnd(LocalDate.of(2025, 8, 31));
        period2.setStart(LocalDate.of(2025, 12, 20));
        period2.setEnd(LocalDate.of(2026, 1, 5));
        highSeason.setPeriods(List.of(period, period2));
    }

    @Test
    void testChoosePricePeriod_HighSeason() {
        RentalPeriod period = rentalPeriod(LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 7));
        CarPricePeriod highSeasonPeriod = pricePeriod(1, PriceType.HIGH_SEASON, 1);
        CarModel model = new CarModel(1L, "Volvo", "XC60", 2022, List.of(highSeasonPeriod));

        assertEquals(highSeasonPeriod, calculator.choosePricePeriod(model, period));
    }

    @Test
    void testChoosePricePeriod_Weekend() {
        RentalPeriod period = rentalPeriod(LocalDate.of(2024, 6, 8), LocalDate.of(2024, 6, 9));
        CarPricePeriod weekendPeriod = pricePeriod(2, PriceType.WEEKEND, 1);
        CarModel model = new CarModel(1L, "Volvo", "XC60", 2022, List.of(weekendPeriod));

        assertEquals(weekendPeriod, calculator.choosePricePeriod(model, period));
    }

    @Test
    void testChoosePricePeriod_Week() {
        RentalPeriod period = rentalPeriod(LocalDate.of(2024, 6, 3), LocalDate.of(2024, 6, 15));
        CarPricePeriod weekPeriod = pricePeriod(3, PriceType.WEEK, 1);
        CarModel model = new CarModel(1L, "Volvo", "XC60", 2022, List.of(weekPeriod));

        assertEquals(weekPeriod, calculator.choosePricePeriod(model, period));
    }

    @Test
    void testChoosePricePeriod_Month() {
        RentalPeriod period = rentalPeriod(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30));
        CarPricePeriod monthPeriod = pricePeriod(4, PriceType.MONTH, 1);
        CarModel model = new CarModel(1L, "Volvo", "XC60", 2022, List.of(monthPeriod));

        assertEquals(monthPeriod, calculator.choosePricePeriod(model, period));
    }

    @Test
    void testChoosePricePeriod_Regular() {
        RentalPeriod period = rentalPeriod(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 2));
        CarPricePeriod regularPeriod = pricePeriod(5, PriceType.REGULAR, 1);
        CarModel model = new CarModel(1L, "Volvo", "XC60", 2022, List.of(regularPeriod));

        assertEquals(regularPeriod, calculator.choosePricePeriod(model, period));
    }

    @Test
    void testChoosePricePeriod_MultiplePeriods_ChoosesHighestPriority() {
        RentalPeriod period = rentalPeriod(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 2));
        CarPricePeriod p1 = pricePeriod(6, PriceType.REGULAR, 1);
        CarPricePeriod p2 = pricePeriod(7, PriceType.MONTH, 2);
        CarModel model = new CarModel(1L, "Volvo", "XC60", 2022, List.of(p1, p2));

        assertEquals(p1, calculator.choosePricePeriod(model, period));
    }

    @Test
    void testChoosePricePeriod_EmptyList_Throws() {
        RentalPeriod period = rentalPeriod(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 2));
        CarModel model = new CarModel(1L, "Volvo", "XC60", 2022, List.of());

        PricePlanNotFoundException ex = assertThrows(PricePlanNotFoundException.class,
                () -> calculator.choosePricePeriod(model, period));
        assertTrue(ex.getMessage().contains("No price plan was matched"));
    }

    private RentalPeriod rentalPeriod(LocalDate start, LocalDate end) {
        return new RentalPeriod(new RentalDate(start), new RentalDate(end));
    }
    private CarPricePeriod pricePeriod(long id, PriceType type, int priority) {
        return new CarPricePeriod(id, BigDecimal.valueOf(100), type, priority);
    }
}
