package com.example.vacation_calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class VacationCalculatorServiceTest {

    @Mock
    private IsDayOffService isDayOffService;

    @InjectMocks
    private VacationCalculatorService vacationCalculatorService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testCalculateVacationPay() {
        double averageSalary = 63000;
        int vacationDays = 10;

        double expectedVacationPay = Math.round((averageSalary / 29.3) * vacationDays * 100.0) / 100.0;
        double actualVacationPay = vacationCalculatorService.calculateVacationPay(averageSalary, vacationDays);

        assertEquals(expectedVacationPay, actualVacationPay);
    }

    @Test
    public void testCalculateVacationPayWithHolidays() {
        double averageSalary = 63000;
        int vacationDays = 10;
        LocalDate startDate = LocalDate.of(2023, 9, 1);

        boolean[] workingDays = {true, false, false, true, true, true, true, true, false, false};

        for (int i = 0; i < vacationDays; i++) {
            when(isDayOffService.isWorkingDay(startDate.plusDays(i))).thenReturn(workingDays[i]);
        }

        double expectedVacationPay = Math.round((averageSalary / 29.3) * vacationDays * 100.0) / 100.0;
        int holidaysCount = 4;

        String expectedResponse = String.format("Сумма отпускных составит %.2f рублей. На период отпуска выпадает %d выходных дня(дней).",
                expectedVacationPay, holidaysCount);

        String actualResponse = vacationCalculatorService.calculateVacationPayWithHolidays(averageSalary, startDate, vacationDays);

        assertEquals(expectedResponse, actualResponse);
    }

}
