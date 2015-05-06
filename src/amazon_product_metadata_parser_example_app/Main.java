package amazon_product_metadata_parser_example_app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import amazon_product_metadata_parser.Parser;
import amazon_product_metadata_parser.output.SQLServer2012.SQLServer2012Output;

public class Main {

  private static Scanner scanner;

  public static void main(String... args) throws IOException {
    run();
  }

  public static void run() throws IOException {
    run(System.in);
  }

  public static boolean setScanner(InputStream inputStream) {
    if (null == scanner) {
      scanner = new Scanner(inputStream);
      return true;
    }
    return false;
  }

  public static void run(InputStream inputStream) throws IOException {
    setScanner(inputStream);

    System.out.printf("Amazon Product Metadata Parser"
                      + "%n-- This program parses the 'amazon-meta.txt' file and inserts the data"
                      + "%n-- into a SQL Server 2012 database. It assumes you have set up the"
                      + "%n-- appropriate stored procedures and schema."
                      + "%n");

    String
        databaseName =
        getUserInput("Please enter the name of your SQL Server database.", "Enter Database Name: ");
    SQLServer2012Output
        output =
        new SQLServer2012Output(databaseName, new SQLServerOperationsImplExample());
    String location = getLocationOfData();
    int lines = Integer.valueOf(
        getUserInput("Please enter the number of products you wish to parse. If you "
                     + "wish to parse all of the data, please enter '-1'", "Products: "));
    Parser parser = new Parser(location, output);
    parser.parse(lines);
  }

  private static String getLocationOfData() {
    String location;
    if (Files.exists(Paths.get("amazon-meta.txt"))) {
      location = "amazon-meta.txt";
    } else {
      location =
          getUserInput(
              "Please enter the location of the 'amazon-meta.txt' file or type 'quit' to exit.",
              "Enter Path: ");
    }
    return location;
  }

  private static String getUserInput(String message, String prompt) {
    System.out.println(
    );
    System.out.println(message);
    System.out.print(prompt);

    String response = scanner.nextLine();
    System.out.println();
    switch (response) {
      case "q":
        // Fall through
      case "quit":
        // Fall through
      case "cancel":
        // Fall through
      case "exit":
        System.out.println("Shutting down...");
        System.exit(0);
        break;
      default:
        // Do nothing
    }
    return response;
  }
}
