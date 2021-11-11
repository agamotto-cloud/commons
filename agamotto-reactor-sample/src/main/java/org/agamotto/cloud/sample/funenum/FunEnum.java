package org.agamotto.cloud.sample.funenum;

import java.util.function.Supplier;

public enum FunEnum {

    getInstance;


    private Supplier supplier;

    FunEnum() {

    }


    private static String getException() {
        System.out.println(123);
        return "123";

    }

    public  enum EnumDemo{
        A;
        EnumDemo(){

        }
    }
}
