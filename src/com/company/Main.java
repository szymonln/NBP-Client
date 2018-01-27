package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

/**
 * Przekazuje do klasy EventParser tabelę z argumentami lini komend.
 */

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        if(args.length == 0){
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\kubsz\\IdeaProjects\\NBP\\src\\com\\company\\help.txt"));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        else {

            EventParser Event = new EventParser(args);
            Event.run();
            DateHandler DH = new DateHandler("2018-01-15");

            /*HttpRouter router = new HttpRouter();
            String test;
            test = router.call("http://api.nbp.pl/api/exchangerates/rates/A/USD/2018-01-01/2018-01-11/?format=json");
            System.out.println(test);

            Parser p = new Parser(test);

            //p.parse();

            DateHandler DH = new DateHandler("2017-01-27", "2018-02-12");
            System.out.println(DH.getIntervals());
            */
        }

    }
}
