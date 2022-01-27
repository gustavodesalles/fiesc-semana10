package datas;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Ex1 {
    static Scanner input = new Scanner(System.in);
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public static void main(String[] args) {
        LocalDate agora = LocalDate.now(), dataNascimento;
        Period diferenca = null;

        do {
            try {
                System.out.println("Digite a sua data de nascimento (dd/MM/yyyy):");
                String dataNasc = input.nextLine();

                dataNascimento = LocalDate.parse(dataNasc, formatter);
                diferenca = Period.between(dataNascimento, agora);
            } catch (DateTimeParseException d) {
                System.out.println("Data inválida.");
            }
        } while (diferenca == null);

            if (diferenca.getYears() < 18) {
                System.out.println("Você não tem idade suficiente para acessar o sistema.");
            } else {
                System.out.println("Acesso concedido.");
            }

    }
}
