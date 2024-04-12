package com.tools;

import java.math.BigInteger;

public class Check {
    public static boolean isInteger ( BigInteger integer ) {
        return integer.compareTo ( BigInteger.valueOf ( 2147483647 ) ) < 1 &&
               integer.compareTo ( BigInteger.valueOf ( - 2147483648 ) ) > - 1;
    }
    
    public static boolean isLong ( BigInteger integer ) {
        return integer.compareTo ( BigInteger.valueOf ( 9_223_372_036_854_775_807L ) ) < 1 &&
               integer.compareTo ( BigInteger.valueOf ( - 9_223_372_036_854_775_808L ) ) > - 1;
    }
}
