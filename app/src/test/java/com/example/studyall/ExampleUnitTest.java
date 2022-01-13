package com.example.studyall;

import org.junit.Test;

import static org.junit.Assert.*;

import android.icu.util.IslamicCalendar;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void subtraction_isCorrect() {
        assertEquals(5,new Calculation().subtractTwoNumbers(8,3),0);
    }

    @Test
    public void multiplication_isCorrect() {
        assertEquals(24, new Calculation().multiplyTwoNumbers(8,3),0);
    }

    @Test
    public void division_isCorrect() {
        assertEquals(3, new Calculation().divideTwoNumbers(9,3),0);
    }

    @Test
    public void percentage_isCorrect() {
        assertEquals(0.03, new Calculation().calculatePercentage(3),0);
    }


}
