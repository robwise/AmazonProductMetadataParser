package amazon_product_metadata_parser.output;

import java.io.Closeable;

import amazon_product_metadata_parser.dto.ProductDTO;

/**
 * Interface describing an object that can create products fed by the {@code Parser}.
 */
public interface Output extends Closeable {

  void open();

  void execute(ProductDTO productDTO);
}
