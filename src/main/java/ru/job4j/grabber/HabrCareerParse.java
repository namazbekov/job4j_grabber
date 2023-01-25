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

    private static final int LAST_PAGE = 5;

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK =
            "https://career.habr.com/vacancies/java_developer?page= ";

    private DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        habrCareerParse.list(PAGE_LINK);
    }

    private static String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        Document document;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        Element vacancyDescription = document.select(".style-ugc").first();
        return vacancyDescription.text();
    }

    @Override
    public List<Post> list(String firstLink) {
        List<Post> list = new ArrayList<>();
        for (int i = 1; i <= LAST_PAGE; i++) {
            try {
                String pageLink1 =
                        String.format("%s%d", firstLink, i);
                String pageLink = String.format(pageLink1);
                Connection connection = Jsoup.connect(pageLink);
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element titleElement = row.select(".vacancy-card__title").first();
                    Element dateElement = row.select(".vacancy-card__date time").first();
                    Element linkElement = titleElement.child(0);
                    String vacancyName = titleElement.text();
                    String dateDrop = dateElement.attr("datetime");
                    LocalDateTime newFormatDate;
                    newFormatDate = dateTimeParser.parse(dateDrop);
                    String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    String description = retrieveDescription(link);
                    Post post = new Post(vacancyName, link, description, newFormatDate);
                    list.add(post);
                });
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
        }
        return list;
    }
}