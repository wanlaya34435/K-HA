package com.zti.kha.utility;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BreakIteratorUtils {
 
   public static List<String> extractWords(String target, BreakIterator wordIterator) {
 
      wordIterator.setText(target);
      int start = wordIterator.first();
      int end = wordIterator.next();
      List<String> list = new ArrayList<>();
 
      while (end != BreakIterator.DONE) {
         String word = target.substring(start,end);
         if (Character.isLetterOrDigit(word.charAt(0))) {
           list.add(word);
         }
         start = end;
         end = wordIterator.next();
      }
      return list;
   }

   public static  List<String> extractWords(String target){

      Locale currentLocale = new Locale ("th","Th");
      BreakIterator wordIterator =
              BreakIterator.getWordInstance(currentLocale);
      return extractWords(target,wordIterator);
   }
 
   static void reverseWords(String target, BreakIterator wordIterator) {
 
      wordIterator.setText(target);
      int end = wordIterator.last();
      int start = wordIterator.previous();
 
      while (start != BreakIterator.DONE) {
         String word = target.substring(start,end);
         if (Character.isLetterOrDigit(word.charAt(0)))
            System.out.println(word);
         end = start;
         start = wordIterator.previous();
      }
   } 
 
   static void markBoundaries(String target, BreakIterator iterator) {
 
      StringBuffer markers = new StringBuffer();
      markers.setLength(target.length() + 1);
      for (int k = 0; k < markers.length(); k++) {
         markers.setCharAt(k,' ');
      }
 
      iterator.setText(target);
      int boundary = iterator.first();
 
      while (boundary != BreakIterator.DONE) {
         markers.setCharAt(boundary,'^');
         boundary = iterator.next();
      }
 
      System.out.println(target);
      System.out.println(markers);
   } 
 
   static void formatLines(String target, int maxLength, 
                           Locale currentLocale) {
 
      BreakIterator boundary = BreakIterator.getLineInstance(currentLocale);
      boundary.setText(target);
      int start = boundary.first();
      int end = boundary.next();
      int lineLength = 0;
 
      while (end != BreakIterator.DONE) {
         String word = target.substring(start,end);
         lineLength = lineLength + word.length();
         if (lineLength >= maxLength) {
            System.out.println();
            lineLength = word.length();
         }
         System.out.print(word);
         start = end;
         end = boundary.next();
      }
   } 
 
   static void listPositions(String target, BreakIterator iterator) {
 
      iterator.setText(target);
      int boundary = iterator.first();
 
      while (boundary != BreakIterator.DONE) {
         System.out.println (boundary);
         boundary = iterator.next();
      }
   } 
 
   static void characterExamples() {
 
      BreakIterator arCharIterator =
         BreakIterator.getCharacterInstance(new Locale ("ar","SA"));
      // Arabic word for "house"
      String house = "\u0628" + "\u064e" + "\u064a" + 
                     "\u0652" + "\u067a" + "\u064f";
      listPositions (house,arCharIterator);
   }
 
   static void wordExamples() {
 
      Locale currentLocale = new Locale ("th","Th");
      BreakIterator wordIterator = 
         BreakIterator.getWordInstance(currentLocale);
      String someText = "เมื่อเวลา 13.00 น. วันที่ 6 มิ.ย. เกิดพายุฝนกระหน่ำในพื้นที่ อ.ปากเกร็ด จ.นนทบุรี เป็นวงกว้างนานกว่า 1 ชม. ส่งผลให้ถนนแจ้งวัฒนะช่วงบริเวณใต้ทางด่วนแจ้\n" +
              "\n" +
              "อ่านข่าวต่อได้ที่: http://www.thairath.co.th/content/964455";
      markBoundaries(someText, wordIterator);
      System.out.println();
      extractWords(someText, wordIterator);
   }
 
   static void sentenceExamples() {
 
      Locale currentLocale = new Locale ("en","US");
      BreakIterator sentenceIterator = 
         BreakIterator.getSentenceInstance(currentLocale);
      String someText = "She stopped.  " +
                        "She said, \"Hello there,\" and then went on.";
      markBoundaries(someText, sentenceIterator);
      String variousText = "He's vanished!  " +
                           "What will we do?  It's up to us.";
      markBoundaries(variousText, sentenceIterator);
      String decimalText = "Please add 1.5 liters to the tank.";
      markBoundaries(decimalText, sentenceIterator);
      String  donneText = "\"No man is an island . . . " +
                          "every man . . . \"";
      markBoundaries(donneText, sentenceIterator);
      String dogText = "My friend, Mr. Jones, has a new dog.  " +
                       "The dog's name is Spot.";
      markBoundaries(dogText, sentenceIterator);
   }
 
   static void lineExamples() {
 
      Locale currentLocale = new Locale ("en","US");
      BreakIterator lineIterator = 
        BreakIterator.getLineInstance(currentLocale);
      String someText = "She stopped.  " +
                        "She said, \"Hello there,\" and then went on.";
      markBoundaries(someText, lineIterator);
      String hardHyphen = "There are twenty-four hours in a day.";
      markBoundaries(hardHyphen, lineIterator);
      System.out.println();
      String moreText = "She said, \"Hello there,\" and then " +
                        "went on down the street.  When she stopped " +
                        "to look at the fur coats in a shop window, " +
                        "her dog growled.  \"Sorry Jake,\" she said. " +
                        " \"I didn't know you would take it personally.\"";
      formatLines(moreText, 30, currentLocale);
      System.out.println();
   }
 
   static public void main(String[] args) {
 
      characterExamples();
      System.out.println();
      wordExamples();
      System.out.println();
      sentenceExamples();
      System.out.println();
      lineExamples();
   }
 
} // class