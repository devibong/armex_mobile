package com.qsystem.android.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    private final String iv ="fmsivkey12345678";
    private final String secretKey ="fmssecretkey1234";



    public Encryption(){
        //this.secretKey = secretKey;
        //this.iv =
    }

    public String encode(String text){
        byte[] encrypted = null;

        if(text == null || text.length() == 0)
            return null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            encrypted = cipher.doFinal(text.getBytes());

        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
