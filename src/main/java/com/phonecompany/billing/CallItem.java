package com.phonecompany.billing;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CallItem {
    private String phoneNumber;
    private BigDecimal price;
    private Calendar startCalendar;
    private Calendar endCalendar;

    public void setStartCalendar(Calendar calendar) {
        this.startCalendar = calendar;
    }

    public void setEndCalendar(Calendar calendar) {
        this.endCalendar = calendar;
    }

    private void setPhoneNumber(String phoneNumber) {
        // prevent create public setter
    }

    public CallItem() {
        BigDecimal price = BigDecimal.ZERO;
    }

    public Calendar getStartCalendar() {
        return startCalendar;
    }

    public Calendar getEndCalendar() {
        return endCalendar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setLogLine(String logLine) throws java.text.ParseException {
        String[] lineParsed = logLine.split(" "); // TODO tohle je treba vylepsit - slabota

        phoneNumber = lineParsed[0];

        startCalendar = Calendar.getInstance();
        startCalendar.setTime(getDateTimeSDF().parse(lineParsed[1]+" "+lineParsed[2]));

        endCalendar = Calendar.getInstance();
        endCalendar.setTime(getDateTimeSDF().parse(lineParsed[3]+" "+lineParsed[4]));

        //Calendar initialTimePeriod = Calendar.getInstance();
        //initialTimePeriod.setTime(getDateTimeSDF().parse(lineParsed[1]+" "+Config.INITIAL_TIME_PERIOD));

        //Calendar finalTimePeriod = Calendar.getInstance();
        //finalTimePeriod.setTime(getDateTimeSDF().parse(lineParsed[1]+" "+Config.FINAL_TIME_PERIOD));
    }

    public Calendar getTimePeriod(String strTime) throws java.text.ParseException {
        Calendar tempCalendar = this.getStartCalendar();
        String initialTimePeriodStr = getDateSDF().format(tempCalendar.getTime());
        initialTimePeriodStr = initialTimePeriodStr+" "+strTime;
        Calendar initialTimePeriod = Calendar.getInstance();
        initialTimePeriod.setTime(getDateTimeSDF().parse(initialTimePeriodStr));
        return initialTimePeriod;
    }

    private SimpleDateFormat getDateSDF() {
        return new SimpleDateFormat("dd-MM-yyyy");
    }

    private SimpleDateFormat getDateTimeSDF() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    }

    public CallItem copy() {
        CallItem ci = new CallItem();
        ci.setPhoneNumber(this.getPhoneNumber());
        ci.setStartCalendar(this.getStartCalendar());
        ci.setEndCalendar(this.getEndCalendar());
        ci.setPrice(this.getPrice());
        return ci;
    }

    public void recalcEndCalendar(int field, int amount) {
        Calendar newEndCalendar = (Calendar) this.getStartCalendar().clone();
        newEndCalendar.add(field, amount);
        this.setEndCalendar(newEndCalendar);
    }
}
