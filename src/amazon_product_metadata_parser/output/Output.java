package amazon_product_metadata_parser.output;

import java.io.Closeable;
import java.io.IOException;

import amazon_product_metadata_parser.dto.ProductDTO;

/**
 * Interface describing an object that can create products fed by the {@code Parser}. <br><br>
 */
public interface Output extends Closeable {

  void open() throws IOException;

  void execute(ProductDTO productDTO) throws IOException;
}
