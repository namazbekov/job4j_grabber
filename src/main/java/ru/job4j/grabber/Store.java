package ru.job4j.grabber;

import java.util.List;

public interface Store extends AutoCloseable {

    boolean save(Post post);

    List<Post> getAll();

    Post findById(int id);
}
