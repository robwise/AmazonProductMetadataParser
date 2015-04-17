package drexel.info634.amazon.parser.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import drexel.info634.amazon.parser.dto.ProductDTO;

/**
 * Simply writes product data to a text file. Useful for debugging. Created by Rob Wise
 * <robert.wise@outlook.com> on 4/16/2015.
 */
public class TextFileOutput implements Output {

  private BufferedWriter bw;
  private Path path;

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
  public void createProduct(ProductDTO productDTO) throws IOException {
    bw.write(productDTO.id);
    bw.newLine();
    bw.write(productDTO.asin);
    bw.newLine();
    bw.write(productDTO.title);
    bw.newLine();
    bw.write(productDTO.group);
  }

  @Override
  public void close() throws IOException {
    bw.close();
  }
}
