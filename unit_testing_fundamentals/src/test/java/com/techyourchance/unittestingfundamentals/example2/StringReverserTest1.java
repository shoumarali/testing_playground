package com.techyourchance.unittestingfundamentals.example2;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringReverserTest1 {

    StringReverser SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringReverser();
    }

    @Test
    public void reverse_emptyString_emptyStringReturned() {
        String result = SUT.reverse("");
        assertThat(result, CoreMatchers.is(""));
    }

    @Test
    public void reverse_singleCharacter_sameStringReturned() {
        String result = SUT.reverse("A");
        assertThat(result,CoreMatchers.is("A"));
    }

    @Test
    public void reverse_longString_reversedStringReturned() {

        String result = SUT.reverse("Ali");
        assertEquals("ilA", result);
    }


}