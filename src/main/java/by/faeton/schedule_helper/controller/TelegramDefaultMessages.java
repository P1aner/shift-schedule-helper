package by.faeton.schedule_helper.controller.handlers;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TelegramDefaultMessages {

    public static final String GREETING = "Создан чтобы напоминать о занятиях.";
    public static final String NEXT_TASK_NOT_FOUND = "Следующее занятие не найдено.";
    public static final String HOUR_NOTICE = "Следующее занятие начнется примерно через час.";
    public static final String TEN_MINUTES_NOTICE = """
        Занятие начнется совсем скоро.
        Ссылка для подключения к конференции:
        https://us05web.zoom.us/j/2329227872?pwd=XrjFOYdXBv3TQT1ikpr2a4VlPQ6Zch.1""";

}
