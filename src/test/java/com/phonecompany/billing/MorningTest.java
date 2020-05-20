package com.phonecompany.billing;

import org.junit.Assert;
import org.junit.Test;
import java.math.BigDecimal;

public class MorningTest {

    @Test
    public void test() {
        StringBuilder phoneLog = new StringBuilder();
        phoneLog.append("420776562353 18-01-2020 07:59:20 18-01-2020 08:10:10");

        TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculator();
        BigDecimal amount = telephoneBillCalculator.calculateInternal(phoneLog.toString(), false);
        System.out.println("amount="+amount);
        Assert.assertEquals(amount.compareTo(new BigDecimal(6.7).setScale(2, BigDecimal.ROUND_HALF_EVEN)), 0);
    }
}
