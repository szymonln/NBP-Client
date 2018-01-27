package com.company;

import java.util.ArrayList;

/**
 * Klasa wykorzystywana do rysowania wykresów tygodniowych.
 */
public class Grapher {

    /**
     * Rysuje wykres tygodniowy
     * @param Prices ArrayLista z polami typu Currency.
     */
    public void write(ArrayList<Currency> Prices){
        Double Max = 0.0;
        for(int i=0; i<Prices.size(); i++){
            if(Prices.get(i).Value>Max)
                Max = Prices.get(i).Value;
        }

        Double MaxBoxes = 20.0;


        String Day;



        for(int i=0; i<5; i++){
            int j = 1;
            for(Currency c : Prices){
                DateHandler DH = new DateHandler(c.date);
                Day = DH.Day();
                if(Day.equals(DayOfWeek.values()[i].toString())){
                    Double BoxesAmount = null;
                    BoxesAmount = MaxBoxes - 150*(1-c.Value/Max);
                    System.out.print(Day + j + " ");
                    for (int p=0; p<12-(Day + j + " ").length(); p++)
                        System.out.print(" ");
                    for (int k=0; k<BoxesAmount; k++){
                        System.out.print("█");
                    }
                    System.out.print(" " + c.Value + " zł");
                    System.out.println();
                    j++;
                }
            }


            for(int l = 0; l < 30; l++){
                //System.out.print("--");
            }
            System.out.println();
        }




    }
}
