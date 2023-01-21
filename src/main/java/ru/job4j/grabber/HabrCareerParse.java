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

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = "vacancies/java_developer?page= ";

    private static DateTimeParser dateTimeParser = null;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        System.out.println(habrCareerParse.list(PAGE_LINK));
    }

    private static String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element vacancyDescription = document.select(".style-ugc").first();
        return String.format("%s %s%n", "Описание вакансии :", vacancyDescription.text());
    }

    @Override
    public List<Post> list(String PAGE_LINK) {
        List<Post> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            System.out.println("Page: " + i);
            try {
                String pageLink1 =
                        String.format("%s/"+ PAGE_LINK + i, SOURCE_LINK);
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
                    System.out.printf("%s %s %s%n", newFormatDate, vacancyName, link);
                    try {
                        String description = retrieveDescription(link);
                        System.out.println(description);
                        Post post = new Post(vacancyName, link, description, newFormatDate);
                        list.add(post);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}