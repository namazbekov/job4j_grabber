package ru.job4j.grabber;

import java.text.ParseException;
import java.time.LocalDateTime;

public interface DateTimeParser {
    LocalDateTime parse(String parses) throws ParseException;
}
