package lcas.project;

import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {

  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();

  // Update this with your Repository Name
  private static final String ABSOLUTE_PATH = "/workspaces/reviews-and-sentiment-value-lab-BCharen/src/main/resources/";

  static {
    try {
      Scanner input = new Scanner(new File(ABSOLUTE_PATH + "cleanSentiment.csv"));
      while (input.hasNextLine()) {
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0], Double.parseDouble(temp[1]));
        // System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    } catch (Exception e) {
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }

    // read in the positive adjectives in postiveAdjectives.txt
    try {
      Scanner input = new Scanner(new File(ABSOLUTE_PATH + "positiveAdjectives.txt"));
      while (input.hasNextLine()) {
        String temp = input.nextLine().trim();
        posAdjectives.add(temp);
      }
      input.close();
    } catch (Exception e) {
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }

    // read in the negative adjectives in negativeAdjectives.txt
    try {
      Scanner input = new Scanner(new File(ABSOLUTE_PATH + "negativeAdjectives.txt"));
      while (input.hasNextLine()) {
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    } catch (Exception e) {
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }
  }

  /**
   * returns a string containing all of the text in fileName (including
   * punctuation),
   * with words separated by a single space
   */
  public static String textToString(String fileName) {
    String temp = "";
    try {
      Scanner input = new Scanner(new File(ABSOLUTE_PATH + fileName));

      // add 'words' in the file to the string, separated by a single space
      while (input.hasNext()) {
        temp = temp + input.next() + " ";
      }
      input.close();

    } catch (Exception e) {
      System.out.println("Unable to locate " + fileName);
    }
    // make sure to remove any additional space that may have been added at the end
    // of the string.
    return temp.trim();
  }

  /**
   * @returns the sentiment value of word as a number between -1 (very negative)
   *          to 1 (very positive sentiment)
   */
  public static double sentimentVal(String word) {
    try {
      return sentiment.get(word.toLowerCase());
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Returns the ending punctuation of a string, or the empty string if there is
   * none
   */
  public static String getPunctuation(String word) {
    String punc = "";
    for (int i = word.length() - 1; i >= 0; i--) {
      if (!Character.isLetterOrDigit(word.charAt(i))) {
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }

  /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation(String word) {
    while (word.length() > 0 && !Character.isAlphabetic(word.charAt(0))) {
      word = word.substring(1);
    }
    while (word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length() - 1))) {
      word = word.substring(0, word.length() - 1);
    }

    return word;
  }

  /**
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and
   * returns it.
   */
  public static String randomPositiveAdj() {
    int index = (int) (Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }

  /**
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and
   * returns it.
   */
  public static String randomNegativeAdj() {
    int index = (int) (Math.random() * negAdjectives.size());
    return negAdjectives.get(index);

  }

  /**
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective() {
    boolean positive = Math.random() < .5;
    if (positive) {
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }

  public static double totalSentiment(String fileName) {
    double sentiment = 0;
    int index = 0;
    int startWord = 0;
    try {
      String fileText = textToString(fileName);
      while (index < fileText.length()) {
        if (fileText.charAt(index) == ' ') {
          sentiment += sentimentVal(removePunctuation(fileText.substring(startWord, index)));
          startWord = index;
        }
        index++;
      }
      sentiment += sentimentVal(removePunctuation(fileText.substring(startWord, index)));

    } catch (Exception e) {
      System.err.println("Error in totalSentiment");
    }
    return sentiment;
  }

  public static int starRating(String fileName) {
    double sentiment = 0;
    int index = 0;
    int startWord = 0;
    int words = 0;
    int rating = 0;
    try {
      String fileText = textToString(fileName);
      while (index < fileText.length()) {
        if (fileText.charAt(index) == ' ') {
          sentiment += sentimentVal(removePunctuation(fileText.substring(startWord, index)));
          startWord = index + 1;
          words++;
        }
        index++;
      }
      sentiment += sentimentVal(removePunctuation(fileText.substring(startWord, index)));

    } catch (Exception e) {
      System.err.println("StarRatingError");
    }
    rating = (int) (sentiment / words * 40);
    if (rating <= 0) {
      rating = 1;
    }
    return rating;
  }

  public static String fakeReview(String fileName) {
    String fileText = textToString(fileName);
    int randNum = (int) (Math.random() * 2);
    String adjective = "";
    int startIndex = fileText.indexOf("\'") + 1;
    int index = startIndex;

    while (index < fileText.length() && startIndex != -1) {
      if (!Character.isLetter(fileText.charAt(index))) {
        adjective = randomAdjective(randNum);
        fileText = fileText.replaceFirst(fileText.substring(startIndex, index), adjective);
        randNum = (int) (Math.random() * 2);
        startIndex = fileText.indexOf("\'");
        index = startIndex;
      }
      index++;
    }

    return fileText;
  }

  public static String fakeStrongPositiveReview(String fileName) {
    String fileText = textToString(fileName);
    String adjective = "";
    int startIndex = fileText.indexOf("\'");
    int index = startIndex+1;

    while (index < fileText.length() && startIndex != -1) {
      if (!Character.isLetter(fileText.charAt(index))) {
        adjective = randomAdjective(0);
        adjective = "very " + adjective;
        System.out.println(fileText);
        System.out.println(fileText.substring(startIndex, index));
        fileText = fileText.replaceFirst(fileText.substring(startIndex, index), adjective);
        startIndex = fileText.indexOf("\'");
        index = startIndex;
      }
      index++;
    }

    return fileText;
  }

  public static String randomAdjective(int input) {
    String adjective;
    if (input % 2 == 0) {
      adjective = randomPositiveAdj();
    } else {
      adjective = randomNegativeAdj();
    }
    if (adjective == null || adjective.equals("") || adjective.equals(" ")) {
      adjective = randomAdjective(input);
    }
    return adjective;
  }
}
