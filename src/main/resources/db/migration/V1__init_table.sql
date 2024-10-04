CREATE TABLE users (
                       username VARCHAR(30) PRIMARY KEY,
                       nick VARCHAR(30) NOT NULL,
                       telegram VARCHAR(30) NOT NULL,
                       role VARCHAR(15) NOT NULL
);


CREATE TABLE auth_data (
                           username VARCHAR(30) REFERENCES users(username) ON DELETE CASCADE,
                           password VARCHAR(100) NOT NULL,
                           PRIMARY KEY (username)
);

CREATE TABLE cities (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(30) NOT NULL,
                        region VARCHAR(50) NOT NULL
);

CREATE TABLE executors (
                           username VARCHAR(30) REFERENCES users(username) ON DELETE CASCADE,
                           passport_series_number VARCHAR(10) NOT NULL,
                           weight DOUBLE PRECISION NOT NULL,
                           height DOUBLE PRECISION NOT NULL,
                           rating DOUBLE PRECISION DEFAULT 0 NOT NULL,
                           completed_orders INTEGER DEFAULT 0 NOT NULL,
                           PRIMARY KEY (username)
);


CREATE TABLE mutilations(
                           id SERIAL PRIMARY KEY,
                           type VARCHAR(100) NOT NULL,
                           price INTEGER NOT NULL
);

CREATE TABLE victims (
                         id SERIAL PRIMARY KEY,
                         first_name VARCHAR(15) NOT NULL,
                         last_name VARCHAR(15) NOT NULL,
                         workplace VARCHAR(50),
                         position VARCHAR(15),
                         residence VARCHAR(30),
                         phone VARCHAR(15),
                         description TEXT
);

CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        username VARCHAR(30) REFERENCES users(username),
                        city_id BIGINT REFERENCES cities(id),
                        victim_id BIGINT REFERENCES victims(id),
                        deadline DATE NOT NULL,
                        status VARCHAR(15) NOT NULL
);

CREATE TABLE order_mutilations (
                                   order_id BIGINT REFERENCES orders(id),
                                   mutilation_id BIGINT REFERENCES mutilations(id),
                                   PRIMARY KEY (order_id, mutilation_id)
);

CREATE TABLE fights (
                        id SERIAL PRIMARY KEY,
                        executor_id VARCHAR REFERENCES executors(username),
                        order_id BIGINT REFERENCES orders(id),
                        status VARCHAR(15) NOT NULL
);