package com.company;

import java.util.Date;
import java.util.HashMap;

/**
 * Klasa dla obiektu kursu waluty lub złota z jednego dnia.
 * Przechowuje hashmapę pól obecnych w JsonArray.
 */

public class Currency {
    String name, code, date;
    Double Value, Ask, Bid, Diff;
    HashMap<String,String> Rates = new HashMap<>();


}
