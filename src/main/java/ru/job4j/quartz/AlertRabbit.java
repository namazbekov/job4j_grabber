package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            JobDetail job = newJob(Rabbit.class).usingJobData(data).build();
            Properties properties = loadProperties("rabbit.properties");
                try (Connection cn = getConnection(properties)) {
                    job.getJobDataMap().put("connection", cn);

            int second = Integer.parseInt(properties.getProperty("rabbit.interval"));
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(second)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        } catch (SchedulerException | InterruptedException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            try {
                if (connection.isClosed()) {
                    System.out.println("Disconnection!!!!!!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into rabbit(create_date) values(?)")) {
                statement.setString(1, String.valueOf(System.currentTimeMillis()));
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public static Connection getConnection(Properties properties) throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        );
    }
}