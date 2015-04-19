package amazon_product_metadata_parser;

import org.junit.Test;

import java.io.IOException;

import amazon_product_metadata_parser.output.StringOutput;

import static org.junit.Assert.assertEquals;

public class ParserTest {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  @Test
  public void testReadFromFile() throws IOException {
    String expected = LINE_SEPARATOR
                      + "# Full information about Amazon Share the Love "
                      + "products" + LINE_SEPARATOR
                      + "Total items: 548552" + LINE_SEPARATOR;
    Parser parser = new Parser("resources/amazon-meta.txt", null);
    String lines = parser.getFirstLines(3);
    assertEquals(expected, lines);
  }

  @Test
  public void testParseDataIntoStringOutput() throws IOException {
    String expectedParsedData = "ID: 0\n"
                                + "ASIN: 0771044445\n"
                                + "Discontinued Product\n"
                                + "\n"
                                + "ID: 1\n"
                                + "ASIN: 0827229534\n"
                                + "Title: Patterns of Preaching: A Sermon Sampler\n"
                                + "Group: Book\n"
                                + "Sales Rank: 396585\n"
                                + "Similar Items:\n"
                                + "  0804215715\n"
                                + "  156101074X\n"
                                + "  0687023955\n"
                                + "  0687074231\n"
                                + "  082721619X\n"
                                + "Categories: 2\n"
                                + "Category Items: 6\n"
                                + "  Category Name: Books\n"
                                + "    Category ID: 283155\n"
                                + "    Parent Category: 283155\n"
                                + "    Is Highest Parent: true\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 0\n"
                                + "  Category Name: Subjects\n"
                                + "    Category ID: 1000\n"
                                + "    Parent Category: 283155\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 1\n"
                                + "  Category Name: Religion & Spirituality\n"
                                + "    Category ID: 22\n"
                                + "    Parent Category: 1000\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 2\n"
                                + "  Category Name: Christianity\n"
                                + "    Category ID: 12290\n"
                                + "    Parent Category: 22\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 3\n"
                                + "  Category Name: Clergy\n"
                                + "    Category ID: 12360\n"
                                + "    Parent Category: 12290\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 4\n"
                                + "  Category Name: Preaching\n"
                                + "    Category ID: 12368\n"
                                + "    Parent Category: 12360\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: true\n"
                                + "    Depth from Parent: 5\n"
                                + "Category Items: 6\n"
                                + "  Category Name: Books\n"
                                + "    Category ID: 283155\n"
                                + "    Parent Category: 283155\n"
                                + "    Is Highest Parent: true\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 0\n"
                                + "  Category Name: Subjects\n"
                                + "    Category ID: 1000\n"
                                + "    Parent Category: 283155\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 1\n"
                                + "  Category Name: Religion & Spirituality\n"
                                + "    Category ID: 22\n"
                                + "    Parent Category: 1000\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 2\n"
                                + "  Category Name: Christianity\n"
                                + "    Category ID: 12290\n"
                                + "    Parent Category: 22\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 3\n"
                                + "  Category Name: Clergy\n"
                                + "    Category ID: 12360\n"
                                + "    Parent Category: 12290\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: false\n"
                                + "    Depth from Parent: 4\n"
                                + "  Category Name: Sermons\n"
                                + "    Category ID: 12370\n"
                                + "    Parent Category: 12360\n"
                                + "    Is Highest Parent: false\n"
                                + "    Is Lowest Child: true\n"
                                + "    Depth from Parent: 5\n"
                                + "Reviews Total: 2\n"
                                + "Reviews Downloaded: 2\n"
                                + "Reviews Average Rating: 5.000000\n"
                                + "  Review Date: 2000-07-28\n"
                                + "    Review Customer: A2JW67OY8U6HHK\n"
                                + "    Review Rating: 5\n"
                                + "    Review Votes: 10\n"
                                + "    Review Helpful: 9\n"
                                + "  Review Date: 2003-12-14\n"
                                + "    Review Customer: A2VE83MZF98ITY\n"
                                + "    Review Rating: 5\n"
                                + "    Review Votes: 6\n"
                                + "    Review Helpful: 5\n"
                                + "\n";
    StringOutput output = new StringOutput();
    Parser parser = new Parser("test resources/test-data.txt", output);
    parser.parse();
    String actualParsedData = output.result;
    assertEquals(expectedParsedData, actualParsedData);
  }
}
