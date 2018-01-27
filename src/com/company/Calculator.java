package com.company;

import java.util.ArrayList;

/**
 * Klasa wykorzystywana do pomocy w obliczeniach.
 */

public class Calculator {

    /**
     * Liczy średnią wartość dla danej ArrayListy.
     * @param values
     * @return Double avg
     */

    public Double avg (ArrayList<Double> values){
        Double sum = 0.0;
        for(Double D: values){
            sum=sum+D;
        }
        Double N = Double.valueOf(values.size());
        return sum/N;
    }

    /**
     * Zwraca obiekt typu Currency, który posiada największą wartośc pola Value.
     * @param values
     * @return Currency max
     */

    public Currency max (ArrayList<Currency> values){
        Double min = 1000.0;
        Double max = 0.0;
        Currency maxC = null;
        for(int i=0; i<values.size(); i++){
            if(values.get(i).Value > max ){
                max = values.get(i).Value;
                maxC=values.get(i);
            }
        }
        return maxC;
    }

    /**
     * Zwraca obiekt typu Currency, który posiada najmniejszą wartośc pola Value.
     * @param values
     * @return Currency min
     */

    public Currency min (ArrayList<Currency> values){
        Double min = 1000.0;
        Double max = 0.0;
        Currency minC = null;
        for(int i=0; i<values.size(); i++){
            if(values.get(i).Value < min ){
                min = values.get(i).Value;
                minC=values.get(i);
            }
        }
        return minC;
    }
}
