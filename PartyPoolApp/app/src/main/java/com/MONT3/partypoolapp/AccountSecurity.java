package com.MONT3.partypoolapp;

import android.graphics.Paint;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.List;

//  Singleton Class. Password Hashing Should Be Available Once
public final class AccountSecurity {

    private static volatile AccountSecurity singleton = null;

    private AccountSecurity() {

    }

    public static AccountSecurity InitialisePasswordHash() {

        if (singleton == null) {

            synchronized (AccountSecurity.class) {
                if (singleton== null) {
                    singleton = new AccountSecurity();
                }
            }
        }

        return singleton;
    }

    public String HashPassword (String stringIn) throws NoSuchAlgorithmException {

        //  MessageDigest Handles All Hashing Algorithms. Utilise This To Handle Passwords.
        MessageDigest hasher = MessageDigest.getInstance("SHA-256");
        byte[] hashStringToBytes = hasher.digest(stringIn.getBytes(StandardCharsets.UTF_8));

        //  StringBuilder Used For Performance. Faster Than String Concatanation.
        StringBuilder hashBuilder = new StringBuilder();

        //  Hashing Must Be Done Utilising Bytes, We Need To Convert Back To String In Order To
        //  Save The Password.
        for (byte b : hashStringToBytes) {
            hashBuilder.append(String.format("%02x", b));
        }

        return hashBuilder.toString();
    }

}
