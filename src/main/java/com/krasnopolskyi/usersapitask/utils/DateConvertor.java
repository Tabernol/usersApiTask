package com.krasnopolskyi.usersapitask.utils;

import com.krasnopolskyi.usersapitask.exception.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class DateConvertor {

    public static LocalDate convertDate(String date) throws ValidationException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            if (date == null) {
                return null;
            }
            // Parse the input date string into a LocalDate
            return LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            throw new ValidationException("Date " + date + " is invalid, you should use pattern 'dd-MM-yyyy'", ex);
        }
    }
}
