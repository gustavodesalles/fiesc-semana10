package io;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Ex7 {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        InputStream fis = null;
        char myChar = 0;

        do {
            try {
                System.out.println("Digite o nome do arquivo:");
                String nomeArquivo = input.nextLine();

                fis = new FileInputStream("./src/io/"+nomeArquivo);
            } catch (FileNotFoundException e) {
                System.out.println("Arquivo não encontrado; tente novamente.");
            }
        } while (fis == null);

        do {
            try {
                System.out.println("Digite um caractere:");
                myChar = input.findInLine(".").charAt(0);
                input.close();
            } catch (InputMismatchException i) {
                System.out.println("Favor digitar somente um caractere.");
            }
        } while (myChar == 0);

        InputStreamReader isr = new InputStreamReader(fis);

        int dados = isr.read();
        int instanceOfChar = 0;

        while(dados != -1) {
            char letra = (char) dados;
            dados = isr.read();
            if (letra == myChar) instanceOfChar++;
        }

        System.out.println(instanceOfChar);
    }
}
