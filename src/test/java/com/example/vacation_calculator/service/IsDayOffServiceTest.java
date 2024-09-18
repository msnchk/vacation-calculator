package com.example.vacation_calculator.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class IsDayOffServiceTest {

    private MockRestServiceServer mockServer;

    @InjectMocks
    private IsDayOffService isDayOffService;

    @Autowired
    public void setUp(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        this.mockServer = MockRestServiceServer.bindTo(restTemplate).build();

        isDayOffService = new IsDayOffService(restTemplate);
    }

    @Test
    public void testIsWorkingDay() {
        LocalDate date = LocalDate.of(2023, 9, 1);
        String formattedDate = date.toString();

        mockServer.expect(requestTo("https://isdayoff.ru/" + formattedDate))
                .andRespond(withSuccess("0", MediaType.TEXT_PLAIN));

        boolean result = isDayOffService.isWorkingDay(date);
        assertTrue(result);

        mockServer.verify();
    }

    @Test
    public void testIsNotWorkingDay() {
        LocalDate date = LocalDate.of(2023, 9, 2);
        String formattedDate = date.toString();

        mockServer.expect(requestTo("https://isdayoff.ru/" + formattedDate))
                .andRespond(withSuccess("1", MediaType.TEXT_PLAIN));

        boolean result = isDayOffService.isWorkingDay(date);
        assertFalse(result);

        mockServer.verify();
    }

    @Test
    public void testIsWorkingDayApiError() {
        LocalDate date = LocalDate.of(2023, 9, 1);
        String formattedDate = date.toString();

        mockServer.expect(requestTo("https://isdayoff.ru/" + formattedDate))
                .andRespond(withServerError());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            isDayOffService.isWorkingDay(date);
        });

        String expectedMessage = "Ошибка при запросе к API для даты " + formattedDate;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        mockServer.verify();
    }

}
