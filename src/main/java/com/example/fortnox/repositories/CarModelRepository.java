package com.example.fortnox.repositories;

import com.example.fortnox.domain.CarModel;
import com.example.fortnox.domain.CarPricePeriod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CarModelRepository {

    private final JdbcTemplate jdbc;
    private final CarPriceRepository carPriceRepository;

    public CarModelRepository(JdbcTemplate jdbc, CarPriceRepository carPriceRepository) {
        this.jdbc = jdbc;
        this.carPriceRepository = carPriceRepository;
    }

    public List<CarModel> findAll() {
        final String sql = "SELECT * FROM car_model";
        return jdbc.query(sql, carModelRowMapper);
    }

    public Optional<CarModel> findById(Long id) {
        final String sql = "SELECT * FROM car_model WHERE id = ?";
        return jdbc.query(sql, carModelRowMapper, id).stream().findFirst();
    }

    private final RowMapper<CarModel> carModelRowMapper = new RowMapper<>() {
        @Override
        public CarModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Long carModelId = rs.getLong("id");
            final List<CarPricePeriod> pricePeriods = carPriceRepository.findByCarModel(carModelId);

            return new CarModel(
                    carModelId,
                    rs.getString("brand"),
                    rs.getString("model"),
                    rs.getInt("model_year"),
                    pricePeriods
            );
        }
    };
}
