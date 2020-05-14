package fr.weefle.myapplication.Model;

import java.util.ArrayList;

// POJO class
public class User {
    private String email = "";
    private ArrayList<Wallet> wallets = null;

    public User(String email) {
        this.email = email;
        this.wallets = new ArrayList<>();
    }

    public User() {
    }



    public String getEmail() {
        return email;
    }

    public void setWallets(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    public void addWallet(Wallet wallet) {
        wallets.add(wallet);
    }
}
