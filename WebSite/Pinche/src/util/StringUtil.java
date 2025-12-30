package util;

public class StringUtil {
	  private static int counter = 0;  
	    public static void main(String[] args) {  
	         String str1 = "sd& ^^java***java(((java";  
	         String str2 = "java";  
	         int i = countStr(str1, str2);  
	         System.out.println("i---------->" + i);  
	    }  
	  
	    /** 
	     * 判断str1中包含str2的个数 
	      * @param str1 
	     * @param str2 
	     * @return counter 
	     */  
	    public static int countStr(String str1, String str2) {  
	        if (str1.indexOf(str2) == -1) {  
	            return 0;  
	        } else if (str1.indexOf(str2) != -1) {  
	            counter++;  
	            countStr(str1.substring(str1.indexOf(str2)+str2.length()), str2);  
	               return counter;  
	        }  
	            return 0;  
	    }  
}
