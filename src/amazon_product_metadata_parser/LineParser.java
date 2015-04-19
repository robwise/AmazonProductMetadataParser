package amazon_product_metadata_parser;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles identification and parsing of values from a given line of data. Created by Rob on
 * 4/15/2015.
 */
class LineParser {

  static final Pattern id                         = Pattern.compile("Id:\\s{3}(.*)");
  static final Pattern asin                       = Pattern.compile("ASIN:\\s(.*)");
  static final Pattern title                      = Pattern.compile("\\s{2,3}title:\\s(.*)");
  static final Pattern group                      = Pattern.compile("\\s{2}group:\\s(.*)");
  static final Pattern salesrank                  = Pattern.compile("\\s{2}salesrank:\\s(.*)");
  static final Pattern similar                    = Pattern.compile("\\s{2}similar:\\s(.*)");
  static final Pattern similarCount               = Pattern.compile("\\s{2}similar:\\s(\\d+).*");
  static final Pattern similarItems               = Pattern.compile("\\s{2}similar:\\s\\d+(.*)");
  static final Pattern similarItemsDelimiter      = Pattern.compile("\\s{2}");
  static final Pattern categories                 = Pattern.compile("\\s{2}categories:\\s(.*)");
  static final Pattern categoriesItemsDelimiter   = Pattern.compile("(\\u007C)");
  static final Pattern categoriesItemName         = Pattern.compile("(.*)\\[.*\\]");
  static final Pattern categoriesItemID           = Pattern.compile(".*\\[(.*)\\]");
  static final Pattern reviews                    = Pattern.compile("\\s{2}reviews:\\s.*");
  static final Pattern reviewsTotal               = Pattern.compile(
      "\\s{2}reviews:\\stotal:\\s(\\S+).*");
  static final Pattern reviewsDownloaded          = Pattern.compile("\\s{2}reviews:\\stotal:\\s\\S+"
                                                     + "\\s{2}downloaded:\\s(\\S+).*");
  static final Pattern reviewsDownloadedAvgRating = Pattern.compile("\\s{2}reviews:\\stotal:\\s\\S+"
                                                            + "\\s{2}downloaded:\\s\\S+"
                                                                    + "\\s{2}avg\\srating:\\s"
                                                                    + "(\\S+)");
  static final Pattern reviewsItemDate            = Pattern.compile(
      "\\s{4}(\\d{4}-\\d{1,2}-\\d{1,2}).*");
  static final Pattern reviewsItemCustomer        = Pattern.compile("\\s{4}\\d{4}-\\d{1,2}-\\d{1,2}"
                                                                    + "\\s{2}cutomer:\\s+(\\S+).*");
  static final Pattern reviewsItemRating          = Pattern.compile("\\s{4}\\d{4}-\\d{1,2}-\\d{1,2}"
                                                                    + "\\s{2}cutomer:\\s+\\S+"
                                                                    + "\\s{2}rating:\\s(\\S+).*");
  static final Pattern reviewsItemVotes           = Pattern.compile("\\s{4}\\d{4}-\\d{1,2}-\\d{1,2}"
                                                                    + "\\s{2}cutomer:\\s+\\S+"
                                                                    + "\\s{2}rating:\\s\\S+"
                                                                    + "\\s{2}votes:\\s{1,3}(\\S+)"
                                                                    + ".*");
  static final Pattern reviewsItemHelpful         = Pattern.compile("\\s{4}\\d{4}-\\d{1,2}-\\d{1,2}"
                                                                    + "\\s{2}cutomer:\\s+\\S+"
                                                                    + "\\s{2}rating:\\s\\S+"
                                                                    + "\\s{2}votes:\\s{1,3}\\S+"
                                                                    + "\\s{2}helpful:\\s{1,3}"
                                                                    + "(\\S+)");
  static final Pattern discontinuedProduct        = Pattern.compile("\\s{2}discontinued\\sproduct");

  static boolean isID(String line) {
    return id.matcher(line).matches();
  }

  static String parseID(String line) {
    return parseLine(line, id).group(1);
  }

  private static MatchResult parseLine(String line, Pattern pattern) {
    Matcher matcher = pattern.matcher(line);
    //noinspection ResultOfMethodCallIgnored
    matcher.matches(); // Necessary to set the match groups
    return matcher.toMatchResult();
  }

  static boolean isASIN(String line) {
    return asin.matcher(line).matches();
  }

  static String parseASIN(String line) {
    return parseLine(line, asin).group(1);
  }

  static boolean isTitle(String line) {
    return title.matcher(line).find();
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
//    int startOfSimilarItems = parseLine(line, similarCount).end(1) + 2;
//    String similarItems = line.substring(startOfSimilarItems, line.length());
    String items = parseLine(line, similarItems).group(1);
    items = items.trim();
    return similarItemsDelimiter.split(items);
  }

  static boolean isCategories(String line) {
    return categories.matcher(line).matches();
  }

  static String parseCategoriesCount(String line) {
    return parseLine(line, categories).group(1);
  }

  static String[] parseCategoryItems(String line) {
    Matcher matcher = categoriesItemsDelimiter.matcher(line);
    //noinspection ResultOfMethodCallIgnored
    matcher.find();
    int indexOfCategoriesItemsStart = matcher.end(1);
    String trimmedLine = line.substring(indexOfCategoriesItemsStart, line.length());
    return categoriesItemsDelimiter.split(trimmedLine);
  }

  static String parseCategoryItemName(String categoriesItem) {
    return parseLine(categoriesItem, categoriesItemName).group(1);
  }

  static String parseCategoryItemID(String line) {
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
    return parseLine(line, reviewsDownloadedAvgRating).group(1);
  }

  static String parseReviewDate(String line) {
    return parseLine(line, reviewsItemDate).group(1);
  }

  static String parseReviewCustomer(String line) {
    return parseLine(line, reviewsItemCustomer).group(1);
  }

  static String parseReviewRating(String line) {
    return parseLine(line, reviewsItemRating).group(1);
  }

  static String parseReviewVotes(String line) {
    return parseLine(line, reviewsItemVotes).group(1);
  }

  static String parseReviewHelpful(String line) {
    return parseLine(line, reviewsItemHelpful).group(1);
  }

  // *************PRIVATE METHODS*****************

  static boolean isDiscontinuedProduct(String line) {
    return discontinuedProduct.matcher(line).matches();
  }

  static boolean isBlankLine(String line) {
    return line != null && line.isEmpty();
  }
}
