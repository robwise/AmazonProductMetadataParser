package amazon_product_metadata_parser.dto;

/**
 * Exception class representing an invalid data.
 */
public class FailedValidationException extends Exception {

  /**
   * Constructor
   */
  public FailedValidationException(String message) {
    super(message);
  }

  /**
   * Constructor
   *
   * @param cause the {@code Throwable} that caused the exception
   */
  public FailedValidationException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructor
   *
   * @param message the error message to associate with the exception
   * @param cause   the {@code Throwable} that caused the exception
   */
  public FailedValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
