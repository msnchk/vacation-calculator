package com.example.vacation_calculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class IsDayOffService {

    private static final String BASE_URL = "https://isdayoff.ru/";

    private final RestTemplate restTemplate;

    @Autowired
    public IsDayOffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isWorkingDay(LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String url = BASE_URL + formattedDate;

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("Ошибка при обращении к API. Код состояния: " + responseEntity.getStatusCode());
            }

            String response = responseEntity.getBody();

            if (response == null) {
                throw new IllegalStateException("Ответ от сервиса пуст.");
            }

            return switch (response) {
                case "0" -> true;  // Рабочий день
                case "1" -> false;  // Выходной день
                case "100" -> throw new IllegalArgumentException("Ошибка в дате. Проверьте корректность даты: " + formattedDate);
                case "101" -> throw new IllegalStateException("Данные не найдены для даты: " + formattedDate);
                case "199" -> throw new IllegalStateException("Ошибка сервиса.");
                default -> throw new IllegalStateException("Неожиданный ответ от API: " + response);
            };
        } catch (RestClientException e) {
            throw new IllegalStateException("Ошибка при запросе к API для даты " + formattedDate + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Произошла непредвиденная ошибка при запросе к API для даты " + formattedDate + ": " + e.getMessage(), e);
        }
    }
}

