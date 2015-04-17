package drexel.info634.amazon.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import drexel.info634.amazon.parser.dto.ProductDTO;
import drexel.info634.amazon.parser.output.ConsoleOutput;
import drexel.info634.amazon.parser.output.Output;

/**
 * Parses Amazon product metadata text file into Java objects.<br> See <a
 * href="https://snap.stanford.edu/data/amazon-meta.html">Data Source</a><br> <br> Created by Rob
 * Wise on 4/15/2015. <br>
 */
public class Parser {

  private final static Charset ENCODING = StandardCharsets.UTF_8;
  private final static int DATA_START_LINE = 3;

  public static void main(String... args) throws IOException {
//    Parser parser = new Parser("resources/amazon-meta.txt");
//    String lines = parser.getFirstLines(300);
//    System.out.println(lines);

    Parser parser = new Parser("resources/amazon-meta.txt");
    parser.parse(new ConsoleOutput());
  }

  private final Path amazonData;

  public Parser(String pathString) {
    this.amazonData = Paths.get(pathString);
  }

  public void parse(Output output, int lineLimit) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(amazonData, ENCODING)) {
      String line;
      int lineNumber = 0;

      // Connect to Output
      output.open();

      // Skip past beginning lines
      while (lineNumber < DATA_START_LINE) {
        reader.readLine();
        lineNumber++;
      }

      // Parse data into ProductDTOs
      ProductFactory fProduct = new ProductFactory();
      List<String> productLines = new ArrayList<>();

      while ((line = reader.readLine()) != null && ((lineLimit < 0) || (++lineNumber
                                                                        <= lineLimit))) {

        if (isEndOfProduct(line)) {
          outputProduct(output, fProduct, productLines);
          productLines = new ArrayList<>();
        } else {
          // Add current line
          productLines.add(line);
        }
        lineNumber++;
      }
      outputProduct(output, fProduct, productLines);

    } catch (InvalidAttributesException e) {
      e.printStackTrace();
    } finally {
      output.close();
    }
  }

  private List<String> outputProduct(Output output, ProductFactory fProduct,
                                     List<String> productLines)
      throws InvalidAttributesException, IOException {
    String[] productLinesArray = productLines.toArray(new String[productLines.size()]);
    System.out.println("Built Object: ");
    for (String line : productLinesArray) {
      System.out.println(line);
    }
    ProductDTO product = fProduct.build(productLinesArray);
    output.createProduct(product);
    productLines = new ArrayList<>();
    return productLines;
  }

  public void parse(Output output) throws IOException {
    parse(output, -1);
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

  private boolean isEndOfProduct(String line) {
    return line.isEmpty();
  }

}
