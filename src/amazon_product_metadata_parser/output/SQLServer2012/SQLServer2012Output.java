package amazon_product_metadata_parser.output.SQLServer2012;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import amazon_product_metadata_parser.dto.ProductDTO;
import amazon_product_metadata_parser.output.Output;
import amazon_product_metadata_parser.output.ProductOutputException;

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
  private final SQLServerOperations statementsToExecute;
  private       Connection conn;

  public SQLServer2012Output(String databaseName, SQLServerOperations statementsToExecute) {
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

  @Override
  public void open() {
    try {
      Class.forName(SQL_SERVER_DRIVER_CLASS_NAME);
      //noinspection CallToDriverManagerGetConnection
      conn = DriverManager.getConnection(serverUrl);
      System.out.println("Connected to SQL Server Database '" + databaseName + "'");
      statementsToExecute.setConn(conn);
      statementsToExecute.executeBeforeStatements();
    } catch (ClassNotFoundException | SQLException e) {
      throw new ProductOutputException(e);
    }
  }

  @Override
  public void execute(ProductDTO productDTO) {
    statementsToExecute.executeProductStatements(productDTO);
  }

  @Override
  public void close() {
    statementsToExecute.executeAfterStatements();
    int insertStatementsExecuted = statementsToExecute.getNumStatementsExecuted();
    try {
      if (conn != null) {
        conn.close();
        System.out.println("Closed connection to '" + databaseName + "'");
        System.out.printf("Number of statements executed: %d%n", insertStatementsExecuted);
      }
    } catch (SQLException e) {
      String reason = "Encounted error while closing database";
      throw new ProductOutputException(reason, e);
    }
  }
}
