package com.techyourchance.unittestingfundamentals.exercise1;

import com.techyourchance.unittestingfundamentals.example2.StringReverser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NegativeNumberValidatorTest {

    private NegativeNumberValidator SUT;

    @Before
    public void setUp(){
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void test1(){
        boolean result = SUT.isNegative(-1);
        Assert.assertTrue(result);
    }

    @Test
    public void test2(){
        boolean result = SUT.isNegative(0);
        Assert.assertTrue(result);
    }

    @Test
    public void test3(){
        boolean result = SUT.isNegative(1);
        Assert.assertFalse(result);
    }
}