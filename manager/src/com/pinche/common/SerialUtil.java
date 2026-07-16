package com.pinche.common;

import java.util.Random;

public class SerialUtil {

	private static KeyValueString[] C_NUMBER_CODE_ARRAY = new KeyValueString[] {
        new KeyValueString("0", "A"),
        new KeyValueString("1", "B"),
        new KeyValueString("2", "C"),
        new KeyValueString("3", "D"),
        new KeyValueString("4", "E"),
        new KeyValueString("5", "F"),
        new KeyValueString("6", "G"),
        new KeyValueString("7", "H"),
        new KeyValueString("8", "I"),
        new KeyValueString("9", "J"),
        new KeyValueString("00", "A"),
        new KeyValueString("01", "B"),
        new KeyValueString("02", "C"),
        new KeyValueString("03", "D"),
        new KeyValueString("04", "E"),
        new KeyValueString("05", "F"),
        new KeyValueString("06", "G"),
        new KeyValueString("07", "H"),
        new KeyValueString("08", "I"),
        new KeyValueString("09", "J"),
        new KeyValueString("10", "K"),
        new KeyValueString("11", "L"),
        new KeyValueString("12", "M"),
        new KeyValueString("13", "O"),
        new KeyValueString("14", "P"),
        new KeyValueString("15", "Q"),
        new KeyValueString("16", "R"),
        new KeyValueString("17", "S"),
        new KeyValueString("18", "T"),
        new KeyValueString("19", "U"),
        new KeyValueString("20", "V"),
        new KeyValueString("21", "W"),
        new KeyValueString("22", "X"),
        new KeyValueString("23", "Y"),
        new KeyValueString("24", "Z"),
        new KeyValueString("25", "0"),
        new KeyValueString("26", "1"),
        new KeyValueString("27", "2"),
        new KeyValueString("28", "3"),
        new KeyValueString("29", "4"),
        new KeyValueString("30", "5"),
        new KeyValueString("31", "6"),
        new KeyValueString("32", "7"),
        new KeyValueString("32", "8"),
        new KeyValueString("33", "9"),
        new KeyValueString("34", "a"),
        new KeyValueString("35", "b"),
        new KeyValueString("36", "c"),
        new KeyValueString("37", "d"),
        new KeyValueString("38", "e"),
        new KeyValueString("39", "f"),
        new KeyValueString("40", "g"),
        new KeyValueString("41", "h"),
        new KeyValueString("42", "i"),
        new KeyValueString("43", "j"),
        new KeyValueString("44", "k"),
        new KeyValueString("45", "l"),
        new KeyValueString("46", "m"),
        new KeyValueString("47", "n"),
        new KeyValueString("48", "o"),
        new KeyValueString("49", "p"),
        new KeyValueString("50", "q"),
        new KeyValueString("51", "r"),
        new KeyValueString("52", "s"),
        new KeyValueString("53", "t"),
        new KeyValueString("54", "u"),
        new KeyValueString("55", "v"),
        new KeyValueString("56", "w"),
        new KeyValueString("57", "x"),
        new KeyValueString("58", "y"),
        new KeyValueString("59", "z"),
    };
	
	// return format : HFHD-72DJ-99PP
	public static String generateSerialNo() {
		
		String firstId = "";
		String secondId = "";
		String thirdId = "";
		
		String currentDateTime = Common.getCurrentDateTimeString();
        
        String[] currentDateArray = currentDateTime.substring(0, currentDateTime.indexOf(" ")).split("-");
        String[] currentTimeArray = currentDateTime.substring(currentDateTime.indexOf(" ") + 1).split(":");
        
        for(KeyValueString kvs : C_NUMBER_CODE_ARRAY) {
            if(currentDateArray[1].equals(kvs.getKey())) {
            	firstId += kvs.getValue();
            }
        }
        
        for(KeyValueString kvs : C_NUMBER_CODE_ARRAY) {
            if(currentDateArray[2].equals(kvs.getKey())) {
            	firstId += kvs.getValue();
            }
        }
        
        for(KeyValueString kvs : C_NUMBER_CODE_ARRAY) {
            if(currentTimeArray[0].equals(kvs.getKey())) {
            	firstId += kvs.getValue();
            }
        }
        
        for(KeyValueString kvs : C_NUMBER_CODE_ARRAY) {
            if(currentTimeArray[1].substring(0, 1).equals(kvs.getKey())) {
            	firstId += kvs.getValue();
            }
        }
        
        for(KeyValueString kvs : C_NUMBER_CODE_ARRAY) {
            if(currentTimeArray[1].substring(1, 2).equals(kvs.getKey())) {
            	secondId += kvs.getValue();
            }
        }
        
        for(KeyValueString kvs : C_NUMBER_CODE_ARRAY) {
            if(currentTimeArray[2].substring(0, 1).equals(kvs.getKey())) {
            	secondId += kvs.getValue();
            }
        }
        
        for(KeyValueString kvs : C_NUMBER_CODE_ARRAY) {
            if(currentTimeArray[2].substring(1, 2).equals(kvs.getKey())) {
            	secondId += kvs.getValue();
            }
        }
        
        Random random = new Random();
        
        secondId += C_NUMBER_CODE_ARRAY[random.nextInt(34)].getValue();
        
        thirdId += C_NUMBER_CODE_ARRAY[random.nextInt(34)].getValue();
        thirdId += C_NUMBER_CODE_ARRAY[random.nextInt(34)].getValue();
        thirdId += C_NUMBER_CODE_ARRAY[random.nextInt(34)].getValue();
        thirdId += C_NUMBER_CODE_ARRAY[random.nextInt(34)].getValue();
        
        return firstId + "-" + secondId + "-" + thirdId;
	}
}
