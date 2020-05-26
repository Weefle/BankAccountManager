package fr.weefle.myapplication.Model;

import java.util.ArrayList;
import java.util.UUID;

// POJO class
public class User {
    private String uuid;
    private ArrayList<Wallet> wallets;

    public User(String uuid) {
        this.uuid = uuid;
        this.wallets = new ArrayList<>();
    }

    public User() {
    }

    public Wallet getWallet(String name){
        int i = 0;

        for(Wallet wallet : this.wallets){

            if(wallet.getName().equals(name)){
                break;
            }
            i++;
        }
        return this.wallets.get(i);
    }



    public String getUUID() {
        return uuid;
    }

    public void setWallets(ArrayList<Wallet> wallets) {
        this.wallets = wallets;
    }

    public void setUUID(String uuid) {
        this.uuid= uuid;
    }

    public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    public void addWallet(Wallet wallet) {
        wallets.add(wallet);
    }
}
