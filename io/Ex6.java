package io;

import java.io.*;
import java.util.Scanner;

public class Ex6 {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        InputStream fis = null;

        do {
            try {
                System.out.println("Digite o nome do arquivo:");
                String nomeArquivo = input.nextLine();

                fis = new FileInputStream("./src/io/"+nomeArquivo);
            } catch (FileNotFoundException e) {
                System.out.println("Arquivo não encontrado; tente novamente.");
            }
        } while (fis == null);

        InputStreamReader isr = new InputStreamReader(fis);

        int dados = 0;

        dados = isr.read();

        double numBytes = 0;
        while(dados != -1) {
            dados = isr.read();
            numBytes++;
        }
        System.out.println("Tamanho do arquivo em bytes: "+numBytes);
        System.out.println("Tamanho do arquivo em kilobytes: "+numBytes/1024);
        System.out.println("Tamanho do arquivo em megabytes: "+(numBytes/1024)/1024);
    }
}
