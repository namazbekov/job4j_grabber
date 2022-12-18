package ru.job4j.grabber;

import java.time.*;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parses) {
        ZonedDateTime time = ZonedDateTime.parse(parses);
        return time.toLocalDateTime();
    }

    public static void main(String[] args) {
        HabrCareerDateTimeParser habrCareerDateTimeParser = new HabrCareerDateTimeParser();
        System.out.println(habrCareerDateTimeParser.parse("2022-12-12T15:59:57+03:00"));
    }
}
