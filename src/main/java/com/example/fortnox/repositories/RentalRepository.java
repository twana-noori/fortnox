package com.example.fortnox.repositories;

import com.example.fortnox.domain.BookingType;
import com.example.fortnox.domain.Car;
import com.example.fortnox.domain.CreateRental;
import com.example.fortnox.domain.Id;
import com.example.fortnox.domain.Rental;
import com.example.fortnox.domain.RentalDate;
import com.example.fortnox.domain.User;
import com.example.fortnox.exceptions.InvalidSQLDataException;
import com.example.fortnox.exceptions.RentalException;
import com.example.fortnox.exceptions.UserNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class RentalRepository {

    private final JdbcTemplate jdbc;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public RentalRepository(
            final JdbcTemplate jdbc,
            final CarRepository carRepository,
            final UserRepository userRepository
    ) {
        this.jdbc = jdbc;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public List<Rental> getAllRentals() {
        final String sql = """
                SELECT id, start_date, end_date, revenue, car_id, user_id, booking_type
                FROM rental
                """;
        try {
            return jdbc.query(sql, createRentalRowMapper());
        } catch (DataAccessException e) {
            throw new RentalException("Could not get rentals: " + e.getMessage());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Long saveRental(final CreateRental createRental,
                           final BigDecimal revenue) {

        final String duplicateSql = """
                    SELECT COUNT(*) FROM rental
                    WHERE car_id = ?
                      AND start_date <= ?
                      AND end_date >= ?
                """;

        final Integer duplicates = jdbc.queryForObject(duplicateSql, Integer.class,
                createRental.carId().value(),
                Date.valueOf(createRental.rentalPeriod().endDate().date()),
                Date.valueOf(createRental.rentalPeriod().startDate().date())
        );

        if (duplicates != null && duplicates > 0) {
            throw new RentalException("Car is already rented during this period.");
        }
        final User user = userRepository.findById(createRental.user().id().value())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        final String sql = """
                    INSERT INTO rental (car_id, user_id, start_date, end_date, revenue, booking_type)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbc.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                ps.setLong(1, createRental.carId().value());
                ps.setLong(2, user.id().value());
                ps.setString(3, String.valueOf(createRental.rentalPeriod().startDate().date()));
                ps.setString(4, String.valueOf(createRental.rentalPeriod().endDate().date()));
                ps.setBigDecimal(5, revenue);
                ps.setString(6, createRental.bookingType().name());
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new RentalException("Could not save rental: " + e.getMessage());
        }

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }


    private RowMapper<Rental> createRentalRowMapper() {

        return (rs, rowNum) -> {
            final var startDateValid = RentalDate.validate(rs.getString("start_date"));
            final var endDateValid = RentalDate.validate(rs.getString("end_date"));

            final Id userId = Id.validate(rs.getLong("user_id"))
                    .getOrElseThrow(() -> new InvalidSQLDataException("Invalid user ID"));
            final Id carId = Id.validate(rs.getLong("car_id"))
                    .getOrElseThrow(() -> new InvalidSQLDataException("Invalid car ID"));

            final BookingType bookingType = BookingType.validate(rs.getString("booking_type"))
                    .getOrElseThrow(() -> new InvalidSQLDataException("Invalid booking type"));

            // Not ideal for performance.
            final User user = userRepository.findById(userId.value())
                    .orElseThrow(() -> new RentalException("User not found"));
            final Car car = carRepository.findById(carId.value())
                    .orElseThrow(() -> new RentalException("Car not found"));

            return new Rental(
                    rs.getLong("id"),
                    startDateValid.getOrElseThrow(() -> new InvalidSQLDataException(startDateValid.getError())).date(),
                    endDateValid.getOrElseThrow(() -> new InvalidSQLDataException(endDateValid.getError())).date(),
                    rs.getBigDecimal("revenue"),
                    car,
                    user,
                    bookingType
            );
        };
    }
}
