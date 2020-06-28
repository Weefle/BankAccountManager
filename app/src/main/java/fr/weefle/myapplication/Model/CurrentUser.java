package fr.weefle.myapplication.Model;

public class CurrentUser {

    public static User user;

    public static User getCurrentUser(){
        return user;
    }

    public static void setCurrentUser(User use){
        user = use;
    }

}
