package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static List<Post> list;

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static DateTimeParser dateTimeParser = null;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) {
        for (int i = 1; i < 6; i++) {
            System.out.println("Page: " + i);
            try {
                String pageLink =
                        String.format("%s/vacancies/java_developer?page=" + i, SOURCE_LINK);
                Connection connection = Jsoup.connect(pageLink);
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element titleElement = row.select(".vacancy-card__title").first();
                    Element dateElement = row.select(".vacancy-card__date time").first();
                    Element linkElement = titleElement.child(0);
                    String vacancyName = titleElement.text();
                    String dateDrop = dateElement.attr("datetime");
                    HabrCareerParse habrCareerParse =
                            new HabrCareerParse(new HabrCareerDateTimeParser()
                            );
                    LocalDateTime newFormatDate;
                    newFormatDate = dateTimeParser.parse(dateDrop);
                    String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    System.out.printf("%s %s %s%n", newFormatDate, vacancyName, link);
                    try {
                        System.out.println(retrieveDescription(link));
                        System.out.println(habrCareerParse.list(
                                pageLink, vacancyName, link,
                                retrieveDescription(link),
                                newFormatDate));
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

    @Override
    public List<Post> list(
            String firstLink, String title,
            String link, String description,
            LocalDateTime time
    ) {
        Post post = new Post(title,
                link, description, time);
        list.add(post);
        return list;
    }
}