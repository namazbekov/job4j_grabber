package ru.job4j.grabber;

import ru.job4j.quartz.AlertRabbit;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        String url = cfg.getProperty("jdbc.url");
        String name = cfg.getProperty("jdbc.username");
        String password = cfg.getProperty("jdbc.password");
        try {
            cnn = DriverManager.getConnection(url, name, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean save(Post post) {
        boolean result = false;
        try (PreparedStatement statement =
                     cnn.prepareStatement("insert into post(title, link, description, created)"
                                     + " values(?, ?, ?, ?) ON CONFLICT (link) DO NOTHING;",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTittle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreate()));
            statement.execute();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = cnn.prepareStatement("select * from post")) {
            try (ResultSet set = preparedStatement.executeQuery()) {
                while (set.next()) {
                    list.add(new Post(set.getString("title"),
                            set.getString("link"),
                            set.getString("description"),
                            set.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement =
                     cnn.prepareStatement("select title, link, description, created "
                             + "from post where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    post = new Post(
                            set.getString("title"),
                            set.getString("link"),
                            set.getString("description"),
                            set.getTimestamp("created").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static Properties loadProperties(String file) {
        Properties properties = new Properties();
        try (InputStream in =
                     AlertRabbit.class.getClassLoader().getResourceAsStream(file)
        ) {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        return properties;
    }
}