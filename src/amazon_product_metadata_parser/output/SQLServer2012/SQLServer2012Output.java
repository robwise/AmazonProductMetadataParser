package amazon_product_metadata_parser.output.SQLServer2012;

import amazon_product_metadata_parser.dto.ProductDTO;
import amazon_product_metadata_parser.output.Output;
import amazon_product_metadata_parser.output.ProductOutputException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Outputs product data to a SQL Server 2012 database using Windows authentication.
 */
@SuppressWarnings("SpellCheckingInspection")
public class SQLServer2012Output implements Output {

  private static final String SQL_SERVER_DRIVER_CLASS_NAME =
      "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private static final String PC_NAME_WINDOWS_ENVIRONMENT_VARIABLE = "COMPUTERNAME";
  private final String serverUrl;
  private final String pcName = System.getenv(PC_NAME_WINDOWS_ENVIRONMENT_VARIABLE);
  private final String              databaseName;
  private final SQLServerOperations sqlServerOperations;
  private       Connection          conn;

  public SQLServer2012Output(String databaseName, SQLServerOperations sqlServerOperations) {
    this.databaseName = databaseName;
    this.sqlServerOperations = sqlServerOperations;
    serverUrl = buildServerURL();
  }

  /**
   * JDBC connects to the SQL Server in-memory via a URL based on the pc and database
   * names. Example url: {@code
   * jdbc:sqlserver://ROB-PC\\MSSQLSERVER;databaseName=AmazonProductMetadata; integratedSecurity=true};
   */
  private String buildServerURL() {
    return "jdbc:sqlserver://"
           + pcName
           + ";databaseName="
           + databaseName
           + ";integratedSecurity=true";
  }

  @Override
  public void open() {
    try {
      connectSQLServerOperationsToDatabase();
    } catch (ClassNotFoundException | SQLException e) {
      throw new ProductOutputException("Encountered error opening connection to database", e);
    }
    sqlServerOperations.executeBeforeStatements();
  }

  private void connectSQLServerOperationsToDatabase() throws ClassNotFoundException, SQLException {
    // Initialize the SQL Server driver class
    Class.forName(SQL_SERVER_DRIVER_CLASS_NAME);

    //noinspection CallToDriverManagerGetConnection
    conn = DriverManager.getConnection(serverUrl);

    // Pass opened connection to the operations object for it to use
    sqlServerOperations.setConn(conn);
    System.out.println("Connected to SQL Server Database '" + databaseName + "'");
  }

  @Override
  public void execute(ProductDTO productDTO) {
    sqlServerOperations.executeProductStatements(productDTO);
  }

  @Override
  public void close() {
    sqlServerOperations.executeAfterStatements();
    try {
      closeConnection();
    } catch (SQLException e) {
      String reason = "Encounted error while closing database";
      throw new ProductOutputException(reason, e);
    }
  }

  private void closeConnection() throws SQLException {
    if (conn != null) {
      int insertStatementsExecuted = sqlServerOperations.getNumStatementsExecuted();
      conn.close();
      System.out.println("Closed connection to '" + databaseName + "'");
      System.out.printf("Number of statements executed: %d%n", insertStatementsExecuted);
    }
  }
}
