package com.zti.kha.security;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by up on 26/9/2558.
 */
public class SessionIdentifierGenerator {
    public static String nextSessionId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(256, random).toString(32);
    }
}

