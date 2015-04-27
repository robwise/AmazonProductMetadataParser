package amazon_product_metadata_parser_example_app;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MainTest {

  @Test
  public void runTest() throws Exception {
    InputStream in = new ByteArrayInputStream("test resources/test-data.txt".getBytes());
    Main.run(in);
  }

}
