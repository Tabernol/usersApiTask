--liquibase formatted sql

--changeset maksKrasnopolskyi:1
CREATE TABLE IF NOT EXISTS krasnopolskyi_task_users_api.users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(64) NOT NULL UNIQUE,
    firstname VARCHAR(64) NOT NULL,
    lastname VARCHAR(64) NOT NULL,
    birth_date TIMESTAMP NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(32)
    );
