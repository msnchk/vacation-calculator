package com.example.vacation_calculator.controller;

import com.example.vacation_calculator.service.VacationCalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(VacationCalculatorController.class)
public class VacationCalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacationCalculatorService vacationCalculatorService;

    @Test
    public void testCalculateVacationPay() throws Exception {
        double averageSalary = 63000;
        int vacationDays = 10;
        double expectedVacationPay = Math.round((averageSalary / 29.3) * vacationDays * 100.0) / 100.0;

        String expectedResponse = String.format("Сумма отпускных составит %.2f рублей.", expectedVacationPay);

        when(vacationCalculatorService.calculateVacationPay(averageSalary, vacationDays)).thenReturn(expectedVacationPay);

        mockMvc.perform(get("/calculate")
                        .param("averageSalary", String.valueOf(averageSalary))
                        .param("vacationDays", String.valueOf(vacationDays)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testCalculateVacationPayWithHolidays() throws Exception {
        double averageSalary = 63000;
        int vacationDays = 10;
        String startDate = "2023-09-01";
        int holidaysCount = 4;
        double expectedVacationPay = Math.round((averageSalary / 29.3) * vacationDays * 100.0) / 100.0;

        String expectedResponse = String.format("Сумма отпускных составит %.2f рублей. На период отпуска выпадает %d выходных дня(дней).",
                expectedVacationPay, holidaysCount);

        when(vacationCalculatorService.calculateVacationPayWithHolidays(averageSalary, LocalDate.parse(startDate), vacationDays))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/calculate/withHolidays")
                        .param("averageSalary", String.valueOf(averageSalary))
                        .param("vacationDays", String.valueOf(vacationDays))
                        .param("startDate", startDate))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}
