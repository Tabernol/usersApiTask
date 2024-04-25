--liquibase formatted sql

--changeset maksKrasnopolskyi:1
INSERT INTO users (email, firstname, lastname, birth_date, address, phone_number)
VALUES ('user1@example.com', 'John', 'Doe', '1980-01-01', '123 Main St', '1234567890'),
       ('user2@example.com', 'Jane', 'Smith', '1985-02-15', '456 Elm St', '2345678901'),
       ('user3@example.com', 'Michael', 'Johnson', '1978-05-20', '789 Oak St', '3456789012'),
       ('user4@example.com', 'Emily', 'Brown', '1990-08-10', '101 Maple St', '4567890123'),
       ('user5@example.com', 'David', 'Williams', '1982-11-30', '202 Pine St', '5678901234'),
       ('user6@example.com', 'Emma', 'Jones', '1975-03-25', '303 Cedar St', '6789012345'),
       ('user7@example.com', 'Matthew', 'Davis', '1988-06-12', '404 Birch St', '7890123456'),
       ('user8@example.com', 'Olivia', 'Miller', '1983-09-05', '505 Walnut St', '8901234567'),
       ('user9@example.com', 'Daniel', 'Wilson', '1970-12-15', '606 Spruce St', '9012345678'),
       ('user10@example.com', 'Sophia', 'Taylor', '1972-04-20', '707 Cherry St', '0123456789'),
       ('user11@example.com', 'James', 'Anderson', '1995-07-08', '808 Aspen St', '1234567890'),
       ('user12@example.com', 'Isabella', 'Martinez', '1998-10-25', '909 Cedar St', '2345678901'),
       ('user13@example.com', 'Benjamin', 'Hernandez', '1986-02-28', '1010 Pine St', '3456789012'),
       ('user14@example.com', 'Mia', 'Garcia', '1993-11-15', '1111 Oak St', '4567890123'),
       ('user15@example.com', 'William', 'Lopez', '1987-09-03', '1212 Elm St', '5678901234');
