package by.faeton.schedule_helper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Task {
    private LocalDateTime date;
    private String name;
    private String teacher;
}
