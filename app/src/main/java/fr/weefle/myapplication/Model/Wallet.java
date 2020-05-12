package fr.weefle.myapplication.Model;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Wallet {

    private String name;
    private Double balance;
    private ArrayList<Transaction> transactions;

    public Wallet(String name, Double balance) {
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }


    public String getName() {
        return name;
    }

    public Double getBalance() {
        return balance;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }


    @Override
    public String toString() {
        return "Wallet{" +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }
}
