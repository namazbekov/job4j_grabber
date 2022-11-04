package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            Integer second = filter("rabbit.properties");
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(second)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
        }
    }

    public static Integer filter(String file) {
        int second;
        Properties properties = new Properties();
        try (InputStream in =
                     AlertRabbit.class.getClassLoader().getResourceAsStream(file)
        ) {
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        second = Integer.parseInt(properties.getProperty("rabbit.interval"));
        return second;
    }
}