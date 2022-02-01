package banco;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Conta> contas = new ArrayList<>();
    static AtomicBoolean ab = new AtomicBoolean(true);
//    static boolean lido = false;

    public static void carregar() throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("src/banco/contas.csv"), "UTF-8")) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();

                try (Scanner linhaScanner = new Scanner(linha)) {
                    linhaScanner.useLocale(Locale.US);
                    linhaScanner.useDelimiter(",");
                    String nome = linhaScanner.next();

                    String cpf = linhaScanner.next();

                    double saldo = linhaScanner.nextDouble();

                    Conta conta = new Conta(nome, cpf, saldo);
                    contas.add(conta);
                }
            }
        }
    }

    public static Conta obterConta(String cpf) {
        for (Conta c : contas) {
            if (cpf.equals(c.getCPF())) {
                return c;
            }
        }

        return null;
    }

    public static boolean verificarCPF(String cpf) { // adaptado do código disponível em: https://www.devmedia.com.br/validando-o-cpf-em-uma-aplicacao-java/22097
        if (cpf.length() != 11) return false;
        char dig10 = cpf.charAt(9);
        char dig11 = cpf.charAt(10);
        int num, soma, peso, resto;
        int zeroAscii = 48;
        char digv1, digv2;

        // calcular o primeiro digito verificador
        soma = 0;
        peso = 10;

        for (int i = 0; i < 9; i++) {
            num = (int) (cpf.charAt(i) - zeroAscii); // subtrair por 48 de acordo com a tabela ASCII
            soma += (num * peso);
            peso--;
        }

        resto = 11 - (soma % 11);
        if (resto == 10 || resto == 11) {
            digv1 = '0';
        } else digv1 = (char) (resto + zeroAscii);

        // calcular o segundo digito verificador
        soma = 0;
        peso = 11;
        for (int j = 0; j < 10; j++) {
            num = (int) (cpf.charAt(j) - zeroAscii);
            soma += (num * peso);
            peso--;
        }

        resto = 11 - (soma % 11);
        if (resto == 10 || resto == 11) {
            digv2 = '0';
        } else digv2 = (char) (resto + zeroAscii);

        return (digv1 == dig10 && digv2 == dig11);
    }

    public static void criarConta(boolean isLoading) {
        try (FileWriter fw = new FileWriter("src/banco/contas.csv", true);
             BufferedWriter hw = new BufferedWriter(new FileWriter("src/banco/historico.csv", true));
             BufferedWriter bw = new BufferedWriter(fw)) {

            scanner.nextLine();
            System.out.print("Digite o seu nome: ");
            String nome = scanner.nextLine();

            System.out.print("Digite o seu CPF: ");
            String CPF = scanner.nextLine();

            for (Conta c : contas) {
                while (CPF.equals(c.getCPF())) {
                    System.out.println("Já existe uma conta com este CPF.");
                    System.out.println("Tente novamente: ");
                    CPF = scanner.nextLine();
                }
            }

            while (verificarCPF(CPF) == false) {
                System.out.print("CPF inválido. Tente novamente: ");
                CPF = scanner.nextLine();
            }

            System.out.print("Digite o saldo: ");
            double saldo = scanner.nextDouble();
            if (saldo < 0) {
                saldo = 0;
            }

            Conta conta = new Conta(nome, CPF, saldo);
            contas.add(conta);

            if (!isLoading) {
                hw.write("Criada conta do CPF " + CPF + " " + LocalDateTime.now());
                hw.newLine();
                bw.write(conta.toString());
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reescrever() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/banco/contas.csv"))) {
            for (Conta c : contas) {
                bw.write(c.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String saque(Conta conta, double quantia, boolean isTransfer) {
        String res = null;
        try (FileWriter fw = new FileWriter("src/banco/contas.csv", true);
             BufferedWriter hw = new BufferedWriter(new FileWriter("src/banco/historico.csv", true));
             BufferedWriter bw = new BufferedWriter(fw)) {

            if (conta.getSaldo() - quantia >= 0 && quantia > 0) {
                conta.setSaldo(conta.getSaldo() - quantia);
                if (!isTransfer) { // caso o saque não seja parte de uma transferência, adiciona-se uma string ao extrato
                    res = "Saque R$ "+quantia+" "+ LocalDateTime.now();
                    hw.write(res);
                    hw.newLine();
                    System.out.println(res);
                    reescrever();
                }
            } else {
                System.out.println("Saldo insuficiente.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String deposito(Conta conta, double quantia, boolean isTransfer) {
        String res = null;

        try (BufferedWriter hw = new BufferedWriter(new FileWriter("src/banco/historico.csv", true))) {
            conta.setSaldo(conta.getSaldo() + quantia);
            if (!isTransfer) { // caso o depósito não seja parte de uma transferência, adiciona-se uma string ao extrato
                res = "Depósito R$ "+quantia+" "+ LocalDateTime.now();
                hw.write(res);
                hw.newLine();
                System.out.println(res);
                reescrever();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String transferencia(Conta origem, Conta destino, double quantia) {
        String res = null;

        try (BufferedWriter hw = new BufferedWriter(new FileWriter("src/banco/historico.csv", true))) {
            saque(origem, quantia, true);
            if (origem.getSaldo() - quantia >= 0 && destino != null) {
                deposito(destino, quantia, true);
                res = "Transferência R$ "+quantia+" Destino "+destino.getCPF()+" "+ LocalDateTime.now();
                hw.write(res);
                hw.newLine();
                System.out.println(res);
                reescrever();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (ab.get()) {
            carregar();
            ab.set(false);
        }
        String cpf;
        double quantia;
        Conta conta;

        System.out.println("\nSelecione uma opção:");
        System.out.println("1 - Criar conta");
        System.out.println("2 - Saque");
        System.out.println("3 - Depósito");
        System.out.println("4 - Transferência");
        System.out.println("Digite qualquer outra tecla para sair.");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                criarConta(false);
                main(null);
                break;
            case 2:
                scanner.nextLine();
                System.out.println("Digite o CPF do titular da conta: ");
                cpf = scanner.nextLine();
                conta = obterConta(cpf);
                if (conta == null) {
                    System.out.println("Conta não encontrada.");
                    main(null);
                    break;
                }
                System.out.println("Digite a quantia que deseja sacar: ");
                quantia = scanner.nextDouble();
                saque(conta, quantia, false);
                main(null);
                break;
            case 3:
                scanner.nextLine();
                System.out.println("Digite o CPF do titular da conta: ");
                cpf = scanner.nextLine();
                conta = obterConta(cpf);
                if (conta == null) {
                    System.out.println("Conta não encontrada.");
                    main(null);
                    break;
                }
                System.out.println("Digite a quantia que deseja depositar: ");
                quantia = scanner.nextDouble();
                if (quantia < 0) {
                    System.out.println("Quantia inválida.");
                    main(null);
                    break;
                }
                deposito(conta, quantia, false);
                main(null);
                break;
            case 4:
                scanner.nextLine();
                System.out.println("Digite o CPF do titular da conta remetente: ");
                String cpf1 = scanner.nextLine();
                Conta conta1 = obterConta(cpf1);
                if (conta1 == null) {
                    System.out.println("Conta não encontrada.");
                    main(null);
                    break;
                }

                System.out.println("Digite a quantia que deseja transferir: ");
                quantia = scanner.nextDouble();

                scanner.nextLine();
                System.out.println("Digite o CPF do titular da conta destinatário: ");
                String cpf2 = scanner.nextLine();
                Conta conta2 = obterConta(cpf2);
                if (conta2 == null) {
                    System.out.println("Conta não encontrada.");
                    main(null);
                    break;
                }

                transferencia(conta1, conta2, quantia);
                main(null);
                break;
            default:
                System.out.println("Até mais!");
        }
    }
}
