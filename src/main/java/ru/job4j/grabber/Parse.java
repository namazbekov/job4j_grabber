package ru.job4j.grabber;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface Parse {

    List<Post> list(
            String firstLink, String title,
            String link, String description,
            LocalDateTime time
    ) throws IOException;
}
