package drexel.info634.amazon.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import drexel.info634.amazon.parser.dto.ProductDTO;
import drexel.info634.amazon.parser.output.ConsoleOutput;
import drexel.info634.amazon.parser.output.Output;

/**
 * Parses Amazon product metadata text file into Java objects.
 * <p>
 * See <a href="https://snap.stanford.edu/data/amazon-meta.html">Data Source</a>
 * <p>
 * Created by Rob Wise on 4/15/2015.
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class Parser {

  private final static Charset ENCODING = StandardCharsets.UTF_8;
  private final static int DATA_START_LINE = 3;
  private final Path amazonData;
  private final Output output;
  private final ProductFactory fProduct;
  private int lineLimit;
  private int lineNumber;
  private int productCount;
  private String line;
  private BufferedReader reader;
  public Parser(String pathString, Output output) {
    this.amazonData = Paths.get(pathString);
    this.output = output;
    fProduct = new ProductFactory();
  }

  public static void main(String... args) throws IOException {
    Parser parser = new Parser("resources/amazon-meta.txt", new ConsoleOutput());
    parser.parse();
  }

  public void parse() throws IOException {
    parse(-1);
  }

  public void parse(int lineLimit) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(amazonData, ENCODING)) {

      initializeFields(lineLimit, reader);

      System.out.println("Beginning Parse...");
      System.out.println();

      // Connect to Output
      output.open();

      moveReaderToDataStart(reader);

      // Parse data into ProductDTOs
      List<String> productLines = new ArrayList<>();

      while (lineLimit < 0 || ++lineNumber <= lineLimit) {
        line = reader.readLine();

        // Check for end of data
        if (readerAtEndOfData()) {
          outputProduct(productLines);
          break;
        }

        // Normalize non-regex-parse-able characters
        line = Normalizer.normalize(line, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        if (readerAtEndOfProduct()) {
          outputProduct(productLines);
          productLines = new ArrayList<>();
        } else {
          productLines.add(line);
        }
      }
    } catch (InvalidAttributesException e) {
      System.out.println("Bad data on line: " + lineNumber);
      System.out.println("Line reads: " + line);
      e.printStackTrace();
    } finally {
      output.close();
    }
    System.out.println("Parse complete: " + productCount + " Products Created...");
  }

  private void initializeFields(int lineLimit, BufferedReader reader) {
    this.lineLimit = lineLimit;
    this.reader = reader;
    lineNumber = 0;
    productCount = 0;
    line = null;
  }

  private void moveReaderToDataStart(BufferedReader reader) throws IOException {
    // Skip past beginning lines
    while (lineNumber < DATA_START_LINE) {
      reader.readLine();
      lineNumber++;
    }
  }

  private boolean readerAtEndOfData() {
    return null == line || lineNumber == lineLimit;
  }

  private void outputProduct(List<String> productLines)
      throws InvalidAttributesException, IOException {
    String[] productLinesArray = productLines.toArray(new String[productLines.size()]);
    ProductDTO product = fProduct.build(productLinesArray);
    output.createProduct(product);
    productCount++;
    if (productCount % 1000 == 0) {
      System.out.println("***Status: " + productCount + " Products Created...");
    }
  }

  private boolean readerAtEndOfProduct() {
    return line.isEmpty();
  }

  public String getFirstLines(int numLines) throws IOException {
    String lines = "";
    try (BufferedReader reader = Files.newBufferedReader(amazonData, ENCODING)) {
      String line;
      int lineCount = 0;
      while ((line = reader.readLine()) != null && lineCount < numLines) {
        lines += System.lineSeparator() + line;
        lineCount++;
      }
    }
    return lines;
  }

}
