package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    int id;
    String tittle;
    String link;
    String description;
    LocalDateTime create;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id + ", tittle='" + tittle + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", create=" + create
                + '}';
    }
}
