package com.example.fortnox.repositories;

import com.example.fortnox.domain.Car;
import com.example.fortnox.domain.CarModel;
import com.example.fortnox.domain.Id;
import com.example.fortnox.exceptions.CarNotFoundException;
import com.example.fortnox.exceptions.CarSQLException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class CarRepository {

    private final JdbcTemplate jdbc;
    private final CarModelRepository carModelRepository;

    public CarRepository(JdbcTemplate jdbc,
                         CarModelRepository carModelRepository) {
        this.jdbc = jdbc;
        this.carModelRepository = carModelRepository;
    }

    public List<Car> findAll() {
        final String sql = "SELECT * FROM cars";
        return jdbc.query(sql, carRowMapper);
    }

    public Optional<Car> findById(Long id) {
        final String sql = "SELECT * FROM cars WHERE id = ?";
        return jdbc.query(sql, carRowMapper, id).stream().findFirst();
    }

    public List<Car> findAvailableCarsByModel(final LocalDate startDate, final LocalDate endDate, final Long carModelId) {
        final String sql = """
        SELECT * FROM cars c
        WHERE c.car_model_id = ?
          AND NOT EXISTS (
              SELECT 1 FROM rental r
              WHERE r.car_id = c.id
                AND r.start_date <= ?
                AND r.end_date >= ?
          )
        """;
        return jdbc.query(sql, carRowMapper, carModelId, endDate, startDate);
    }

    public List<Car> findAllAvailableCars(final LocalDate startDate, final LocalDate endDate) {
        final String sql = """
        SELECT * FROM cars c
        WHERE NOT EXISTS (
            SELECT 1 FROM rental r
            WHERE r.car_id = c.id
              AND r.start_date <= ?
              AND r.end_date >= ?
        )
    """;
        return jdbc.query(sql, carRowMapper, endDate, startDate);
    }
    public CarModel findCarModelByCarId(final Id carId) {
        final String sql = "SELECT car_model_id FROM cars WHERE id = ?";

        try {
            Long carModelId = jdbc.queryForObject(sql, Long.class, carId.value());
            return carModelRepository.findById(carModelId)
                    .orElseThrow(() -> new CarSQLException("No CarModel found with ID: " + carModelId));

        } catch (EmptyResultDataAccessException e) {
            throw new CarNotFoundException("No CarModel found with ID: " + carId.value());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch CarModel for car ID: " + carId.value(), e);
        }
    }

    private final RowMapper<Car> carRowMapper = new RowMapper<>() {
        @Override
        public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long carModelId = rs.getLong("car_model_id");
            CarModel carModel = carModelRepository.findById(carModelId)
                    .orElseThrow(() -> new SQLException("Car model not found, id=" + carModelId));

            return new Car(
                    rs.getLong("id"),
                    rs.getString("registration_number"),
                    carModel
            );
        }
    };
}
