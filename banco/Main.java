package banco;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Conta> contas = new ArrayList<>();
    static ArrayList<String> historico = new ArrayList<>();
//    static boolean lido = false;

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

    public static void criarConta() {
        System.out.print("Digite o seu nome: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o seu CPF: ");
        String CPF = scanner.nextLine();

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
        historico.add("Criada conta do CPF "+CPF);
        contas.add(conta);
    }

    public String saque(Conta conta, double quantia, boolean isTransfer) {
        String res = null;

        if (conta.getSaldo() - quantia >= 0) {
            conta.setSaldo(conta.getSaldo() - quantia);
            if (!isTransfer) { // caso o saque não seja parte de uma transferência, adiciona-se uma string ao extrato
                res = "Saque R$ "+quantia+" "+ LocalDateTime.now().toString();
                historico.add(res);
                System.out.println(res);
            }
        } else {
            System.out.println("Saldo insuficiente.");
        }

        return res;
    }

    public String deposito(Conta conta, double quantia, boolean isTransfer) {
        String res = null;

        conta.setSaldo(conta.getSaldo() + quantia);
        if (!isTransfer) { // caso o depósito não seja parte de uma transferência, adiciona-se uma string ao extrato
            res = "Depósito R$ "+quantia+" "+ LocalDateTime.now().toString();
            historico.add(res);
            System.out.println(res);
        }

        return res;
    }

    public String transferencia(Conta origem, Conta destino, double quantia) {
        String res = null;

        saque(origem, quantia, true);
        if (origem.getSaldo() - quantia >= 0 && destino != null) {
            deposito(destino, quantia, true);
            res = "Transferência R$ "+quantia+" Destino "+destino.getCPF()+" "+ LocalDateTime.now().toString();
            historico.add(res);
            System.out.println(res);
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println("\nSelecione uma opção:");
        System.out.println("1 - Criar conta");
        System.out.println("2 - Saque");
        System.out.println("3 - Depósito");
        System.out.println("4 - Transferência");
        System.out.println("Digite qualquer outra tecla para sair.");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                criarConta();
        }
    }
}
