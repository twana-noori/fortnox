package com.example.fortnox.repositories;

import com.example.fortnox.domain.CarPricePeriod;
import com.example.fortnox.domain.PriceType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CarPriceRepository {

    private final JdbcTemplate jdbc;

    public CarPriceRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<CarPricePeriod> findByCarModel(Long carModelId) {
        String sql = "SELECT * FROM car_price_period WHERE car_model_id = ?";
        return jdbc.query(sql, carPricePeriodRowMapper, carModelId);
    }

    private final RowMapper<CarPricePeriod> carPricePeriodRowMapper = new RowMapper<>() {
        @Override
        public CarPricePeriod mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CarPricePeriod(
                    rs.getLong("id"),
                    rs.getBigDecimal("price_per_day"),
                    PriceType.fromString(rs.getString("price_type")),
                    rs.getInt("priority")
            );
        }
    };
}
