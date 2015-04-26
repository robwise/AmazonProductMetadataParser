package amazon_product_metadata_parser_example_app;

import java.io.Console;
import java.io.IOException;

import amazon_product_metadata_parser.Parser;
import amazon_product_metadata_parser.output.SQLServer2012.SQLServer2012Output;

public class Main {

  public static void main(String... args) throws IOException {
    System.out.printf("This program parses the 'amazon-meta.txt' file and inserts the data into "
                      + "a SQL Server 2012 database.%nIt assumes you have set up the appropriate"
                      + " stored procedures and schema in a database named 'AmazonProductMetadata'"
                      + ".%n");
    System.out.println("Press enter the path to the 'amazon-meta.txt' file to proceed or "
                       + "press Ctrl+C to exit");
    Console console = System.console();
    String response = console.readLine();
    switch (response) {
      case "quit":
        // Fall through
      case "cancel":
        // Fall through
      case "exit":
        break;
      default:
        SQLServer2012Output output = new SQLServer2012Output("AmazonProductMetadata",
                                                             new SQLServerOperationsImplExample());
        Parser parser = new Parser(response, output);
        parser.parse();
    }
  }

}
