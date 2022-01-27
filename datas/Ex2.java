package datas;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Ex2 {
    static Scanner input = new Scanner(System.in);
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        LocalDateTime embarque, desembarque;
        Duration diferenca = null;

        do {
            try {
                System.out.println("Digite a data e hora do embarque (dd/MM/yyyy HH:mm): ");
                String data1 = input.nextLine();
                embarque = LocalDateTime.parse(data1, formatter);

                System.out.println("Digite a data e hora do desembarque (dd/MM/yyyy HH:mm): ");
                String data2 = input.nextLine();
                desembarque = LocalDateTime.parse(data2, formatter);

                diferenca = Duration.between(embarque, desembarque);
            } catch (DateTimeParseException d) {
                System.out.println("Data inválida.");
            }
        } while (diferenca == null);

        System.out.printf(diferenca.toDaysPart() + " dias, " + diferenca.toHoursPart() + " horas, " + diferenca.toMinutesPart() + " minutos.");
    }
}
