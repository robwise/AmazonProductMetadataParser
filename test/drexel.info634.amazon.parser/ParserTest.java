package drexel.info634.amazon.parser;

import org.junit.Test;

import java.io.IOException;

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
    Parser parser = new Parser("resources/amazon-meta.txt");
    String lines = parser.getFirstLines(3);
    assertEquals(expected, lines);
  }
}
