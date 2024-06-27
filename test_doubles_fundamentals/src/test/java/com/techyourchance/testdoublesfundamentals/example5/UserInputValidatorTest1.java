package com.techyourchance.testdoublesfundamentals.example5;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserInputValidatorTest1 {

     UserInputValidator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new UserInputValidator();
    }

    @Test
    public void  isValidFullName_validFullName_returnedTrue() {
        Assert.assertTrue(SUT.isValidFullName("Ali Shoumar"));
    }

    @Test
    public void isValidFullName_notValidFullName_returnedFalse() {
        Assert.assertFalse(SUT.isValidFullName(""));
    }

    @Test
    public void isValidUserName_validUserName_returnedTrue() {
        Assert.assertTrue(SUT.isValidUsername("Ali Shoumar"));
    }

    @Test
    public void isValidUserName_notValidUserName_returnedFalse() {
        Assert.assertFalse(SUT.isValidUsername(""));
    }
}