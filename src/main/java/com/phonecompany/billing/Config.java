package com.phonecompany.billing;

import java.math.BigDecimal;

public final class Config {
    private Config() {
        // restrict instantiation
    }

    public static final int REDUCED_RATE_PERIOD = 5;
    public static final BigDecimal REDUCED_RATE_PERIOD_D = BigDecimal.valueOf(REDUCED_RATE_PERIOD);
    public static final BigDecimal INTERVAL_RATE = BigDecimal.valueOf(1);
    public static final BigDecimal NORMAL_RATE = BigDecimal.valueOf(0.5);
    public static final BigDecimal REDUCED_RATE = BigDecimal.valueOf(0.2);


    public static final String INITIAL_TIME_PERIOD = "08:00:00";
    public static final String FINAL_TIME_PERIOD = "16:00:00";
}
