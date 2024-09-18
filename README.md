# Vacation Calculator

Проект **Vacation Calculator** — это веб-сервис, позволяющий рассчитать сумму отпускных выплат. Он предоставляет два основных метода расчета:
1. **calculateVacationPay**: Рассчитывает отпускные по количеству заданных дней отпуска;
2. **calculateVacationPayWithHolidays**: Выполняет тот же расчет, но дополнительно выводит информацию о количестве выходных дней, на которые выпадает на период отпуска.<br>

## Технологии

- Java 11+
- Spring Boot
- Maven
- REST API
- JUnit 
- Mockito

## Требования

- **Java 11** или выше
- **Maven** 3.6+ для управления зависимостями и сборки проекта

## Установка

1. Клонируйте репозиторий:<br>
   ```bash 
   git clone https://github.com/msnchk/vacation-calculator.git
   
2. Перейдите в директорию проекта:
   ```bash 
   cd vacation-calculator

3. Соберите проект с помощью Maven:
   ```bash 
   mvn clean install

## Запуск
1. Запустите приложение:
   ```bash
   mvn spring-boot:run
2. Приложение будет доступно по адресу *http://localhost:8080*

## Использование
1. Рассчитать отпускные<br> 
•  URL: `/calculate`<br>
•  Метод: GET<br>
•  Параметры запроса: *averageSalary* (double) — средняя зарплата за 12 месяцев, *vacationDays* (int) — количество дней отпуска.<br>
•  Пример запроса: GET http://localhost:8080/calculate?averageSalary=60000&vacationDays=14
2. Рассчитать отпускные и вывести информацию о выходных днях<br>
      •  URL: `/calculate/withHolidays`<br>
      •  Метод: GET<br>
      •  Параметры запроса: *averageSalary* (double) — средняя зарплата за 12 месяцев, *vacationDays* (int) — количество дней отпуска,<br>
   *startDate* (String) — дата начала отпуска в формате yyyy-MM-dd.<br>
      •  Пример запроса: GET http://localhost:8080/calculate/withHolidays?averageSalary=60000&vacationDays=14&startDate=2024-09-18<br>

## Дополнительная информация
Расчет отпускных основан на следующей формуле: *(averageSalary / 29.3) * vacationDays*,
где 29.3 — это среднее количество дней в месяце, используемое для расчета отпускных.<br>
Для определения выходных дней используется внешний сервис IsDayOff. Приложение отправляет запрос к этому сервису для определения рабочих и выходных дней на заданный период отпуска.
