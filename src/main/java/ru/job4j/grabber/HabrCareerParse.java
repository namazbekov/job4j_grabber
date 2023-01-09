package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.time.LocalDateTime;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    public static void main(String[] args) {
        for (int i = 1; i < 6; i++) {
            System.out.println("Page: " + i);
            try {
                PAGE_LINK = String.format("%s/vacancies/java_developer?page=" + i, SOURCE_LINK);
                Connection connection = Jsoup.connect(PAGE_LINK);
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element titleElement = row.select(".vacancy-card__title").first();
                    Element dateElement = row.select(".vacancy-card__date time").first();
                    Element linkElement = titleElement.child(0);
                    String vacancyName = titleElement.text();
                    String dateDrop = dateElement.attr("datetime");
                    HabrCareerDateTimeParser habrCareerDateTimeParser = new HabrCareerDateTimeParser();
                    LocalDateTime newFormatDate;
                    newFormatDate = habrCareerDateTimeParser.parse(dateDrop);
                    String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    System.out.printf("%s %s %s%n", newFormatDate, vacancyName, link);
                    try {
                        System.out.println(retrieveDescription(link));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element vacancyDescription = document.select(".style-ugc").first();
        return String.format("%s %s%n", "Описание вакансии :", vacancyDescription.text());
    }
}