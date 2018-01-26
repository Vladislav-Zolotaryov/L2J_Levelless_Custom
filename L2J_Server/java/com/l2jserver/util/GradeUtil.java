package com.l2jserver.util;

public class GradeUtil {

  /*
  01-19 (nograde)
  20-39(D Grade)
  40-51(C Grade)
  52-60(B grade)
  61-75(A grade)
  76-79(S grade)
  80-83(S80 grade)
  84-85(S84 grade)
   */

  public static String resolveGrade(int level) {
    if (level >= 1 && level < 20) {
      return "NG";
    } else if (level >= 20 && level < 40){
      return "D";
    } else if (level >= 40 && level < 52){
      return "C";
    } else if (level >= 52 && level < 61){
      return "B";
    } else if (level >= 61 && level < 76){
      return "A";
    } else if (level >= 76 && level < 80){
      return "S";
    } else if (level >= 80){
      return "S+";
    } else {
      return "UNKNOWN";
    }
  }

}
