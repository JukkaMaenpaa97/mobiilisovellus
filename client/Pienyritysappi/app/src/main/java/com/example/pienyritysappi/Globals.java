package com.example.pienyritysappi;

public class Globals {
    private static Globals instance;
    private static String api_key;

    private Globals(){
    }

    public static void setApi_key(String apikey){
        Globals.api_key = apikey;
    }

    public static String getApi_key(){
        return Globals.api_key;
    }

    public static synchronized Globals getInstance(){
        if (instance == null){
            instance = new Globals();
        }
        return instance;
    }
}
