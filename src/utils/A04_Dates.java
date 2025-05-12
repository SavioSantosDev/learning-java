package src.utils;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class A04_Dates {
    public static void main(String[] args) {
        System.out.println("Date and Time in Java");

        System.out.println("\nUsing java.time.LocalDate");
        LocalDate date = LocalDate.of(2023, 10, 5);
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

        System.out.println("\nUsing java.time.LocalDateTime");
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        LocalDateTime dateTimeNow = LocalDateTime.now();
        System.out.println("Date and Time: " + dateTime);
        System.out.println("Date and Time Now: " + dateTimeNow);

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

        System.out.println("\nUsing java.time.OffsetDateTime");
        OffsetDateTime offsetDateTime = OffsetDateTime.of(date, time, OffsetDateTime.now().getOffset());
        OffsetDateTime offsetDateTimeNow = OffsetDateTime.now();
        OffsetDateTime offsetDateTimeParsed = OffsetDateTime.parse("2023-10-05T23:32:12+00:00");
        OffsetDateTime offsetDateTimeMinusFiveDays = offsetDateTimeNow.minusDays(5);
        System.out.println("Offset Date and Time: " + offsetDateTime);
        System.out.println("Offset Date and Time Now: " + offsetDateTimeNow);
        System.out.println("Parsed Offset Date and Time: " + offsetDateTimeParsed);
        System.out.println("Offset Date and Time Minus 5 Days: " + offsetDateTimeMinusFiveDays);

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

        System.out.println("\nUsing java.time.Period");
        Period period = Period.of(1, 2, 3);
        Period periodParsed = Period.parse("P1Y2M3D");
        Period periodPlusFiveDays = period.plusDays(5);
        System.out.println("Period: " + period);
        System.out.println("Parsed Period: " + periodParsed);
        System.out.println("Period Plus 5 Days: " + periodPlusFiveDays);

        System.out.println("\nUsing java.time.Duration");
        Duration duration = Duration.ofHours(5);
        Duration durationParsed = Duration.parse("PT5H");
        Duration durationPlusFiveMinutes = duration.plusMinutes(5);
        System.out.println("Duration: " + duration);
        System.out.println("Parsed Duration: " + durationParsed);
        System.out.println("Duration Plus 5 Minutes: " + durationPlusFiveMinutes);

        System.out.println("\nUsing java.time.CronoUnit");
        System.out.println("Days between " + date + " and " + currentDate + ": " + ChronoUnit.DAYS.between(date, currentDate));
        System.out.println("Hours between " + time + " and " + timeNow + ": " + ChronoUnit.HOURS.between(time, timeNow));
        System.out.println("Minutes between " + dateTime + " and " + dateTimeNow + ": " + ChronoUnit.MINUTES.between(dateTime, dateTimeNow));


    }
}
