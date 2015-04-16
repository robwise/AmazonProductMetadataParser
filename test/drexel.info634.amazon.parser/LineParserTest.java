package drexel.info634.amazon.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Rob on 4/15/2015.
 */
public class LineParserTest {
  static final String[] sampleData = {
    "Id:   1",
    "ASIN: 0827229534",
    "  title: Patterns of Preaching: A Sermon Sampler",
    "  group: Book",
    "  salesrank: 396585",
    "  similar: 5  0804215715  156101074X  0687023955  0687074231  082721619X",
    "  categories: 2",
    "   |Books[283155]|Subjects[1000]|Religion & Spirituality[22]|Christianity[12290]|Clergy[12360]|Preaching[12368]",
    "   |Books[283155]|Subjects[1000]|Religion & Spirituality[22]|Christianity[12290]|Clergy[12360]|Sermons[12370]",
    "  reviews: total: 2  downloaded: 2  avg rating: 5",
    "    2000-7-28  cutomer: A2JW67OY8U6HHK  rating: 5  votes:  10  helpful:   9",
    "    2003-12-14  cutomer: A2VE83MZF98ITY  rating: 5  votes:   6  helpful:   5",
    "",
    "  discontinued product"
  };

  @Test
  public void testIsID() throws Exception {
    boolean isID = LineParser.isID(sampleData[0]);
    assertTrue(isID);
  }

  @Test
  public void testParseID() throws Exception {
    assertEquals(1, LineParser.parseID(sampleData[0]));
  }

  @Test
  public void testIsASIN() throws Exception {
    boolean isASIN = LineParser.isASIN(sampleData[1]);
    assertTrue(isASIN);
  }

  @Test
  public void testParseASIN() throws Exception {
    assertEquals("0827229534", LineParser.parseASIN(sampleData[1]));
  }

  @Test
  public void testIsTitle() throws Exception {
    boolean isTitle = LineParser.isTitle(sampleData[2]);
    assertTrue(isTitle);
  }

  @Test
  public void testParseTitle() throws Exception {
    assertEquals("Patterns of Preaching: A Sermon Sampler", LineParser.parseTitle(sampleData[2]));
  }

  @Test
  public void testIsGroup() throws Exception {
    boolean isGroup = LineParser.isGroup(sampleData[3]);
    assertTrue(isGroup);
  }

  @Test
  public void testParseGroup() throws Exception {
    assertEquals("Book", LineParser.parseGroup(sampleData[3]));
  }

  @Test
  public void testIsSalesrank() throws Exception {
    boolean isSalesrank = LineParser.isSalesrank(sampleData[4]);
    assertTrue(isSalesrank);
  }

  @Test
  public void testParseSalesRank() throws Exception {
    assertEquals("396585", LineParser.parseSalesrank(sampleData[4]));
  }

  @Test
  public void testIsSimilar() throws Exception {
    boolean isSimilar = LineParser.isSimilar(sampleData[5]);
    assertTrue(isSimilar);
  }

  @Test
  public void testParseSimilarCount() throws Exception {
    assertEquals("5", LineParser.parseSimilarCount(sampleData[5]));
  }

  @Test
  public void testParseSimilarItems() throws Exception {
    String[] expected = {"0804215715", "156101074X", "0687023955", "0687074231", "082721619X"};
    String[] items = LineParser.parseSimilarItems(sampleData[5]);
    assertArrayEquals(expected, items);
  }

  @Test
  public void testIsCategories() throws Exception {
    boolean isCategories = LineParser.isCategories(sampleData[6]);
    assertTrue(isCategories);
  }

  @Test
  public void testParseCategoriesCount() throws Exception {
    String actualCount = LineParser.parseCategoriesCount(sampleData[6]);
    assertEquals("2", actualCount);
  }

  @Test
  public void testParseCategoriesItems() throws Exception {
    String[] expectedItems = {"Books[283155]", "Subjects[1000]", "Religion & Spirituality[22]",
                              "Christianity[12290]", "Clergy[12360]", "Preaching[12368]"};
    String[] actualItems = LineParser.parseCategoriesItems(sampleData[7]);
    assertArrayEquals(expectedItems, actualItems);
  }

  @Test
  public void testParseCategoriesItemName() throws Exception {
    String actualName = LineParser.parseCategoriesItemName("Books[283155]");
    assertEquals("Books", actualName);
  }

  @Test
  public void testParseCategoriesItemID() throws Exception {
    String actualID = LineParser.parseCategoriesItemID("Books[283155]");
    assertEquals("283155", actualID);
  }

  @Test
  public void testIsReviews() throws Exception {
    boolean isReviews = LineParser.isReviews(sampleData[9]);
    assertTrue(isReviews);
  }

  @Test
  public void testParseReviewsTotal() throws Exception {
    String actualTotal = LineParser.parseReviewsTotal(sampleData[9]);
    assertEquals("2", actualTotal);
  }

  @Test
  public void testParseReviewsDownloaded() throws Exception {
    String actualDownloaded = LineParser.parseReviewsDownloaded(sampleData[9]);
    assertEquals("2", actualDownloaded);
  }

  @Test
  public void testParseReviewsAvgRating() throws Exception {
    String actualRating = LineParser.parseReviewsAvgRating(sampleData[9]);
    assertEquals("5", actualRating);
  }

  @Test
  public void testParseReviewsItemDate() throws Exception {
    String actualDate = LineParser.parseReviewsItemDate(sampleData[10]);
    assertEquals("2000-7-28", actualDate);
  }

  @Test
  public void testParseReviewsItemCustomer() throws Exception {
    String actualCustomer = LineParser.parseReviewsItemCustomer(sampleData[10]);
    assertEquals("A2JW67OY8U6HHK", actualCustomer);
  }

  @Test
  public void testParseReviewsItemRating() throws Exception {
    String actualRating = LineParser.parseReviewsItemRating(sampleData[10]);
    assertEquals("5", actualRating);
  }

  @Test
  public void testParseReviewsItemVotes() throws Exception {
    String actualVotes = LineParser.parseReviewsItemVotes(sampleData[10]);
    assertEquals("10", actualVotes);
  }

  @Test
  public void testParseReviewsItemHelpful() throws Exception {
    String actualHelpful = LineParser.parseReviewsItemHelpful(sampleData[10]);
    assertEquals("9", actualHelpful);
  }

  @Test
  public void testIsBlankLine() throws Exception {
    boolean isBlankLine = LineParser.isBlankLine(sampleData[12]);
    assertTrue(isBlankLine);
  }

  @Test
  public void testIsDiscontinuedProduct() throws Exception {
    boolean isDiscontinued = LineParser.isDiscontinuedProduct(sampleData[13]);
    assertTrue(isDiscontinued);
  }
}
