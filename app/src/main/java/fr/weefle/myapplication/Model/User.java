package fr.weefle.myapplication.Model;

import java.util.ArrayList;

// POJO class
public class User {
    private String password;
    private String email ;
    private ArrayList<Wallet> wallets;

    public User(String password, String email) {
        this.password = password;
        this.email = email;
        this.wallets = new ArrayList<>();
    }

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    public Wallet getWallet(Wallet wallet){
        return wallet;
    }

    public void addWallet(Wallet wallet) {
        wallets.add(wallet);
    }
}
