package amazon_product_metadata_parser_example_app;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MainTest {

  @Test
  public void runTest() throws Exception {
    String location = "test resources/test-data.txt";
    String databaseName = "AmazonProductMetadata";
    String fakeUserInput = String.format("%s%n%s", databaseName, location);
    InputStream in = new ByteArrayInputStream((fakeUserInput).getBytes());
    Main.run(in);
  }

}
