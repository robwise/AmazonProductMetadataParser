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
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"})
public class Parser {

  private final static Charset ENCODING = StandardCharsets.UTF_8;
  private final static int DATA_START_LINE = 4;
  private final Path amazonData;
  private final Output output;
  private final ProductFactory fProduct;
  private int lineLimit;
  private int currLineNumber;
  private int productCount;
  private String currLine;
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

      moveReaderToDataStart();

      while (!readerAtEndOfData()) {
        String[] productLines = readLinesUntilPastEndOfAProduct();
        outputProduct(productLines);
      }

    } catch (InvalidAttributesException e) {
      System.out.println("Bad data on line: " + currLineNumber);
      System.out.println("Line reads: " + currLine);
      e.printStackTrace();
    } finally {
      output.close();
    }

    System.out.println("Parse complete: " + productCount + " Products Created...");
  }

  private void initializeFields(int lineLimit, BufferedReader reader) {
    this.lineLimit = lineLimit;
    this.reader = reader;
    currLineNumber = 0;
    productCount = 0;
    currLine = null;
  }

  private void moveReaderToDataStart() throws IOException {
    // Set so next read will be the first line of data
    while (currLineNumber < DATA_START_LINE - 1) {
      readNextLine();
    }
  }

  private boolean readerAtEndOfData() {
    return null == currLine || currLineNumber == lineLimit;
  }

  private String[] readLinesUntilPastEndOfAProduct() throws IOException {
    List<String> productLines = new ArrayList<>();
    while (true) {
      readNextLine();
      sanitizeCurrentLine();
      if (readerAtEndOfProduct()) {
        return productLines.toArray(new String[productLines.size()]);
      }
      productLines.add(currLine);
    }
  }

  private void outputProduct(String[] productLines) throws InvalidAttributesException, IOException {
    ProductDTO product = fProduct.build(productLines);
    output.createProduct(product);
    productCount++;
    if (productCount % 1000 == 0) {
      System.out.println("***Status: " + productCount + " Products Created...");
    }
  }

  private void readNextLine() throws IOException {
    currLine = reader.readLine();
    currLineNumber++;
  }

  private String sanitizeCurrentLine() {
    if (null == currLine) {
      return null;
    }
    // Normalize non-regex-parse-able characters
    return Normalizer.normalize(currLine, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
  }

  private boolean readerAtEndOfProduct() {
    return currLine == null || currLine.isEmpty();
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
