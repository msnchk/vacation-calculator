package com.example.vacation_calculator.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VacationCalculatorService {

    private final IsDayOffService isDayOffService;

    public VacationCalculatorService(IsDayOffService isDayOffService) {
        this.isDayOffService = isDayOffService;
    }
    public double calculateVacationPay(double averageSalary, int vacationDays) {
        return Math.round((averageSalary / 29.3) * vacationDays * 100.0) / 100.0;
    }

    public String calculateVacationPayWithHolidays(double averageSalary, LocalDate startDate, int vacationDays) {
        int holidaysCount = 0;
        LocalDate currentDate = startDate;

        for (int i = 0; i < vacationDays; i++) {
            if (!isDayOffService.isWorkingDay(currentDate)) {
                holidaysCount++;
            }
            currentDate = currentDate.plusDays(1);
        }

        double vacationPay = calculateVacationPay(averageSalary, vacationDays);

        return String.format("Сумма отпускных составит %.2f рублей. На период отпуска выпадает %d выходных дня(дней).",
                vacationPay, holidaysCount);
    }
}
