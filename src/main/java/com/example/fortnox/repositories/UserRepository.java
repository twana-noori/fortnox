package com.example.fortnox.repositories;

import com.example.fortnox.domain.Id;
import com.example.fortnox.domain.Name;
import com.example.fortnox.domain.DateOfBirth;
import com.example.fortnox.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> result = jdbc.query(sql, userRowMapper, id);
        return result.stream().findFirst();
    }

    private final RowMapper<User> userRowMapper = new RowMapper<>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            final var idValidation = Id.validate(rs.getLong("id"));
            final var nameValidation = Name.validateName(rs.getString("name"));
            final var dateOfBirth = DateOfBirth.validateDateOfBirthAndAge(rs.getString("date_of_birth"));
            return new User(
                    idValidation.getOrElseThrow(() -> new SQLException(idValidation.getError())),
                    nameValidation.getOrElseThrow(() -> new SQLException(nameValidation.getError())),
                    dateOfBirth.getOrElseThrow(() -> new SQLException(dateOfBirth.getError()))
            );
        }
    };
}

