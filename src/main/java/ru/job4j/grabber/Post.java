package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {

    private int id;
    private String tittle;
    private String link;
    private String description;
    private LocalDateTime create;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreate() {
        return create;
    }

    public void setCreate(LocalDateTime create) {
        this.create = create;
    }

    public Post(int id, String tittle, String link, String description, LocalDateTime create) {
        this.id = id;
        this.tittle = tittle;
        this.link = link;
        this.description = description;
        this.create = create;
    }

    public Post(String tittle, String link, String description, LocalDateTime create) {
        this.tittle = tittle;
        this.link = link;
        this.description = description;
        this.create = create;
    }

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
