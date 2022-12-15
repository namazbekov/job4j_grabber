package ru.job4j.grabber;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parses) throws ParseException {

        ZonedDateTime time = ZonedDateTime.parse(parses);
        return time.toLocalDateTime();
    }

    public static void main(String[] args) throws ParseException {
        HabrCareerDateTimeParser habrCareerDateTimeParser = new HabrCareerDateTimeParser();
        habrCareerDateTimeParser.parse("2022-12-12T15:59:57+03:00");
    }
}
