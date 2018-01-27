package com.company;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Klasa do zarządzania przedziałami czasowymi.
 */

public class DateHandler {
    LocalDate from, to;

    LocalDate dt;


    DateHandler (String from, String to) throws ParseException {
        this.from = LocalDate.parse(from);
        this.to = LocalDate.parse(to);
    }
    DateHandler (String dt) {
        this.dt=LocalDate.parse(dt);
        //konstruktor potrzebny dla graphera.
    }

    /**
     * Dzieli podany okres na przedziały po max 93 dni.
     * @return ArrayList z danymi typu String - kolejne daty końca/początku przedziałów.
     */

    public ArrayList getIntervals(){

        long diff = ChronoUnit.DAYS.between(from,to);

        ArrayList<String> Dates = new ArrayList<>();

        if(diff>93){
            LocalDate Iterator = from;
            while(!Iterator.isAfter(to)){
                Dates.add(Iterator.toString());
                Iterator = Iterator.plus(93, DAYS);
            }
            Dates.add(to.toString());
        }
        else{
            Dates.add(from.toString());
            Dates.add(to.toString());
        }

        return Dates;
    }

    public String Day(){
        return String.valueOf(dt.getDayOfWeek());
    }
}
