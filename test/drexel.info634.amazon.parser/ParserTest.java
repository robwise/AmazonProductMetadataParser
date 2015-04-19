package drexel.info634.amazon.parser;

import org.junit.Test;

import java.io.IOException;

import drexel.info634.amazon.parser.output.StringOutput;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rob on 4/15/2015.
 */
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
  public void testParseData() throws IOException {
    String expectedParsedData = "0" + LINE_SEPARATOR
                                + "0771044445" + LINE_SEPARATOR
                                + "discontinued product" + LINE_SEPARATOR
                                + "1" + LINE_SEPARATOR
                                + "0827229534" + LINE_SEPARATOR
                                + "Patterns of Preaching: A Sermon Sampler" + LINE_SEPARATOR
                                + "Book" + LINE_SEPARATOR
                                + "396585" + LINE_SEPARATOR
                                + "5" + LINE_SEPARATOR
                                + "0804215715" + LINE_SEPARATOR
                                + "156101074X" + LINE_SEPARATOR
                                + "0687023955" + LINE_SEPARATOR
                                + "0687074231" + LINE_SEPARATOR
                                + "082721619X" + LINE_SEPARATOR
                                + LINE_SEPARATOR
                                + "Reviews:" + LINE_SEPARATOR
                                + LINE_SEPARATOR
                                + "  Review Date: 2000-7-28" + LINE_SEPARATOR
                                + "  Review Customer: A2JW67OY8U6HHK" + LINE_SEPARATOR
                                + "  Review Rating: 5" + LINE_SEPARATOR
                                + "  Review Votes: 10" + LINE_SEPARATOR
                                + "  Review Helpful: 9" + LINE_SEPARATOR
                                + LINE_SEPARATOR
                                + "  Review Date: 2003-12-14" + LINE_SEPARATOR
                                + "  Review Customer: A2VE83MZF98ITY" + LINE_SEPARATOR
                                + "  Review Rating: 5" + LINE_SEPARATOR
                                + "  Review Votes: 6" + LINE_SEPARATOR
                                + "  Review Helpful: 5" + LINE_SEPARATOR
                                + LINE_SEPARATOR;
    StringOutput output = new StringOutput();
    Parser parser = new Parser("test resources/test-data.txt", output);
    parser.parse();
    String actualParsedData = output.result;
    assertEquals(expectedParsedData, actualParsedData);
  }
}
