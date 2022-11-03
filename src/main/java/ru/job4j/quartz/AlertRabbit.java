package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            Integer second = filter("data/rabbit.properties");
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
        String line = null;
        int second = 0;
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) {
                    throw new IllegalArgumentException("wrong string, string is empty");
                }
                if (!line.contains(".")) {
                    throw new IllegalArgumentException("wrong string, string does not contain a dot");
                }
                if (!line.contains("=")) {
                    throw new IllegalArgumentException("wrong string, string does not contain a equals");
                }
                if (line.startsWith(".")) {
                    throw new IllegalArgumentException("wrong string, string doesn't start with a dot");
                }
                String[] array = line.split("=");
                second = Integer.parseInt(array[array.length - 1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return second;
    }
}