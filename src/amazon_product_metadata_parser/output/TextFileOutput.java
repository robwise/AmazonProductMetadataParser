package amazon_product_metadata_parser.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import amazon_product_metadata_parser.dto.ProductDTO;

/**
 * Simply writes product data to a text file. Useful for debugging.
 */
public class TextFileOutput implements Output {

  private final Path           path;
  private       BufferedWriter bw;

  public TextFileOutput(String pathString) {
    path = Paths.get(pathString);
    if (!Files.exists(path) || !Files.isWritable(path)) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void open() throws IOException {
    bw = Files.newBufferedWriter(path);
  }

  @Override
  public void execute(ProductDTO productDTO) throws IOException {
    bw.write(productDTO.toString());
    bw.newLine();
  }

  @Override
  public void close() throws IOException {
    bw.close();
  }
}
