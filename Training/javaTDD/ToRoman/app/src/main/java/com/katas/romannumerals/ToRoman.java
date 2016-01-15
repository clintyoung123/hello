package com.katas.romannumerals;

public class ToRoman {
    public static String Numeral(int n)
    {
        String s = "";
        if (n < (5-1)){
            for (int i=0; i<n; i++) {
                s = s + "I";
            }
            return s;
        }else if (n>= (5-1) && n<5){
            s="I" + "V";
            return s;
        }else if (n>=(5) && n < (5*2-1)){
            s = "V";
            for (int i=n-5; i>0; i--) {
                s = s + "I";
            }
            return s;
        } else if (n==9){
            s="IX";
            return s;
        } else if (n<40){
            return s="X"+Numeral(n-10);
        }
        if (n<50){
            return "XL"+ Numeral(n-40);
        }
        if (n<90){
            return "L"+Numeral(n-50);
        }
        if (n<100){
            return "XC" + Numeral(n-90);
        }
        if (n<400){
            return "C" + Numeral(n-100);
        }
        if (n<500){
            return "CD" + Numeral(n-400);
        }
        if (n<900){
            return "D" + Numeral(n-500);
        }
        if (n<1000){
            return "CM" + Numeral(n-900);
        }
        if (n<4000){
            return "M" + Numeral(n-1000);
        }


        return s;
    }
}
