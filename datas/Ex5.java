package datas;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Scanner;

public class Ex5 {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        LocalDate anoEMes = null;

        do {
            try {
                System.out.println("Digite um ano e um mês (formato yyyy-MM):");
                String anoEMesStr = input.nextLine();
                anoEMes = LocalDate.parse(anoEMesStr+"-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException d) {
                System.out.println("Data inválida.");
            }
        } while (anoEMes == null);

        LocalDate ultimoDiaDoMes = anoEMes.with(TemporalAdjusters.lastDayOfMonth());

        while (anoEMes.isBefore(ultimoDiaDoMes)) {
            System.out.println(anoEMes.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)));
            anoEMes = anoEMes.plusWeeks(1);
        }

    }
}
