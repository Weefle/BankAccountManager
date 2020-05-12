package fr.weefle.myapplication.Model;

import java.util.ArrayList;

// POJO class
public class User {
    private int id;
    private String userName;
    private String password;
    private String email ;
    private ArrayList<Wallet> wallets;

    public User(int id, String userName, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.wallets = new ArrayList<>();
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
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
