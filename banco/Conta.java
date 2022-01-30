package banco;

public class Conta {
    private String nome;
    private String CPF;
    private double saldo;

    public Conta(String nome, String CPF, double saldo) {
        this.nome = nome;
        this.CPF = CPF;
        this.saldo = saldo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getNome() {
        return nome;
    }

    public String getCPF() {
        return CPF;
    }
}
