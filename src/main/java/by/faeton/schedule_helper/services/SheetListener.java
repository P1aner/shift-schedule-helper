package by.faeton.schedule_helper.services;

import by.faeton.schedule_helper.config.BotConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SheetListener {

    private static final String SPREADSHEETS = "https://sheets.googleapis.com/v4/spreadsheets/";

    private final BotConfig botConfig;
    private final RestClient restClient;

    private String getSheetJSON(String sheetListName, String fields) {
        String url = String.format("%s%s/values/%s%s?key=%s",
            SPREADSHEETS,
            botConfig.sheetId(),
            sheetListName,
            fields.isEmpty() ? "" : "!" + fields,
            botConfig.apiKey());
        return restClient
            .get()
            .uri(url)
            .retrieve()
            .body(String.class);
    }

    public Optional<List<List<String>>> getSheetList(String sheetListName, String fields) {
        String sheetJSON = getSheetJSON(sheetListName, fields);
        return convertJSONToList(sheetJSON);
    }

    private Optional<List<List<String>>> convertJSONToList(String text) {
        List<List<String>> values;
        try {
            Map result = new ObjectMapper().readValue(text, HashMap.class);
            values = (List<List<String>>) result.get("values");
        } catch (JsonProcessingException e) {
            values = null;
        }
        return Optional.ofNullable(values);
    }
}