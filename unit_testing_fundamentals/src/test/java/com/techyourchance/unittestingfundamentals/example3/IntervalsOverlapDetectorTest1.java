package com.techyourchance.unittestingfundamentals.example3;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntervalsOverlapDetectorTest1 {

    IntervalsOverlapDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsOverlapDetector();
    }

    @Test
    public void isOverlap_interval1IsBeforeInterval2_falseReturned() {
        Interval interval1= new Interval(-1,5);
        Interval interval2 = new Interval(8,12);
        boolean result = SUT.isOverlap(interval1,interval2);
        Assert.assertFalse(result);
    }

    @Test
    public void isOverlap_interval1isAfterInterval_falseReturned() {
        boolean result = SUT.isOverlap(
                new Interval(3,10),
                new Interval(-5,2)
        );
        Assert.assertFalse(result);
    }

    @Test
    public void isOverlap_interval1IsOverlapsInterval2AtStart_trueReturned() {
        Interval interval1= new Interval(-1,5);
        Interval interval2 = new Interval(3,12);
        boolean result = SUT.isOverlap(interval1,interval2);
        Assert.assertTrue(result);
    }

    @Test
    public void isOverlap_interval1IsOverlapsInterval2AtEnd_trueReturned() {
        boolean result = SUT.isOverlap(
                new Interval(2,10),
                new Interval(-5,3)
        );
        Assert.assertTrue(result);
    }

    @Test
    public void isOverlap_interval1isInsideInterval2_trueReturned() {
        boolean result = SUT.isOverlap(
                new Interval(3,7),
                new Interval(-1,9)
        );
        Assert.assertTrue(result);
    }

    @Test
    public void isOverlap_interval1ContainsInterval2_trueReturned() {
        boolean result = SUT.isOverlap(
                new Interval(-1,10),
                new Interval(3,7)
        );
        Assert.assertTrue(result);
    }

    @Test
    public void isOverlap_interval1BeforeAdjacentInterval2_falseReturned() {
        boolean result = SUT.isOverlap(
                new Interval(-1,5),
                new Interval(5,9)
        );
        Assert.assertFalse(result);
    }

    @Test
    public void name() {
        boolean result= SUT.isOverlap(
                new Interval(5,9),
                new Interval(-1,5)
        );
        Assert.assertFalse(result);
    }
//interval one before interval two
    //interval one after interval two
    //interval one overlap interval two at the end
    //interval one overlaps interval two at the start
    //interval one overlaps within interval 2
    //interval one contains interval 2
    //interval one before adjacent interval 2
    //interval one after adjacent interval 2
}