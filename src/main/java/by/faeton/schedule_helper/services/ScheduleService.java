package by.faeton.schedule_helper.services;

import by.faeton.schedule_helper.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private static final DateTimeFormatter DEFAULT_PATTERN = DateTimeFormatter.ofPattern("EEEE d MMM yyyy 'г.'", new Locale("ru"));
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final String URL = "https://koronatech.by/shift_java/schedule";
    private static final String START = "Неделя 1";
    private static final String END = "Выпускной; ";

    private final RestClient restClient;

    @Cacheable("scheduleTask")
    public Task getNextTask() {
        List<Task> tasks = getTasks();
        return tasks.stream()
            .filter(task -> task.getDate().isAfter(LocalDateTime.now()))
            .findFirst()
            .orElseThrow();
    }

    @Cacheable("scheduleListOfTasks")
    public List<Task> getTasks() {
        String body = getBody(URL);

        List<List<String>> listOfSchedule = getListOfSchedule(body);

        List<Task> tasks = new ArrayList<>();

        LocalDate previousDate = null;
        for (List<String> strings : listOfSchedule) {
            if (strings.size() == 4) {
                previousDate = LocalDate.parse(strings.get(0), DEFAULT_PATTERN);
                String startTaskTimeString = strings.get(1).split(" - ")[0].trim();
                try {
                    LocalTime startTaskTime = LocalTime.parse(startTaskTimeString, DEFAULT_TIME_FORMATTER);
                    LocalDateTime taskDateTime = LocalDateTime.of(previousDate, startTaskTime);
                    tasks.add(new Task(taskDateTime, strings.get(2), strings.get(3)));
                } catch (Exception e) {
                    LocalDateTime taskDateTime = LocalDateTime.of(previousDate, LocalTime.of(12, 0));
                    tasks.add(new Task(taskDateTime, strings.get(2), strings.get(3)));
                }
            } else if (strings.size() == 3) {
                String startTaskTimeString = strings.get(0).split(" - ")[0].trim();
                LocalTime startTaskTime = LocalTime.parse(startTaskTimeString, DEFAULT_TIME_FORMATTER);
                LocalDateTime taskDateTime = LocalDateTime.of(previousDate, startTaskTime);
                tasks.add(new Task(taskDateTime, strings.get(1), strings.get(2)));
            }
        }
        return tasks;
    }

    private String getBody(String url) {
        return restClient
            .get()
            .uri(url)
            .retrieve()
            .body(String.class);
    }

    private List<List<String>> getListOfSchedule(String body) {
        return trimString(body, START, END)
            .replace("&#039;", "'")
            .replace('\u00A0', ' ')
            .lines()
            .filter(line -> !line.contains("Неделя "))
            .map(line -> Arrays.stream(line.split(";"))
                .map(String::trim)
                .toList())
            .toList();
    }

    private String trimString(String original, String start, String end) {
        int startIndex = original.indexOf(start);
        int endIndex = original.indexOf(end);
        if (startIndex != -1 && endIndex != -1 && endIndex >= startIndex) {
            return original.substring(startIndex, endIndex + end.length());
        }
        return "";
    }

}
