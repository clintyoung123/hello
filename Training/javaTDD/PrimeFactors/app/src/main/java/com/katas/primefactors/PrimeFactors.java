package com.katas.primefactors;

import java.util.ArrayList;

public class PrimeFactors {
    public static ArrayList<Integer> Of(int n) throws Exception
    {
        ArrayList<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        return factors;
        //throw new Exception("not implemented yet!");
    }

    public static ArrayList<Integer> primesUnder(int n) throws Exception
    {

        boolean[] primes = new boolean[n+1];
        for (int i=0; i<(n+1); i++){
            primes[i]=true;
        }

        for (int i = 2; i < (n+1)/2; i++){
            int s = i+i;
            while (s < n+1) {
                primes[s] = false;
                s += i;
            }
        }

        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i=2; i<(n+1); i++){
            if (primes[i])
                result.add(i);
        }

        return result;
        //throw new Exception("not implemented yet!");
    }
}
