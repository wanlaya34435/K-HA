package com.zti.framework.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class Authentication {

    public static String getSalt(){
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        SecureRandom random = new SecureRandom();

        byte salt[] = new byte[12];
        random.nextBytes(salt);
        String s = encoder.encodeToString(salt);
        return encoder.encodeToString(salt);
    }


}
