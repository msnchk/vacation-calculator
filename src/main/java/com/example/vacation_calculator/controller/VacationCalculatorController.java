package com.example.vacation_calculator.controller;

import com.example.vacation_calculator.service.VacationCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/calculate")
public class VacationCalculatorController {

    private final VacationCalculatorService vacationCalculatorService;

    @Autowired
    public VacationCalculatorController(VacationCalculatorService vacationCalculatorService) {
        this.vacationCalculatorService = vacationCalculatorService;
    }

    @GetMapping
    public String calculateVacationPay(@RequestParam double averageSalary, @RequestParam int vacationDays) {
        if (averageSalary <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше 0.");
        }
        if (vacationDays <= 0) {
            throw new IllegalArgumentException("Количество дней отпуска должно быть больше 0.");
        }

        double vacationPay = vacationCalculatorService.calculateVacationPay(averageSalary, vacationDays);
        return String.format("Сумма отпускных составит %.2f рублей.", vacationPay);
    }

    @GetMapping("/withHolidays")
    public String calculateVacationPayWithHolidays(@RequestParam double averageSalary,
                                                   @RequestParam int vacationDays,
                                                   @RequestParam String startDate) {
        if (averageSalary <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше 0.");
        }
        if (vacationDays <= 0) {
            throw new IllegalArgumentException("Количество дней отпуска должно быть больше 0.");
        }

        try {
            LocalDate start = LocalDate.parse(startDate);
            return vacationCalculatorService.calculateVacationPayWithHolidays(averageSalary, start, vacationDays);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты: " + startDate);
        }
    }
}
