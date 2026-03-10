package com.example.fortnox.repository;

import com.example.fortnox.model.Car;
import com.example.fortnox.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
        carRepository.save(new Car("Toyota", "Camry", 2020));
        carRepository.save(new Car("Honda", "Civic", 2021));
        carRepository.save(new Car("Volvo", "S60", 2022));
        carRepository.save(new Car("Ford", "Focus", 2019));
    }

    @Test
    void findAvailableCars_shouldExcludeVolvoCars() {
        List<Car> availableCars = carRepository.findAvailableCars();
        assertThat(availableCars).noneMatch(car -> car.getMake().equals("Volvo"));
    }

    @Test
    void findAll_shouldExcludeVolvoCars() {
        List<Car> allCars = carRepository.findAll();
        assertThat(allCars).noneMatch(car -> car.getMake().equals("Volvo"));
    }
}