package org.example.apitiendaaa;

import org.example.apitiendaaa.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilTests {


    @Test
    public void testFormat() {
        String dateString = "01/01/2021";

        LocalDate actualLocalDate = DateUtil.format(dateString);
        LocalDate expectedLocalDate = LocalDate.of(2021, 1, 1);
        assertEquals(expectedLocalDate, actualLocalDate);



    }

    @Test
    public void testGetDaysBetweenDates() {
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 1, 10);

        int days = DateUtil.getDaysBetweenDates(startDate, endDate);
        assertEquals(9, days);
        days = DateUtil.getDaysBetweenDates(endDate, startDate);
        assertEquals(9, days);
        days = DateUtil.getDaysBetweenDates(startDate, startDate);
        assertEquals(0, days);

    }

}
