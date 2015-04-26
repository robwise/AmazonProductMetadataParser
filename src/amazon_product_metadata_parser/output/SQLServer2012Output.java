package amazon_product_metadata_parser.output;

import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import amazon_product_metadata_parser.Parser;
import amazon_product_metadata_parser.dto.ProductDTO;

/**
 * Outputs product data to the SQL Server 2012 database "AmazonData" using Windows authentication.
 */
@SuppressWarnings("SpellCheckingInspection")
public class SQLServer2012Output implements Output {

  private static final String SQL_SERVER_DRIVER_CLASS_NAME =
      "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private static final String PC_NAME_WINDOWS_ENVIRONMENT_VARIABLE = "COMPUTERNAME";
  private final String serverUrl;
  private final String pcName = System.getenv(PC_NAME_WINDOWS_ENVIRONMENT_VARIABLE);
  private final String     databaseName;
  private final JDBCStatementsToExecute statementsToExecute;
  private       Connection conn;
  private int insertStatementsExecuted = 0;
  private int rowsAffected = 0;

  public SQLServer2012Output(String databaseName, JDBCStatementsToExecute statementsToExecute) {
    this.databaseName = databaseName;
    this.statementsToExecute = statementsToExecute;
    serverUrl = buildServerURL();
  }

  //  url = "jdbc:sqlserver://MYPC\\SQLEXPRESS;databaseName=MYDB;integratedSecurity=true";
  private String buildServerURL() {
    return "jdbc:sqlserver://"
           + pcName
           + ";databaseName="
           + databaseName
           + ";integratedSecurity=true";
  }

  public static void main(String[] args) throws SQLException, IOException {
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
        // Fall through
        break;
      default:
        Parser parser = new Parser(response, new SQLServer2012Output("AmazonProductMetadata", new
            JDBCStatementsToExecuteImplExample()));
        parser.parse();
    }

  }

  @Override
  public void open() throws IOException {
    try {
      Class.forName(SQL_SERVER_DRIVER_CLASS_NAME);
      conn = DriverManager.getConnection(serverUrl);
      System.out.println("Connected to SQL Server Database '" + databaseName + "'");
    } catch (ClassNotFoundException | SQLException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void execute(ProductDTO productDTO) throws IOException {
    statementsToExecute.executeProductStatements(productDTO);
  }

  @Override
  public void close() throws IOException {
    try {
      if (conn != null) {
        conn.close();
        System.out.println("Closed connection to '" + databaseName + "'");
      }
    } catch (SQLException e) {
      throw new IOException(e);
    }
  }
}
