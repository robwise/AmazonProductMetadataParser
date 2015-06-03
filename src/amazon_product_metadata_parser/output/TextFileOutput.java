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
@SuppressWarnings("unused")
public class TextFileOutput implements Output {

  private final Path           path;
  private       BufferedWriter bw;

  public TextFileOutput(String pathString) {
    path = Paths.get(pathString);
    if (!Files.exists(path) || !Files.isWritable(path)) {
      throw new IllegalArgumentException("Given path either does not exist or is not writable");
    }
  }

  @Override
  public void open() {
    try {
      bw = Files.newBufferedWriter(path);
    } catch (IOException e) {
      String reason = String.format("IO Error while attempting to create buffered writer on "
                                    + "path '%s'", path);
      throw new ProductOutputException(reason, e);
    }
  }

  @Override
  public void execute(ProductDTO productDTO) {
    try {
      bw.write(productDTO.toString());
      bw.newLine();
    } catch (IOException e) {
      String reason = String.format("IO Error while attempting to write product data to path '%s'",
                                    path);
      throw new ProductOutputException(reason, e);
    }
  }

  @Override
  public void close() throws IOException {
    bw.close();
  }
}
