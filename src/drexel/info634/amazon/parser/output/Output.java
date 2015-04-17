package drexel.info634.amazon.parser.output;

import java.io.Closeable;
import java.io.IOException;

import drexel.info634.amazon.parser.dto.ProductDTO;

/**
 * Interface describing an object that can create products fed by the {@code Parser}. <br><br>
 * Created by Rob on 4/15/2015.
 */
public interface Output extends Closeable {

  void open() throws IOException;

  void createProduct(ProductDTO productDTO) throws IOException;
}
