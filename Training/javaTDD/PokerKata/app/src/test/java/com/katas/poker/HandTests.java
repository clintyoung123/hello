package com.katas.poker;

// from https://projecteuler.net/problem=54

//In the card game poker, a hand consists of five cards and are ranked, from lowest to highest, in the following way:
// High Card: Highest value card.
// One Pair: Two cards of the same value.
// Two Pairs: Two different pairs.
// Three of a Kind: Three cards of the same value.
// Straight: All cards are consecutive values.
// Flush: All cards of the same suit.
// Full House: Three of a kind and a pair.
// Four of a Kind: Four cards of the same value.
// Straight Flush: All cards are consecutive values of same suit.
// Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.

//The cards are valued in the order:
// 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace.
//If two players have the same ranked hands then the rank made up of the highest value wins; for example, a pair of eights beats a pair of fives (see example 1 below).
//But if two ranks tie, for example, both players have a pair of queens, then highest cards in each hand are compared (see example 4 below); if the highest cards tie then the next highest cards are compared, and so on.

//Consider the following five hands dealt to two players:
//  Hand    Player 1	 	    Player 2	 	    Winner
//  1	 	5H 5C 6S 7S KD      2C 3S 8S 8D TD
//          Pair of Fives	 	Pair of Eights	 	Player 2
//  2	 	5D 8C 9S JS AC      2C 5C 7D 8S QH
//          Highest card Ace	Highest card Queen  Player 1
//  3	 	2D 9C AS AH AC      3D 6D 7D TD QD
//          Three Aces	 	    Flush with Diamonds Player 2
//  4	 	4D 6S 9H QH QC      3D 6D 7H QD QS
//          Pair of Queens      Pair of Queens
//          Highest card Nine	Highest card Seven  Player 1
//  5	 	2H 2D 4C 4D 4S      3C 3D 3S 9S 9D
//          Full House          Full House
//          With Three Fours	with Three Threes   Player 1

// The file, poker.txt, contains one-thousand random hands dealt to two players. Each line of the file contains ten cards (separated by a single space): the first five are Player 1's cards and the last five are Player 2's cards. You can assume that all hands are valid (no invalid characters or repeated cards), each player's hand is in no specific order, and in each hand there is a clear winner.
// How many hands does Player 1 win?


import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


class Hand
{
    enum Suit { D, S, C, H }
    enum Value {
        _2(2), _3(3), _4(4), _5(5), _6(6), _7(7), _8(8), _9(9), _T(10), _J(11), _Q(12), _K(13), _A(14);

        final int v;
        Value(int i) {
            v = i;
        }
    }
    static class Card
    {
        public final Suit suit;
        public final Value value;

        public Card(String s)
        {
            char[] chars = new char[2];
            s.getChars(0, 2, chars, 0);
            value = DecodeCard(chars[0]);
            suit  = DecodeSuit(chars[1]);
        }

        private static Suit DecodeSuit(char s)
        {
            switch (s)
            {
                case 'D': return Suit.D;
                case 'S': return Suit.S;
                case 'C': return Suit.C;
                case 'H': return Suit.H;
            }
            throw new IllegalArgumentException("bad suit code");
        }
        private static Value DecodeCard(char c)
        {
            switch (c)
            {
                case '2': return Value._2;
                case '3': return Value._3;
                case '4': return Value._4;
                case '5': return Value._5;
                case '6': return Value._6;
                case '7': return Value._7;
                case '8': return Value._8;
                case '9': return Value._9;
                case 'T': return Value._T;
                case 'J': return Value._J;
                case 'Q': return Value._Q;
                case 'K': return Value._K;
                case 'A': return Value._A;
            }
            throw new IllegalArgumentException("bad card value code");
        }
    }
    private final Card[] cards = new Card[5];

    @Override
    public String toString() {
        String result="[";
        for (Card card:cards) {
            result += card.value.toString() + card.suit.toString()+", ";
        }
        return result+"]";
    }

    public Hand(String card1, String card2, String card3, String card4, String card5)
    {
        cards[0] = new Card(card1);
        cards[1] = new Card(card2);
        cards[2] = new Card(card3);
        cards[3] = new Card(card4);
        cards[4] = new Card(card5);

        class CardComparator implements Comparator<Card> {
            public int compare(Card e1, Card e2) { return e2.value.compareTo(e1.value); }
        }
        Arrays.sort(cards, new CardComparator());


    }

    public boolean Beats(Hand other)
    {
        // Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.
        if (EvaluateRoyalFlush()    != other.EvaluateRoyalFlush()   ) return EvaluateRoyalFlush()    > other.EvaluateRoyalFlush();
        // Straight Flush: All cards are consecutive values of same suit.
        if (EvaluateStraightFlush() != other.EvaluateStraightFlush()) return EvaluateStraightFlush() > other.EvaluateStraightFlush();
        // Four of a Kind: Four cards of the same value.
        if (EvaluateFourOfAKind() != other.EvaluateFourOfAKind()) return EvaluateFourOfAKind() > other.EvaluateFourOfAKind();
        // Full House: Three of a kind and a pair.
        if (EvaluateFullHouse() != other.EvaluateFullHouse()) return EvaluateFullHouse() > other.EvaluateFullHouse();
        // Flush: All cards of the same suit.
        if (EvaluateFlush() != other.EvaluateFlush()) return EvaluateFlush() > other.EvaluateFlush();
        // Straight: All cards are consecutive values.
        if (EvaluateStraight() != other.EvaluateStraight()) return EvaluateStraight() > other.EvaluateStraight();
        // Three of a Kind: Three cards of the same value.
        if (EvaluateThreeOfAKind() != other.EvaluateThreeOfAKind()) return EvaluateThreeOfAKind() > other.EvaluateThreeOfAKind();
        // Two Pairs: Two different pairs.
        if (EvaluateTwoPairs() != other.EvaluateTwoPairs()) return EvaluateTwoPairs() > other.EvaluateTwoPairs();
        // One Pair: Two cards of the same value.
        if (EvaluateOnePair() != other.EvaluateOnePair()) return EvaluateOnePair() > other.EvaluateOnePair();
        // High Card: Highest value card.
        if (EvaluateHighCard() != other.EvaluateHighCard()) return EvaluateHighCard() > other.EvaluateHighCard();

        return false;
    }

    public long EvaluateHighCard() {
        long sum = 0;
        for (int i=0; i< 5; i++){
            // treat as HEX
            sum = sum * 16 + cards[i].value.v;
        }
        return sum;
    }

    public int EvaluateOnePair() {
        int kindCounter = 1;
        int kindValue = cards[0].value.v;

        for (int i=0; i< 4; i++){
            if (cards[i].value.v == cards[i+1].value.v ){
                kindCounter++;
                if (kindCounter==2){
                    return kindValue;
                }
            }else {
                kindCounter=1;
                kindValue = cards[i+1].value.v;
            }
        }
        return 0;
    }

    public int EvaluateTwoPairs() {

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        int maxDupCounter = 1;
        for (int i=0; i< 5; i++) {
            if (map.containsKey(cards[i].value.v)) {
                map.put(cards[i].value.v, 1 + map.get(cards[i].value.v));
                int dupCounter = map.get(cards[i].value.v);
                maxDupCounter = (dupCounter > maxDupCounter) ? dupCounter : maxDupCounter;
            } else {
                map.put(cards[i].value.v, 1);
            }
        }

        if (map.keySet().size()==3 && maxDupCounter<=2) {
            return cards[1].value.v * 100 + cards[3].value.v;
        }

        return 0;
    }

    public int EvaluateThreeOfAKind() {
        int kindCounter = 1;
        int kindValue = cards[0].value.v;

        for (int i=0; i< 4; i++){
            if (cards[i].value.v == cards[i+1].value.v ){
                kindCounter++;
                if (kindCounter==3){
                    return kindValue;
                }
            }else {
                kindCounter=1;
                kindValue = cards[i+1].value.v;
            }
        }
        return 0;
    }

    public int EvaluateStraight() {
        int v0 = cards[0].value.v;

        for (int i=1; i< 5; i++){
            Card card = cards[i];
            if (card.value.v+i != v0){
                return 0;
            }
        }
        return v0;
    }

    public long EvaluateFlush() {
        Suit s0 = cards[0].suit;
        for (Card card:cards){
            if (card.suit != s0){
                return 0;
            }
        }

        //return 1;
        return EvaluateHighCard();
    }

    public int EvaluateFullHouse() {
        int kindValue = 0;

        if (cards[0].value.v == cards[1].value.v && cards[3].value.v == cards[4].value.v ) {
            if (cards[2].value.v == cards[0].value.v || cards[2].value.v == cards[4].value.v) {
                kindValue = cards[2].value.v;
            }
        }
        return kindValue;
    }

    public int EvaluateFourOfAKind() {
        int kindCounter = 1;
        int kindValue = cards[0].value.v;

        for (int i=0; i< 4; i++){
            if (cards[i].value.v == cards[i+1].value.v ){
                kindCounter++;
                if (kindCounter==4){
                    return kindValue;
                }
            }else {
                kindCounter=1;
                kindValue = cards[i+1].value.v;
            }
        }
        return 0;
    }

    /**
     * Evaluate if this hand is Strait Flush
     * @return (6-14)the High Value of Flush, (0) not a Straight Flush
     */
    public int EvaluateStraightFlush() {
        Suit s0 = cards[0].suit;
        int v0 = cards[0].value.v;

        for (int i=1; i< 5; i++){
            Card card = cards[i];
            if (card.suit != s0){
                return 0;
            }
            if (card.value.v+i != v0){
                return 0;
            }
        }
        return v0;
    }

    public int EvaluateRoyalFlush() {
        if (cards[0].value != Value._A){
            return 0;
        }
        return EvaluateStraightFlush()>0?1:0;
    }

}

public class HandTests extends TestCase {
    public Hand[] HandsData = new Hand[]{
            //EvaluateRoyalFlush()
            new Hand("AC", "KC", "QC", "JC", "TC"),
            //EvaluateStraightFlush()
            new Hand("9D", "KD", "QD", "JD", "TD"),
            new Hand("6D", "5D", "4D", "3D", "2D"),
            //EvaluateFourOfAKind()
            new Hand("4D", "9S", "9C", "9H", "9D"),
            new Hand("2D", "7D", "2C", "2S", "2H"),
            //EvaluateFullHouse()
            new Hand("4D", "4S", "4C", "9H", "9D"),
            new Hand("3D", "3S", "3C", "2S", "2D"),

            //EvaluateFlush()
            new Hand("AD", "KD", "9D", "8D", "3D"),
            new Hand("AC", "KC", "9C", "8C", "2C"),

            //EvaluateStraight()
            new Hand("9D", "8S", "7C", "6H", "5D"),
            new Hand("6D", "5S", "4C", "3H", "2D"),

            //EvaluateThreeOfAKind(){
            new Hand("4D", "9S", "9C", "9H", "3D"),
            new Hand("2D", "KD", "2C", "2S", "TD"),

            //EvaluateTwoPairs(){
            new Hand("TD", "TS", "3C", "4H", "3D"),
            new Hand("TC", "TH", "2C", "2S", "5D"),

            //High Card: Highest value card.
            new Hand("AD", "9S", "6C", "4H", "3D"),
            new Hand("TC", "7H", "6C", "5S", "4D"),
    };

    public void test_CanInitializeHandWithGoodValues() {
        assertNotNull(new Hand("5H", "5C", "6S", "7S", "KD")); // does not throw
    }

    public void test_CannotInitializeHandWithBadSuit() {
        try {
            new Hand("5H", "5C", "6S", "7S", "KR"); // there is no suit called R
        } catch (IllegalArgumentException e) {
            assertEquals("bad suit code", e.getMessage());
            return;
        } catch (Exception e) {
            fail("wrong exception thrown");
        }
        fail("no exception thrown");
    }

    public void test_CannotInitializeHandWithBadValue() {
        try {
            new Hand("5H", "5C", "6S", "7S", "1S"); // there is no 1 card
        } catch (IllegalArgumentException e) {
            assertEquals("bad card value code", e.getMessage());
            return;
        } catch (Exception e) {
            fail("wrong exception thrown");
        }
        fail("no exception thrown");
    }

    // TODO:  add your tests here

    //region Evaluation Unittest
    public void test_EvaluateRoyalFlush() {
        assertTrue(new Hand("AD", "KD", "QD", "JD", "TD").EvaluateRoyalFlush() > 0);
        assertTrue(new Hand("AD", "KD", "QD", "JS", "TD").EvaluateRoyalFlush() == 0);
    }

    public void test_EvaluateStraightFlush() {
        assertEquals(new Hand("9D", "KD", "QD", "JD", "TD").EvaluateStraightFlush(), 13);
        assertEquals(new Hand("9D", "KD", "QD", "JS", "TD").EvaluateStraightFlush(), 0);
    }

    public void test_EvaluateFourOfAKind() {
        assertEquals(new Hand("4D", "9S", "9C", "9H", "9D").EvaluateFourOfAKind(), 9);
        assertEquals(new Hand("9D", "KD", "9C", "9S", "TD").EvaluateFourOfAKind(), 0);
    }

    public void test_EvaluateFullHouse() {
        assertEquals(new Hand("4D", "4S", "4C", "9H", "9D").EvaluateFullHouse(), 4);
        assertEquals(new Hand("9D", "KD", "9C", "9S", "TD").EvaluateFullHouse(), 0);
    }

    public void test_EvaluateFlush() {
        assertEquals(new Hand("4D", "9D", "2D", "8D", "3D").EvaluateFlush(), (((9*16+8)*16+4)*16+3)*16+2);
        assertEquals(new Hand("9D", "KD", "9C", "9S", "TD").EvaluateFlush(), 0);
    }

    public void test_EvaluateStraight() {
        assertEquals(new Hand("9D", "8S", "7C", "6H", "5D").EvaluateStraight(), 9);
        assertEquals(new Hand("9D", "8S", "7C", "6H", "4D").EvaluateStraight(), 0);
    }

    public void test_EvaluateThreeOfAKind() {
        assertEquals(new Hand("4D", "9S", "9C", "9H", "3D").EvaluateThreeOfAKind(), 9);
        assertEquals(new Hand("9D", "KD", "9C", "2S", "TD").EvaluateThreeOfAKind(), 0);
    }

    public void test_EvaluateTwoPairs() {
        assertEquals(new Hand("TD", "TS", "3C", "4H", "3D").EvaluateTwoPairs(), 10 * 100 + 3);
        assertEquals(new Hand("9D", "KD", "9C", "2S", "TD").EvaluateTwoPairs(), 0);
    }

    public void test_BeatsBlanketTest(){
        int n = HandsData.length;
        for (int i=0; i<n; i++) {
            for (int j=i+1; j<n; j++) {
                assertTrue ("Hand Failed:"+i+","+j+"{"+HandsData[i]+HandsData[j]+"}", HandsData[i].Beats(HandsData[j]));
            }
        }
    }

    public void testTemp() {
        assertTrue("value1" + HandsData[1].EvaluateStraightFlush()+ "value7" + HandsData[7].EvaluateStraightFlush(), HandsData[1].Beats(HandsData[7]));
    }
    //endregion

}