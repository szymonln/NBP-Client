package com.company;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EventParser {
    String arg[];
    EventParser(String arg[]){
        this.arg=arg;
    }

    /**
     * Parsuje argumenty lini komend i wywołuje odpowiednie funkcje
     * dla żadanego działania programu.
     * @throws IOException
     * @throws ParseException
     */

    public void run() throws IOException, ParseException {

        if(arg[0].equals("-d")){
            String Date = arg[1];
            String Code = arg[2];
            String URLGold = "http://api.nbp.pl/api/cenyzlota/" + Date + "/?format=json";
            String URLcode = "http://api.nbp.pl/api/exchangerates/rates/A/" + Code + "/" + Date + "/?format=json";
            HttpRouter router = new HttpRouter();
            String goldJson = router.call(URLGold);
            String currencyJson = router.call(URLcode);

            Parser parserGold = new Parser(goldJson);
            ArrayList<HashMap<String,String>> Gold = parserGold.parseGold();
            String GoldDate, GoldValue;
            GoldDate = GoldValue = null;
            for (HashMap<String,String> Cr : Gold){
                for (Map.Entry<String, String> entry : Cr.entrySet()){
                    String key = entry.getKey();
                    if(key.equals("data"))
                        GoldDate = entry.getValue();
                    if(key.equals("cena"))
                        GoldValue = entry.getValue();
                }
            }

            Parser parserCurr = new Parser(currencyJson);
            ArrayList<Currency> Curr = parserCurr.parse();
            String CurrDate, Value, Name;
            CurrDate = Value = null;
            Name = Curr.get(0).name;
            for (Currency Cr : Curr){

                for (Map.Entry<String,String> entry : Cr.Rates.entrySet()){
                    String key = entry.getKey();
                    if(key.equals("effectiveDate"))
                        CurrDate = entry.getValue();
                    if(key.equals("mid"))
                        Value = entry.getValue();
                }
            }

            System.out.println("Cena złota w dniu " + GoldDate + " wynosi(ła) " + GoldValue + "zł");
            System.out.println("Cena waluty " + Code.toUpperCase() + " w dniu " + CurrDate + " wynosi(ła) " + Value + " zł");

        }

        if(arg[0].equals("-a")){
            String StartDate = arg[1];
            String EndDate = arg[2];

            DateHandler DH = new DateHandler(StartDate,EndDate);
            ArrayList Intervals  = new ArrayList();
            Intervals = DH.getIntervals();
            ArrayList<Double> prices = new ArrayList<>();

            ArrayList<ArrayList<HashMap<String, String>>> Concat = new ArrayList<>();
            for(int i = 0; i<Intervals.size(); i++){
                if(i+1<Intervals.size()){
                    String URLGold = "http://api.nbp.pl/api/cenyzlota/" + Intervals.get(i) + "/" + Intervals.get(i+1)  + "/?format=json";
                    HttpRouter router = new HttpRouter();
                    String goldJson = router.call(URLGold);
                    Parser parserGold = new Parser(goldJson);
                    ArrayList<HashMap<String,String>> Gold = parserGold.parseGold();
                    Concat.add(Gold);

                }
            }

            for(ArrayList<HashMap<String,String>> Arr: Concat){
                for(HashMap<String,String> Hm : Arr){
                    for(Map.Entry<String,String> E : Hm.entrySet()){
                        String key = E.getKey();
                        String value = E.getValue();
                        if(key.equals("cena")){
                            Double valued = Double.parseDouble(value);
                            prices.add(valued);
                        }
                    }
                }
            }
            Calculator calc = new Calculator();
            Double avg = calc.avg(prices);
            DecimalFormat dec = new DecimalFormat("#0.00");


            System.out.println("Srednia cena zlota od " + StartDate + " do " + EndDate + " wynosi " + dec.format(avg) +" zł");

        }

        if(arg[0].equals("-amp")){
            String StartDate = arg[1];
            String EndDate = arg[2];

            DateHandler DH = new DateHandler(StartDate,EndDate);
            ArrayList Intervals  = new ArrayList();
            Intervals = DH.getIntervals();
            ArrayList<Double> prices = new ArrayList<>();

            ArrayList<ArrayList<Currency>> Concat = new ArrayList<>();
            for(int i = 0; i<Intervals.size(); i++){
                if(i+1<Intervals.size()){
                    String URL = "http://api.nbp.pl/api/exchangerates/tables/A/" + Intervals.get(i) + "/" + Intervals.get(i+1)  + "/?format=json";
                    HttpRouter router = new HttpRouter();
                    String Json = router.call(URL);

                    Parser parser = new Parser(Json);
                    ArrayList<Currency> Rate = parser.Aparse();
                    Concat.add(Rate);
                }
            }

            ArrayList<Currency> Prices = new ArrayList();
            for(ArrayList<Currency> R : Concat){
                for(Currency C : R){
                    Prices.add(C);
                }
            }

            for(Currency C : Prices){
                //System.out.println(C.name + C.code + C.Value);
            }

            Double Max = 0.0;
            Double Min = 1000000.0;
            Double MaxAmp = 0.0;
            String MaxCode = Prices.get(0).name;

            for(int i=0; i<35; i++){
                Double Value = 0.0;
                Max = 0.0;
                Min = 100000.0;
                Double Amp = null;
                for(int j=i; j<Prices.size(); j=j+35) { //bo tyle jest walut
                    Value = Prices.get(j).Value;
                    if (Value < Min) {
                        Min = Value;
                    }
                    if (Value > Max) {
                        Max = Value;
                    }

                }
                Amp = Max - Min;
                if(Amp > MaxAmp){
                        MaxCode = Prices.get(i).name;
                        //System.out.println(MaxCode);
                        MaxAmp = Amp;
                    }
                }


            System.out.println("Waluta, która uległa największym wahaniom od " + StartDate + " do " + EndDate + " to " + MaxCode);

        }

        if(arg[0].equals("-l")){
            String Date = arg[1];
            String URL = "http://api.nbp.pl/api/exchangerates/tables/C/" + Date + "/?format=json";
            HttpRouter router = new HttpRouter();
            String Json = router.call(URL);
            Parser parser = new Parser(Json);
            ArrayList<Currency> Rates = parser.Cparse();

            Double Min = 1000.0;
            String MinName=null;
            for(int i=0; i< Rates.size(); i++){
                if(Rates.get(i).Bid < Min){
                    MinName=Rates.get(i).name;
                    Min = Rates.get(i).Bid;
                }
            }

            System.out.println("Najtańsza waluta w zakupie dnia " + Date + " to: " + MinName);


        }

        if(arg[0].equals("-N")){
            String Date = arg[2];
            int n = Integer.valueOf(arg[1]);
            String URL = "http://api.nbp.pl/api/exchangerates/tables/C/" + Date + "/?format=json";
            HttpRouter router = new HttpRouter();
            String Json = router.call(URL);
            Parser parser = new Parser(Json);
            ArrayList<Currency> Rates = parser.Cparse();

            Double Min = 1000.0;
            String MinName=null;
            for(int i=0; i< Rates.size(); i++){
                Rates.get(i).Diff = Rates.get(i).Ask - Rates.get(i).Bid;
            }

            Collections.sort(Rates, new Comparator<Currency>() {
                @Override
                public int compare(Currency c2, Currency c1)
                {

                    return  -1*(c1.Diff.compareTo(c2.Diff));
                }
            });

            for(int i=0; i<n; i++){
                System.out.println(Rates.get(i).name + "    " + Rates.get(i).Diff);
            }
        }


        if(arg[0].equals("-f")){
            String StartDate = arg[1];
            String EndDate = arg[2];
            String code = arg[3];

            DateHandler DH = new DateHandler(StartDate,EndDate);
            ArrayList Intervals  = new ArrayList();
            Intervals = DH.getIntervals();
            ArrayList<Double> prices = new ArrayList<>();

            ArrayList<ArrayList<Currency>> Concat = new ArrayList<>();
            for(int i = 0; i<Intervals.size(); i++){
                if(i+1<Intervals.size()){
                    String URL = "http://api.nbp.pl/api/exchangerates/rates/A/" + code + "/" + Intervals.get(i) + "/" + Intervals.get(i+1)  + "/?format=json";
                    HttpRouter router = new HttpRouter();
                    String Json = router.call(URL);
                    Parser parser = new Parser(Json);
                    ArrayList<Currency> Rate = parser.Dparse();
                    Concat.add(Rate);
                }
            }

            ArrayList<Currency> Prices = new ArrayList();
            for(ArrayList<Currency> R : Concat){
                for(Currency C : R){
                    Prices.add(C);
                }
            }


            Calculator cl = new Calculator();

            String MinDate,MaxDate = null;
            Double min = 1000.0;
            Double max = 0.0;

            Currency MinC, MaxC;
            MinC = cl.min(Prices);
            MaxC = cl.max(Prices);

            MinDate = MinC.date;
            MaxDate = MaxC.date;

            System.out.println("Waluta " + MinC.name + " była najtańsza w " + MinDate + " a najdrożdza w " + MaxDate);

        }

        if(arg[0].equals("-g")){
            //String StartDate = arg[1]+arg[2]+arg[3];
            //String EndDate = arg[4]+arg[5]+arg[6];
            //String code = arg[7];
            String StartDate = arg[1];
            String EndDate = arg[2];
            String code = arg[3];

            String StartDateedit, EndDateedit;
            StartDateedit = StartDate.replace(',', '-');
            EndDateedit = EndDate.replace(',','-');

            LocalDate DateS, DateE;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-F");

            //DateS = LocalDate.parse(StartDateedit,formatter);
            //DateE = LocalDate.parse(StartDateedit,formatter);
            //System.out.println(DateS + " " + DateE);

            DateHandler DH = new DateHandler(StartDate,EndDate);
            ArrayList Intervals  = new ArrayList();
            Intervals = DH.getIntervals();
            ArrayList<Double> prices = new ArrayList<>();

            ArrayList<ArrayList<Currency>> Concat = new ArrayList<>();
            for(int i = 0; i<Intervals.size(); i++){
                if(i+1<Intervals.size()){
                    String URL = "http://api.nbp.pl/api/exchangerates/rates/A/" + code + "/" + Intervals.get(i) + "/" + Intervals.get(i+1)  + "/?format=json";
                    HttpRouter router = new HttpRouter();
                    String Json = router.call(URL);
                    Parser parser = new Parser(Json);
                    ArrayList<Currency> Rate = parser.Dparse();
                    Concat.add(Rate);
                }
            }

            ArrayList<Currency> Prices = new ArrayList();
            for(ArrayList<Currency> R : Concat){
                for(Currency C : R){
                    Prices.add(C);
                }
            }

            System.out.println("Wykres zmiany kursu w widoku tygodniowym dla waluty " + Prices.get(1).name);
            System.out.println("W okresie od " + StartDate + " do " + EndDate);
            System.out.println();
            Grapher graph = new Grapher();
            graph.write(Prices);

        }


    }
}
