package com.phonecompany;

import com.phonecompany.billing.TelephoneBillCalculator;

public class MainApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No log for processing! (For example \"420774567453 13-01-2020 18:10:15 13-01-2020 18:12:57\"");
        }
        else {
            StringBuilder phoneLog = new StringBuilder();
            phoneLog.append(args[0]);

           /*phoneLog.append("420774567453 13-01-2020 18:10:15 13-01-2020 18:12:57"+"\n");
            phoneLog.append("420774567453 13-01-2020 18:10:15 13-01-2020 18:17:57"+"\n");
            phoneLog.append("420776562353 18-01-2020 08:59:20 18-01-2020 09:10:00"+"\n");
            phoneLog.append("420776562353 18-01-2020 07:59:20 18-01-2020 08:10:10"+"\n");*/ // 0.5 + 5*1 + 6*0.2 = 6.7
            // System.out.println(phoneLog.toString());

            TelephoneBillCalculator telephoneBillCalculator = new TelephoneBillCalculator();
            System.out.println("");
            System.out.println("Celkova cena vsech novoru: " + telephoneBillCalculator.calculate(phoneLog.toString()));
        }
    }
}
