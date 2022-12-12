package ru.job4j.grabber;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parses) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'Т'HH:mm:ss");
        return LocalDateTime.parse(parses, formatter);
    }
}
