package amazon_product_metadata_parser;

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

import amazon_product_metadata_parser.dto.FailedValidationException;
import amazon_product_metadata_parser.dto.ProductDTO;
import amazon_product_metadata_parser.dto.ProductDTOFactory;
import amazon_product_metadata_parser.output.ConsoleOutput;
import amazon_product_metadata_parser.output.Output;
import amazon_product_metadata_parser.output.ProductOutputException;

/**
 * Parses Amazon product metadata text file into Java objects.
 * <p>
 * See <a href="https://snap.stanford.edu/data/amazon-meta.html">Data Source</a>
 * <p>
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"})
public class Parser {

  private final static Charset ENCODING        = StandardCharsets.UTF_8;
  private final static int     DATA_START_LINE = 4;
  private final Path   amazonData;
  private final Output output;
  private final ProductDTOFactory fProduct;
  private       int               lineLimit;
  private       int               currLineNumber;
  private       int               productsParsedCount;
  private       String            currLine;
  private       BufferedReader    reader;

  public Parser(String pathString, Output output) {
    this.amazonData = Paths.get(pathString);
    this.output = output;
    fProduct = new ProductDTOFactory();
  }

  public static void main(String... args) throws IOException {
    Parser parser = new Parser("resources/amazon-meta.txt", new ConsoleOutput());
    parser.parse();
  }

  public void parse() throws IOException {
    parse(-1);
  }

  public void parse(int lineLimit) throws IOException {
    String[] productLines = null;
    try (BufferedReader reader = Files.newBufferedReader(amazonData, ENCODING)) {

      initializeFields(lineLimit, reader);

      System.out.println("Beginning Parse...");
      System.out.println();

      // Connect to Output
      output.open();

      moveReaderToDataStart();

      while (!readerAtEndOfData()) {
        productLines = readLinesUntilPastEndOfAProduct();
        ProductDTO productDTO = fProduct.build(productLines);
        outputProductDTO(productDTO);
      }


    } catch (FailedValidationException e) {
      System.out.println("Bad product data. Data reads: ");
      for (int i = 0; i < productLines.length; i++) {
        System.out.printf("Line %2d: %s%n",
                          currLineNumber - productLines.length + i,
                          productLines[i]);
      }
      e.printStackTrace();
    } catch (ProductOutputException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();
    } finally {
      output.close();
    }

    System.out
        .println("Parse completed successfully: " + productsParsedCount + " Products Created...");
  }

  private void initializeFields(int lineLimit, BufferedReader reader) {
    this.lineLimit = lineLimit;
    this.reader = reader;
    currLineNumber = 0;
    productsParsedCount = 0;
    currLine = null;
  }

  private void moveReaderToDataStart() throws IOException {
    // Set so next read will be the first line of data
    while (currLineNumber < (DATA_START_LINE - 1)) {
      readNextLine();
    }
  }

  private boolean readerAtEndOfData() {
    return (null == currLine) || (currLineNumber == lineLimit);
  }

  private String[] readLinesUntilPastEndOfAProduct() throws IOException {
    moveReaderToStartOfNextProduct();
    List<String> productLines = new ArrayList<>();
    while (!readerAtEndOfProduct()) {
      productLines.add(currLine);
      readNextLine();
    }
    return productLines.toArray(new String[productLines.size()]);
  }

  private void moveReaderToStartOfNextProduct() throws IOException {
    while (!readerAtEndOfData() && currLine.isEmpty()) {
      readNextLine();
    }
  }

  private void outputProductDTO(ProductDTO productDTO) {
    output.execute(productDTO);
    productsParsedCount++;
    if ((productsParsedCount % 1000) == 0) {
      System.out.println("***Status: " + productsParsedCount + " Products Created...");
    }
  }

  private void readNextLine() throws IOException {
    currLine = reader.readLine();
    currLine = sanitizeCurrentLine();
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
    return (currLine == null) || currLine.isEmpty();
  }

  // TODO: refactor Parser.getFirstLines()
  public String getFirstLines(int numLines) throws IOException {
    String lines = "";
    try (BufferedReader reader = Files.newBufferedReader(amazonData, ENCODING)) {
      String line;
      int lineCount = 0;
      while ((((line = reader.readLine())) != null) && (lineCount < numLines)) {
        lines += System.lineSeparator() + line;
        lineCount++;
      }
    }
    return lines;
  }

}
