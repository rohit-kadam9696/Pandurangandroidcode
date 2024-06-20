package com.twd.chitboyapp.spsskl.constant;

public class MarathiHelper {
    public static String convertMarathitoEnglish(String mystring) {
        mystring = mystring.replaceAll("०", "0");
        mystring = mystring.replaceAll("१", "1");
        mystring = mystring.replaceAll("२", "2");
        mystring = mystring.replaceAll("३", "3");
        mystring = mystring.replaceAll("४", "4");
        mystring = mystring.replaceAll("५", "5");
        mystring = mystring.replaceAll("६", "6");
        mystring = mystring.replaceAll("७", "7");
        mystring = mystring.replaceAll("८", "8");
        mystring = mystring.replaceAll("९", "9");
        return mystring;
    }

    public static String convertEnglishtoMarathi(String mystring) {
        mystring = mystring.replaceAll("0", "०");
        mystring = mystring.replaceAll("1", "१");
        mystring = mystring.replaceAll("2", "२");
        mystring = mystring.replaceAll("3", "३");
        mystring = mystring.replaceAll("4", "४");
        mystring = mystring.replaceAll("5", "५");
        mystring = mystring.replaceAll("6", "६");
        mystring = mystring.replaceAll("7", "७");
        mystring = mystring.replaceAll("8", "८");
        mystring = mystring.replaceAll("9", "९");
        return mystring;
    }

}
