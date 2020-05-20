package com.phonecompany.billing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.*;

public class TelephoneBillCalculator implements TelephoneBillCalculatorItf {

    public BigDecimal calculateInternal(String phoneLog, boolean firstIsFree) {
        BigDecimal result = BigDecimal.ZERO;
        String phoneLoglines[] = phoneLog.split("\\r?\\n");
        // String.split("[\\r\\n]+") nebo taky takhle

        try {
            List<CallItem> ciList=new ArrayList<CallItem>();
            for (String line : phoneLoglines) {
                CallItem ci = new CallItem();
                ci.setLogLine(line);
                ci.setPrice(calculatePrice(ci));
                ciList.add(ci);
            }
            printList(ciList, "--Cena vsech hovoru--");

            List<CallItem> ciMaxEntries = searchMaxEntry(ciList);
            if (firstIsFree) {
                maxIsFree(ciMaxEntries);
                printList(ciList, "--Cena vsech hovoru bez nejcasteji volaneho cisla, tyto jsou zadarmo--");
            }

            result = sumTotalPrice(ciList);
        }
        catch (java.text.ParseException e) {
            // vyjimku trapne odchytnu, abych splnil rozhrani metody calculate
            System.out.println("Chyba parsovani radku logu");
        }
        return result;
    }

    public BigDecimal calculate(String phoneLog) {
        return calculateInternal(phoneLog, true);
    }

    private void maxIsFree(List<CallItem> ciList) {
        for (CallItem item: ciList) {
            item.setPrice(BigDecimal.ZERO);
        }
    }

    private List<CallItem> searchMaxEntry(List<CallItem> ciList) {
        List<String> sameEntries = new ArrayList<String>();
        Map.Entry<String, List<CallItem>> maxEntry = null;
        Map<String, List<CallItem>> ciMap =
                ciList.stream().collect(Collectors.groupingBy(w -> w.getPhoneNumber()));

        for (Map.Entry<String, List<CallItem>> entry : ciMap.entrySet()) {
            if ((maxEntry == null) || (entry.getValue().size() > maxEntry.getValue().size()))
            {
                maxEntry = entry;
                sameEntries.clear();
                sameEntries.add(entry.getKey());
            }
            else if ((maxEntry != null) && (entry.getValue().size() == maxEntry.getValue().size())) {
                sameEntries.add(entry.getKey());
            }
        }
        if (sameEntries.size() == 1) {
            return maxEntry.getValue();
        }
        else {
            // tohle je divne, ale v zadani je takova podivna veta: "zpoplatneny nebudou hovory na cislo s aritmeticky nejvyzsi hodnotou"
            // vysvetluji si to tak, ze se nezpoplatnit v pripade shody nejvyzsi telefonni cislo.
            // Kdyby ta veta znela "zpoplatneny nebudou hovory s aritmeticky nejvyzsi cenou za jednotliva volani" tak bych sumoval delku hovoru
            // a vydelil to poctem, ale v zadani se pise o "telefonim cisle s nejvyzsi hodnotou"
            String maxKey = null;
            for (String item: sameEntries) {
                if ((maxKey == null) || new BigDecimal(item).compareTo(new BigDecimal(maxKey)) == 1) {
                    maxKey = item;
                }
            }
            return ciMap.get(maxKey);
        }
    }

    public BigDecimal calculateInitialPeriodPrice(CallItem ci) throws java.text.ParseException {
        Calendar startCalendar = ci.getStartCalendar();
        Calendar endCalendar = ci.getEndCalendar();
        Calendar initialTimePeriod = ci.getTimePeriod(Config.INITIAL_TIME_PERIOD);
        Calendar finalTimePeriod = ci.getTimePeriod(Config.FINAL_TIME_PERIOD);
        //System.out.println("max(startCalendar, initialTimePeriod)"+CalendarTool.calendarToString(CalendarTool.max(startCalendar, initialTimePeriod)));
        //System.out.println("min(endCalendar, finalTimePeriod)"+CalendarTool.calendarToString(CalendarTool.min(endCalendar, finalTimePeriod)));

        long inInterval = CalendarTool.minutesBetween(
                CalendarTool.max(startCalendar, initialTimePeriod),
                CalendarTool.min(endCalendar, finalTimePeriod)); // v intervalue
        long beforeInterval = CalendarTool.minutesBetween(
                CalendarTool.min(startCalendar, initialTimePeriod),
                CalendarTool.min(endCalendar, initialTimePeriod)); // pred zapocetim intervalu
        long afterInterval = CalendarTool.minutesBetween(
                CalendarTool.max(startCalendar, finalTimePeriod),
                CalendarTool.max(endCalendar, finalTimePeriod)); // po intervalu

        return new BigDecimal(beforeInterval).multiply(Config.NORMAL_RATE).add(
               new BigDecimal(inInterval).multiply(Config.INTERVAL_RATE)).add(
               new BigDecimal(afterInterval).multiply(Config.NORMAL_RATE) );
    }

    public BigDecimal calculatePrice(CallItem ci) throws java.text.ParseException {
        /*Uvodni perioda ma tezsi vypocet, ale pak uz je to vsechno za jeden peniz*/
        BigDecimal price = BigDecimal.ZERO;

        // asi bych mel spis implementovat rozhrani clonable
        // jestli se jedna o hovor delsi nez je zakladni perioda (pretece do zlevnene), tak si posunu cas konce na konec periody
        // a tak si udelam kopii
        CallItem loCallItem = ci.copy();

        // tak jakpak dlouho nam trval hovor
        BigDecimal callDuration = new BigDecimal(CalendarTool.minutesBetween(loCallItem.getStartCalendar(), loCallItem.getEndCalendar()));
        //System.out.println(callDuration);

        // po uvodni periode se jiz vsechno pocita za stejnou cenu (useknu tedy uvodni casovou periodu) a nasobim danou cenou
        if (callDuration.compareTo(Config.REDUCED_RATE_PERIOD_D) == 1) {
            price = callDuration.subtract(Config.REDUCED_RATE_PERIOD_D).multiply(Config.REDUCED_RATE);
            // vsechno nad RatePeriod je za jednu cenu. (To jsem spocital). Tak tedka jeste zjistit kolik
            // je koncovy cas od zacatku hovoru po konec periody (abych mohl tedy spocist tu slozitejsi cast co neni za jednu cenu)
            loCallItem.recalcEndCalendar(Calendar.MINUTE, Config.REDUCED_RATE_PERIOD);
        }

        // vypocet uvodni perioda je slozitejsi (muzou tam byt kousky hovoru v levnejsi nebo drazsi sazbe)
        price = price.add( calculateInitialPeriodPrice(loCallItem) );
        return price.setScale (2, BigDecimal.ROUND_HALF_EVEN);
    }

    private BigDecimal sumTotalPrice(List<CallItem> ciList) {
        BigDecimal result = BigDecimal.ZERO;
        for (CallItem item: ciList) {
            result = result.add(item.getPrice());
        }
        return result;
    }

    private void printList(List<CallItem> ciList, String info) {
        System.out.println(info);
        for (CallItem item: ciList) {
            System.out.println(item.getPhoneNumber()+
                    " ("+
                        CalendarTool.calendarToString(item.getStartCalendar())+
                    " to "+
                        CalendarTool.calendarToString(item.getEndCalendar())+
                    ")"+" - "+item.getPrice()+" kc");
        }
    }

}
