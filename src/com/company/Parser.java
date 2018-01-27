package com.company;
import com.sun.istack.internal.FinalArrayList;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Parsuje dany ciąg znaków jako plik JSON.
 * Zwraca ArrayListę z objektami Currency.
 */

public class Parser {

    String json; //przydatny do późniejszego tworzenia readera w parserze

    Parser(String json){
        this.json=json;
    }


    /**
     * Parsuje tabelę A z wykorzystaniem HashMap w Currency.
     * Zwraca Arraylistę obiektów Currency.
     * Jeden obiekt Currency reprezentuje wartości danej waluty
     * lub złota dla jednego dnia.
     * @return ArrayList
     * @throws IOException
     */



    public ArrayList<Currency> parse() throws IOException {

        JsonParser parser = Json.createParser(new StringReader(json));

        ArrayList<Currency> Concat = new ArrayList<>();

        JsonParser.Event event = parser.next();
        while (parser.hasNext()) {
            event = parser.next();

            Currency Cr = new Currency();

            if(event == JsonParser.Event.KEY_NAME && (parser.getString().equals("currency"))){

                event = parser.next();
                String name = parser.getString();
                event = parser.next();
                String code = parser.getString();
                Cr.code = code;
                Cr.name = name;
            }
            if (event == JsonParser.Event.START_ARRAY) {
                event=parser.next();
                while (parser.hasNext() && event != JsonParser.Event.END_ARRAY) {


                    String key,val;
                    key = null;
                    val = null;

                    event = parser.next();


                    if (event == JsonParser.Event.KEY_NAME){
                        key = parser.getString();
                    }

                    event=parser.next();

                    if (event == JsonParser.Event.VALUE_STRING) {
                        val = parser.getString();
                    }



                    if (event == JsonParser.Event.VALUE_NUMBER){
                        JsonValue rateJS = parser.getValue();
                        val = rateJS.toString();

                    }


                    if(key != null && val != null){
                        Cr.Rates.put(key, val);
                    }



                }
            }

            if(event == JsonParser.Event.END_ARRAY) Concat.add(Cr);
        }


        return Concat;

    }

    /**
     * Parsuje plik Json dla danych dot. złota.
     * @return ArrayList<Currency> z wartościami cen
     */

    public ArrayList<HashMap<String,String>> parseGold(){
        JsonParser parser = Json.createParser(new StringReader(json));
        JsonParser.Event event = parser.next();
        ArrayList <HashMap<String,String>> Concat = new FinalArrayList<>();

        while(parser.hasNext()){


            if (event == JsonParser.Event.START_ARRAY){


                HashMap<String, String> Day = new HashMap<>();

                    event = parser.next();
                    event = parser.next();
                    event = parser.next();
                    Day.put("data", parser.getString());
                    event = parser.next();
                    event = parser.next();
                    Day.put("cena", parser.getString());

                Concat.add(Day);
            }
            event=parser.next();


        }
        return Concat;

    }

    /**
     * Parsuje tabelę typu A bez wykkorzystywania HashMap.
     * @return ArrayList
     * @throws IOException
     */


    public ArrayList<Currency> Aparse() throws IOException {

        JsonParser parser = Json.createParser(new StringReader(json));

        ArrayList<Currency> Concat = new ArrayList<>();

        JsonParser.Event event = parser.next();
        while (parser.hasNext()) {
            event = parser.next();



            if(event == JsonParser.Event.KEY_NAME && parser.getString().equals("currency")){
                Currency Cr = new Currency();
                event = parser.next();
                Cr.name = parser.getString();
                event = parser.next();
                event = parser.next();
                Cr.code = parser.getString();
                event = parser.next();
                event = parser.next();
                Cr.Value = Double.parseDouble(parser.getString());
                Concat.add(Cr);
            }

        }

        return Concat;

    }

    /**
     * Parsuje tabelę typu C.
     * @return ArrayList
     * @throws IOException
     */


    public ArrayList<Currency> Cparse() throws IOException {

        JsonParser parser = Json.createParser(new StringReader(json));

        ArrayList<Currency> Concat = new ArrayList<>();

        JsonParser.Event event = parser.next();
        while (parser.hasNext()) {
            event = parser.next();



            if(event == JsonParser.Event.KEY_NAME && parser.getString().equals("currency")){
                Currency Cr = new Currency();
                event = parser.next();
                Cr.name = parser.getString();
                event = parser.next();
                event = parser.next();
                Cr.code = parser.getString();
                event = parser.next();
                event = parser.next();
                Cr.Bid = Double.parseDouble(parser.getString());
                event=parser.next();
                event=parser.next();
                Cr.Ask = Double.parseDouble(parser.getString());

                Concat.add(Cr);
            }

        }

        return Concat;

    }


    public ArrayList<Currency> Dparse() throws IOException {

        JsonParser parser = Json.createParser(new StringReader(json));

        ArrayList<Currency> Concat = new ArrayList<>();
        String name = null;
        JsonParser.Event event = parser.next();
        while (parser.hasNext()) {
            event = parser.next();

            if(event == JsonParser.Event.KEY_NAME && parser.getString().equals("currency")) {
                event = parser.next();
                name = parser.getString();
            }


            if(event == JsonParser.Event.KEY_NAME && parser.getString().equals("no")){
                Currency Cr = new Currency();
                Cr.name = name;
                event = parser.next();
                event = parser.next();
                event = parser.next();
                Cr.date = parser.getString();
                event = parser.next();
                event = parser.next();
                Cr.Value = Double.parseDouble(parser.getString());

                Concat.add(Cr);
            }

        }

        return Concat;

    }




}


