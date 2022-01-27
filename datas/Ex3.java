package datas;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Ex3 {

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.of(2022, 1, 1);
        int mes = localDate.getMonthValue();

        for (int i = mes; i <= 12; i++) {
            System.out.println(localDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.SATURDAY)));
            localDate = localDate.plusMonths(1);
        }
    }
}
