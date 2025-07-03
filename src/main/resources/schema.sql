SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS rental;
DROP TABLE IF EXISTS car_price_period;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS car_model;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     date_of_birth VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS car_model (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         brand VARCHAR(255) NOT NULL,
                                         model VARCHAR(255) NOT NULL,
                                         model_year INT NOT NULL
);

CREATE TABLE IF NOT EXISTS cars (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    registration_number VARCHAR(50) NOT NULL UNIQUE,
                                    car_model_id BIGINT NOT NULL,
                                    FOREIGN KEY (car_model_id) REFERENCES car_model(id)
);

CREATE TABLE IF NOT EXISTS car_price_period (
                                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                car_model_id BIGINT NOT NULL,
                                                price_per_day DECIMAL(10,2) NOT NULL,
                                                price_type VARCHAR(100),
                                                priority INT,
                                                FOREIGN KEY (car_model_id) REFERENCES car_model(id)
);

CREATE TABLE IF NOT EXISTS rental (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      car_id BIGINT NOT NULL,
                                      user_id BIGINT NOT NULL,
                                      start_date DATE NOT NULL,
                                      end_date DATE NOT NULL,
                                      revenue DECIMAL(10,2) NOT NULL,
                                      booking_type VARCHAR(10) NOT NULL,
                                      FOREIGN KEY (car_id) REFERENCES cars(id),
                                      FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO users (name, date_of_birth) VALUES ('Körbara Anna', '1979-08-04');
INSERT INTO users (name, date_of_birth) VALUES ('Unga Kalle', '2005-02-02');

INSERT INTO car_model (brand, model, model_year) VALUES ('Volvo', 'S60', 2020);
INSERT INTO car_model (brand, model, model_year) VALUES ('Ford', 'Mustang', 2021);

INSERT INTO cars (registration_number, car_model_id) VALUES ('ABC123', 1);
INSERT INTO cars (registration_number, car_model_id) VALUES ('XYZ789', 2);
INSERT INTO cars (registration_number, car_model_id) VALUES ('TTT222', 1);

INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (1, 1500, 'REGULAR', 5);
INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (1, 1500, 'WEEKEND', 4);
INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (1, 800, 'WEEK', 3);
INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (1, 600, 'MONTH', 2);
INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (1, 3000, 'HIGH_SEASON', 1);

INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (2, 1200, 'REGULAR', 5);
INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (2, 2500, 'WEEKEND', 4);
INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (2, 1000, 'WEEK', 3);
INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (2, 6000, 'MONTH', 2);
INSERT INTO car_price_period (car_model_id, price_per_day, price_type, priority)
VALUES (2, 4000, 'HIGH_SEASON', 1);

-- Bilen ABC123 uthyrd dagar i juli
INSERT INTO rental (car_id, user_id, start_date, end_date,revenue, booking_type)
VALUES (1, 1, '2026-07-10', '2026-07-15',  18000.00, 'ONLINE');
-- Bilen XYZ789 uthyrd dagar i juli
INSERT INTO rental (car_id, user_id, start_date, end_date, revenue, booking_type)
VALUES (2, 2, '2026-07-13', '2026-06-21', 5000.00, 'MANUAL');

-- Bilen ABC123 är även uthyrd en vecka i augusti
INSERT INTO rental (car_id, user_id, start_date, end_date, revenue, booking_type)
VALUES (1, 2, '2026-08-01', '2026-08-07', 5600.00, 'MANUAL');

-- Bilen XYZ789 uthyrd månad i juni
INSERT INTO rental (car_id, user_id, start_date, end_date,revenue, booking_type)
VALUES (2, 1, '2026-06-01', '2026-06-30', 180000.00, 'ONLINE');
