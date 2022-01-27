package datas;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Ex4 {
    static Scanner input = new Scanner(System.in);
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        LocalDate date = null;

        do {
            try {
                System.out.println("Digite uma data (dd/MM/yyyy):");
                String dateStr = input.nextLine();
                date = LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException d) {
                System.out.println("Data inválida.");
            }
        } while (date == null);


        if (date.getDayOfWeek() == DayOfWeek.FRIDAY && date.getDayOfMonth() == 13) {
            System.out.println("Boo!");
        }
    }
}
