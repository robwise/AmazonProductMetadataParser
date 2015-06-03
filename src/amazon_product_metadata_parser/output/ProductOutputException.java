package amazon_product_metadata_parser.output;

/**
 * Exception class representing an unexpected error when {@link amazon_product_metadata_parser
 * .output.Output} encounters an error.
 */
@SuppressWarnings("unused")
public class ProductOutputException extends RuntimeException {

  /**
   * Constructor
   */
  public ProductOutputException(String message) {
    super(message);
  }

  /**
   * Constructor
   *
   * @param cause the {@code Throwable} that caused the exception
   */
  public ProductOutputException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructor
   *
   * @param message the error message to associate with the exception
   * @param cause   the {@code Throwable} that caused the exception
   */
  public ProductOutputException(String message, Throwable cause) {
    super(message, cause);
  }
}
