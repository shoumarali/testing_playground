package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringDuplicatorTest {

    StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringDuplicator();
    }

    @Test
    public void stringDuplicator_emptyString_returnEmptyString() {
        String result = SUT.duplicate("");
        Assert.assertEquals("",result);
    }

    @Test
    public void stringDuplicator_singleCharacter_DuplicatedStringReturned() {
        String result = SUT.duplicate("A");
        Assert.assertEquals("AA",result);
    }

    @Test
    public void stringDuplicator_longCharacter_DuplicatedStringReturned() {
        String result =SUT.duplicate("Ali");
        Assert.assertEquals("AliAli",result);
    }

}