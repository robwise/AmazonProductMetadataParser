package drexel.info634.amazon.parser;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles identification and parsing of values from a given line of data. Created by Rob on 4/15/2015.
 */
public class LineParser {
  static Pattern id                       = Pattern.compile("Id:\\s{3}(.*)");
  static Pattern asin                     = Pattern.compile("ASIN:\\s(.*)");
  static Pattern title                    = Pattern.compile("\\s{2}title:\\s(.*)");
  static Pattern group                    = Pattern.compile("\\s{2}group:\\s(.*)");
  static Pattern salesrank                = Pattern.compile("\\s{2}salesrank:\\s(.*)");
  static Pattern similar                  = Pattern.compile("\\s{2}similar:\\s(.*)");
  static Pattern similarCount             = Pattern.compile("\\s{2}similar:\\s(\\d+).*");
  static Pattern similarItemsDelimiter    = Pattern.compile("\\s{2}");
  static Pattern categories               = Pattern.compile("\\s{2}categories:\\s(.*)");
  static Pattern categoriesItemsDelimiter = Pattern.compile("(\\u007C)");
  static Pattern categoriesItemName       = Pattern.compile("(.*)\\[.*\\]");
  static Pattern categoriesItemID         = Pattern.compile(".*\\[(.*)\\]");
  static Pattern reviews                  = Pattern.compile("\\s{2}reviews:\\s.*");
  static Pattern reviewsTotal             = Pattern.compile("\\s{2}reviews:\\stotal:\\s(\\S+).*");
  static Pattern
                 reviewsDownloaded
                                          = Pattern.compile("\\s{2}reviews:\\stotal:\\s\\S+\\s{2}downloaded:\\s(\\S+)"
                                                            + ".*");
  static Pattern
                 reviewsDownloadAvgRating
                                          = Pattern.compile("\\s{2}reviews:\\stotal:\\s\\S+\\s{2}downloaded:\\s\\S+"
                                                            + "\\s{2}avg\\srating:\\s(\\S+)");
  static Pattern reviewsItemDate          = Pattern.compile("\\s{4}(\\d{4}-\\d{1,2}-\\d{1,2}).*");
  static Pattern reviewsItemCustomer      = Pattern.compile("\\s{4}\\d{4}-\\d{1,2}-\\d{1,2}\\s{2}cutomer:\\s(\\S+).*");
  static Pattern reviewsItemRating        = Pattern.compile("\\s{4}\\d{4}-\\d{1,2}-\\d{1,"
                                                            + "2}\\s{2}cutomer:\\s\\S+\\s{2}rating:\\s(\\S+).*");
  static Pattern reviewsItemVotes         = Pattern.compile("\\s{4}\\d{4}-\\d{1,2}-\\d{1,"
                                                            + "2}\\s{2}cutomer:\\s\\S+\\s{2}rating:\\s\\S+\\s{2}"
                                                            + "votes:\\s{2,3}(\\S+).*");
  static Pattern reviewsItemHelpful       = Pattern.compile("\\s{4}\\d{4}-\\d{1,2}-\\d{1,"
                                                            + "2}\\s{2}cutomer:\\s\\S+\\s{2}rating:\\s\\S+\\s{2}"
                                                            + "votes:\\s{2,3}\\S+\\s{2}helpful:\\s{2,3}(\\S+)");
  static Pattern discontinuedProduct      = Pattern.compile("\\s{2}discontinued\\sproduct");

  static boolean isID(String line) {
    return id.matcher(line).matches();
  }

  static String parseID(String line) {
    return parseLine(line, id).group(1);
  }

  static boolean isASIN(String line) {
    return asin.matcher(line).matches();
  }

  static String parseASIN(String line) {
    return parseLine(line, asin).group(1);
  }

  static boolean isTitle(String line) {
    return title.matcher(line).matches();
  }

  static String parseTitle(String line) {
    return parseLine(line, title).group(1);
  }

  static boolean isGroup(String line) {
    return group.matcher(line).matches();
  }

  static String parseGroup(String line) {
    return parseLine(line, group).group(1);
  }

  static boolean isSalesrank(String line) {
    return salesrank.matcher(line).matches();
  }

  static String parseSalesrank(String line) {
    return parseLine(line, salesrank).group(1);
  }

  static boolean isSimilar(String line) {
    return similar.matcher(line).matches();
  }

  static String parseSimilarCount(String line) {
    return parseLine(line, similarCount).group(1);
  }

  static String[] parseSimilarItems(String line) {
    int startOfSimilarItems = parseLine(line, similarCount).end(1) + 2;
    String similarItems = line.substring(startOfSimilarItems, line.length());
    return similarItemsDelimiter.split(similarItems);
  }

  static boolean isCategories(String line) {
    return categories.matcher(line).matches();
  }

  static String parseCategoriesCount(String line) {
    return parseLine(line, categories).group(1);
  }

  static String[] parseCategoriesItems(String line) {
    Matcher matcher = categoriesItemsDelimiter.matcher(line);
    //noinspection ResultOfMethodCallIgnored
    matcher.find();
    int indexOfCategoriesItemsStart = matcher.end(1);
    String trimmedLine = line.substring(indexOfCategoriesItemsStart, line.length());
    return categoriesItemsDelimiter.split(trimmedLine);
  }

  static String parseCategoriesItemName(String categoriesItem) {
    return parseLine(categoriesItem, categoriesItemName).group(1);
  }

  static String parseCategoriesItemID(String line) {
    return parseLine(line, categoriesItemID).group(1);
  }

  static boolean isReviews(String line) {
    return reviews.matcher(line).matches();
  }

  static String parseReviewsTotal(String line) {
    return parseLine(line, reviewsTotal).group(1);
  }

  static String parseReviewsDownloaded(String line) {
    return parseLine(line, reviewsDownloaded).group(1);
  }

  static String parseReviewsAvgRating(String line) {
    return parseLine(line, reviewsDownloadAvgRating).group(1);
  }

  static String parseReviewsItemDate(String line) {
    return parseLine(line, reviewsItemDate).group(1);
  }

  static String parseReviewsItemCustomer(String line) {
    return parseLine(line, reviewsItemCustomer).group(1);
  }

  static String parseReviewsItemRating(String line) {
    return parseLine(line, reviewsItemRating).group(1);
  }

  static String parseReviewsItemVotes(String line) {
    return parseLine(line, reviewsItemVotes).group(1);
  }

  static String parseReviewsItemHelpful(String line) {
    return parseLine(line, reviewsItemHelpful).group(1);
  }

  static boolean isDiscontinuedProduct(String line) {
    return discontinuedProduct.matcher(line).matches();
  }

  // *************PRIVATE METHODS*****************

  static boolean isBlankLine(String line) {
    return line != null && line.isEmpty();
  }

  private static MatchResult parseLine(String line, Pattern pattern) {
    Matcher matcher = pattern.matcher(line);
    //noinspection ResultOfMethodCallIgnored
    matcher.matches(); // Necessary to set the match groups
    return matcher.toMatchResult();
  }
}
