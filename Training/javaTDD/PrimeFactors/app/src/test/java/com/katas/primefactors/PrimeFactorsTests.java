package com.katas.primefactors;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PrimeFactorsTests extends TestCase {

    private static String ToString(Collection<Integer> coll)
    {
        StringBuilder sb = new StringBuilder("{ ");
        for (int i: coll) {
            sb.append(i);
            sb.append(' ');
        }
        sb.append('}');
        return sb.toString();
    }
    private static void assertArrayEqualsList(Integer[] expected, List<Integer> actual) { assertEquals(ToString(new ArrayList(Arrays.asList(expected))), ToString(actual)); }

    public void testThereAreNoFactorsOf1 () throws Exception { assertArrayEqualsList(new Integer[]{}, PrimeFactors.Of(1)); }
    public void testTheFactorOf2Is2      () throws Exception { assertArrayEqualsList(new Integer[]{2}, PrimeFactors.Of(2)); }
    public void testTheFactorOf3Is3      () throws Exception { assertArrayEqualsList(new Integer[]{3}, PrimeFactors.Of(3)); }
    public void testTheFactorsOf4Are2And2() throws Exception { assertArrayEqualsList(new Integer[]{2, 2}, PrimeFactors.Of(4)); }
    public void testTheFactorsOf6Are2And3() throws Exception { assertArrayEqualsList(new Integer[]{2, 3}, PrimeFactors.Of(6)); }
    public void testTheFactorsOf8Are3Twos() throws Exception { assertArrayEqualsList(new Integer[]{2, 2, 2}, PrimeFactors.Of(8)); }
    public void testTheFactorsOf9AreTwo3s() throws Exception { assertArrayEqualsList(new Integer[]{3, 3   }, PrimeFactors.Of(9)); }

    public void testLargeComposite() throws Exception {
        assertArrayEqualsList(new Integer[]{2, 2, 2, 3, 3, 5, 5, 7, 11, 13},
                PrimeFactors.Of(2 * 2 * 2 * 3 * 3 * 5 * 5 * 7 * 11 * 13));
    }

    public void testLargePrime() throws Exception {
        assertArrayEqualsList(new Integer[]{1000000007},
                PrimeFactors.Of(1000000007));
    }

}
