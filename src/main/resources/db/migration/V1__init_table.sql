CREATE TABLE users (
                       username VARCHAR(30) PRIMARY KEY,
                       nick VARCHAR(30) NOT NULL,
                       telegram VARCHAR(30) NOT NULL,
                       role VARCHAR(15) NOT NULL
);


CREATE TABLE auth_data (
                           username VARCHAR(30) PRIMARY KEY,
                           password VARCHAR(100) NOT NULL,
                           CONSTRAINT fk_auth_user FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE cities (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(30) NOT NULL,
                        region VARCHAR(50) NOT NULL
);

CREATE TABLE executors (
                           username VARCHAR(30) PRIMARY KEY,
                           passport_series_number VARCHAR(50) NOT NULL,
                           weight DOUBLE PRECISION NOT NULL,
                           height DOUBLE PRECISION NOT NULL,
                           rating DOUBLE PRECISION DEFAULT 0.0 NOT NULL,
                           completed_orders INTEGER DEFAULT 0 NOT NULL,
                           CONSTRAINT fk_executor_user FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE mutilations(
                           id BIGSERIAL PRIMARY KEY,
                           type VARCHAR(100) NOT NULL,
                           price INTEGER NOT NULL
);

CREATE TABLE victims (
                         id BIGSERIAL PRIMARY KEY,
                         first_name VARCHAR(15) NOT NULL,
                         last_name VARCHAR(15) NOT NULL,
                         workplace VARCHAR(50),
                         position VARCHAR(15),
                         residence VARCHAR(30),
                         phone VARCHAR(15),
                         description TEXT
);

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        username VARCHAR(30) NOT NULL,
                        city_id BIGINT NOT NULL,
                        victim_id BIGINT NOT NULL,
                        deadline DATE NOT NULL,
                        status VARCHAR(15) NOT NULL,
                        CONSTRAINT fk_orders_user FOREIGN KEY (username) REFERENCES users(username),
                        CONSTRAINT fk_orders_city FOREIGN KEY (city_id) REFERENCES cities(id),
                        CONSTRAINT fk_orders_victim FOREIGN KEY (victim_id) REFERENCES victims(id)
);


CREATE TABLE order_mutilations (
                                   order_id BIGINT NOT NULL,
                                   mutilation_id BIGINT NOT NULL,
                                   PRIMARY KEY (order_id, mutilation_id),
                                   CONSTRAINT fk_order_mutilations_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                                   CONSTRAINT fk_order_mutilations_mutilation FOREIGN KEY (mutilation_id) REFERENCES mutilations(id) ON DELETE CASCADE
);


CREATE TABLE fights (
                        id BIGSERIAL PRIMARY KEY,
                        executor_id VARCHAR REFERENCES executors(username),
                        order_id BIGINT REFERENCES orders(id),
                        status VARCHAR(15) NOT NULL
);