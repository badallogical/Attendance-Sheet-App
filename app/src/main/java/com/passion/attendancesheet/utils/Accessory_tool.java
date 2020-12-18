package com.passion.attendancesheet.utils;

public class Accessory_tool {

    public static int getIntFromRoman( String roman ){
        switch ( roman ){
            case "I": return 1;
            case "II": return 2;
            case "III": return 3;
            case "IV": return 4;
            case "V": return 5;
            case "VI": return 6;
            case "VII": return 7;
            case "VIII": return 8;
            default: return 0;
        }
    }

    public static String getRomanFromInt( int num ){
        switch ( num ){
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            default: return " ";
        }
    }

}
