package com.phonecompany.billing;

import org.junit.Assert;
import org.junit.Test;
import java.math.BigDecimal;

public class LongTest {

    @Test
    public void test() {
        StringBuilder phoneLog = new StringBuilder();
        phoneLog.append("420776562353 18-01-2020 08:59:20 18-01-2020 09:10:00");

        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculator();
        BigDecimal amount = telephoneBillCalculator.calculateInternal(phoneLog.toString(), false);
        System.out.println("amount="+amount);
        Assert.assertEquals(amount.compareTo(new BigDecimal(6.2).setScale(2, BigDecimal.ROUND_HALF_EVEN)), 0);
    }
}