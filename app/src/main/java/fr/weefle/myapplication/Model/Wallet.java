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

    public Wallet(){}



    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
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


}
