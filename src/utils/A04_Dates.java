package src.utils;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

public class A04_Dates {
    public static void main(String[] args) {
        System.out.println("Date and Time in Java");

        {
            System.out.println("\nUsing java.time.LocalDate");
            LocalDate currentDate = LocalDate.now();
            System.out.println("Year: " + currentDate.getYear());
            System.out.println("Month: " + currentDate.getMonth());
            System.out.println("Day: " + currentDate.getDayOfMonth());
            System.out.println("Day of Week: " + currentDate.getDayOfWeek());
            System.out.println("Day of Year: " + currentDate.getDayOfYear());
            System.out.println("Leap Year: " + currentDate.isLeapYear());
            System.out.println("Length of Month: " + currentDate.lengthOfMonth());
            System.out.println("Length of Year: " + currentDate.lengthOfYear());
            System.out.println("Date: " + currentDate);

            System.out.println("Maximum Date: " + LocalDate.MAX);
            System.out.println("Minimum Date: " + LocalDate.MIN);
            System.out.println("Difference in Days between Max and Min: " + currentDate.until(LocalDate.MAX).getDays());
        }

        {
            System.out.println("\nUsing java.time.LocalTime");
            LocalTime time = LocalTime.of(23, 32, 12, 123456789);
            LocalTime timeNow = LocalTime.now();
            System.out.println("Time: " + time);
            System.out.println("Time Now: " + timeNow);
            System.out.println("Hour: " + timeNow.getHour());
            System.out.println("Minute: " + timeNow.getMinute());
            System.out.println("Second: " + timeNow.getSecond());
            System.out.println("Nano: " + timeNow.getNano());
            System.out.println("CLOCK_HOUR_OF_AMPM: " + timeNow.get(ChronoField.CLOCK_HOUR_OF_AMPM));
            System.out.println("Time in 2 hours: " + timeNow.plusHours(2));
        }

        {
            LocalDate date = LocalDate.of(2023, 10, 5);
            LocalTime time = LocalTime.of(23, 32, 12, 123456789);
            System.out.println("\nUsing java.time.LocalDateTime");
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            LocalDateTime dateTimeNow = LocalDateTime.now();
            System.out.println("Date and Time: " + dateTime);
            System.out.println("Date and Time Now: " + dateTimeNow);

            LocalDate currentDate = LocalDate.now();
            LocalTime timeNow = LocalTime.now();

            LocalDate parsedDate = LocalDate.parse("2023-10-05");
            LocalTime parsedTime = LocalTime.parse("23:32:12");
            System.out.println("Parsed Date: " + parsedDate);
            System.out.println("Parsed Time: " + parsedTime);
            LocalDateTime currentDateAtTime = LocalDateTime.of(currentDate, timeNow);
            LocalDateTime parsedDateTime = LocalDateTime.parse("2023-10-05T23:32:12");
            LocalDateTime parsedDateAtTime = parsedDate.atTime(parsedTime);
            LocalDateTime parsedTimeAtDate = parsedTime.atDate(parsedDate);
            System.out.println("Current Date at Time: " + currentDateAtTime);
            System.out.println("Parsed Date and Time: " + parsedDateTime);
            System.out.println("Parsed Date at Time: " + parsedDateAtTime);
            System.out.println("Parsed Time at Date: " + parsedTimeAtDate);
        }

        {
            System.out.println("\nUsing java.time.OffsetDateTime");
            LocalDate date = LocalDate.of(2023, 10, 5);
            LocalTime time = LocalTime.of(23, 32, 12, 123456789);
            OffsetDateTime offsetDateTime = OffsetDateTime.of(date, time, OffsetDateTime.now().getOffset());
            OffsetDateTime offsetDateTimeNow = OffsetDateTime.now();
            OffsetDateTime offsetDateTimeParsed = OffsetDateTime.parse("2023-10-05T23:32:12+00:00");
            OffsetDateTime offsetDateTimeMinusFiveDays = offsetDateTimeNow.minusDays(5);
            System.out.println("Offset Date and Time: " + offsetDateTime);
            System.out.println("Offset Date and Time Now: " + offsetDateTimeNow);
            System.out.println("Parsed Offset Date and Time: " + offsetDateTimeParsed);
            System.out.println("Offset Date and Time Minus 5 Days: " + offsetDateTimeMinusFiveDays);
        }

        {
            System.out.println("\nUsing java.time.Instant");
            Instant instant = Instant.now();
            Instant instantParsed = Instant.parse("2023-10-05T23:32:12Z");
            Instant instantPlusFiveDays = instant.plus(5, ChronoUnit.DAYS);
            System.out.println("Instant: " + instant);
            System.out.println("Parsed Instant: " + instantParsed);
            System.out.println("Instant Plus 5 Days: " + instantPlusFiveDays);
            System.out.println("Instant in Milliseconds: " + instant.toEpochMilli());
            System.out.println("Instant in Seconds: " + instant.getEpochSecond());
            System.out.println("Instant in Nanoseconds: " + instant.getLong(ChronoField.NANO_OF_SECOND));
        }

        {
            System.out.println("\nUsing java.time.Period");
            Period period = Period.of(1, 2, 3);
            Period periodParsed = Period.parse("P1Y2M3D");
            Period periodPlusFiveDays = period.plusDays(5);
            System.out.println("Period: " + period);
            System.out.println("Parsed Period: " + periodParsed);
            System.out.println("Period Plus 5 Days: " + periodPlusFiveDays);
        }

        {
            System.out.println("\nUsing java.time.Duration");
            Duration duration = Duration.ofHours(5);
            Duration durationParsed = Duration.parse("PT5H");
            Duration durationPlusFiveMinutes = duration.plusMinutes(5);
            System.out.println("Duration: " + duration);
            System.out.println("Parsed Duration: " + durationParsed);
            System.out.println("Duration Plus 5 Minutes: " + durationPlusFiveMinutes);
        }

        {
            System.out.println("\nUsing java.time.CronoUnit");
            LocalDate date = LocalDate.of(2023, 10, 5);
            LocalTime time = LocalTime.of(23, 32, 12, 123456789);
            LocalDate currentDate = LocalDate.now();
            LocalTime timeNow = LocalTime.now();
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            LocalDateTime dateTimeNow = LocalDateTime.now();
            System.out.println("Days between " + date + " and " + currentDate + ": " + ChronoUnit.DAYS.between(date, currentDate));
            System.out.println("Hours between " + time + " and " + timeNow + ": " + ChronoUnit.HOURS.between(time, timeNow));
            System.out.println("Minutes between " + dateTime + " and " + dateTimeNow + ": " + ChronoUnit.MINUTES.between(dateTime, dateTimeNow));
        }

        {
            System.out.println("\nUsing java.time.TemporalAdjusters");
            LocalDate date = LocalDate.now();
            System.out.println("Date now: " + date);
            System.out.println("Day of the week: " + date.getDayOfWeek());

            date = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
            System.out.println("First day of the month: " + date);
            System.out.println("Day of the week: " + date.getDayOfWeek());

            date = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
            System.out.println("Last day of the month: " + date);
            System.out.println("Day of the week: " + date.getDayOfWeek());

            date = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
            System.out.println("First day of the next month: " + date);
            System.out.println("Day of the week: " + date.getDayOfWeek());

            date = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
            System.out.println("First day of the year: " + date);
            System.out.println("Day of the week: " + date.getDayOfWeek());
        }

        {
            System.out.println("\nUsing java.time.TemporalAdjuster");
            LocalDate date = LocalDate.now().withDayOfMonth(16);
            System.out.println("Date now: " + date);
            System.out.println("Day of the week: " + date.getDayOfWeek());

            date = date.with(CustomTemporalAdjuster.getNextWorkingDay());
            System.out.println("Next working day: " + date);
            System.out.println("Day of the week: " + date.getDayOfWeek());
        }

        {
            System.out.println("\nUsing java.time.ZonedDateTime");

            Map<String, String> shortIds = ZoneId.SHORT_IDS;
            System.out.println("Short IDs: " + shortIds);
            System.out.println("System Zone ID: " + ZoneId.systemDefault());

            LocalDateTime now = LocalDateTime.now();
            ZoneId tokyoZoneId = ZoneId.of("Asia/Tokyo");
            ZonedDateTime tokyoZonedDateTime = ZonedDateTime.of(now, tokyoZoneId);
            ZonedDateTime tokyoZonedDateTime2 = now.atZone(tokyoZoneId);
            System.out.println("Local Date and Time: " + now);
            System.out.println("Tokyo Date and Time: " + tokyoZonedDateTime);
            System.out.println("Tokyo Date and Time: " + tokyoZonedDateTime2);

            System.out.println("Max ZonedDateTime: " + ZoneOffset.MAX);
            System.out.println("Min ZonedDateTime: " + ZoneOffset.MIN);
        }

    }
}

class CustomTemporalAdjuster {
    public static TemporalAdjuster getNextWorkingDay() {
        return temporal -> {
            LocalDate date = LocalDate.from(temporal);
            return switch (date.getDayOfWeek()) {
                case FRIDAY -> date.plusDays(3);
                case SATURDAY -> date.plusDays(2);
                default -> date.plusDays(1);
            };
        };
    }
}