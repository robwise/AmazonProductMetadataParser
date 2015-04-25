package amazon_product_metadata_parser.output;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
  private       Connection          conn;
  private int insertStatementsExecuted = 0;
  private int rowsAffected = 0;

  public SQLServer2012Output(String databaseName, transactSQLStatements queries)
      throws SQLException {
    this.databaseName = databaseName;
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

  // TODO: Erase this
  public static void main(String... args) throws IOException, SQLException {
    SQLServer2012Output output = new SQLServer2012Output("AdventureWorks2012", null);
    output.open();
    output.execute(null);
    output.close();
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
    ResultSet queryResults = null;
    PreparedStatement statement = null;
    try {
      for (int i = 0; i < preparedSQLInsertStatements.length; i++) {
        statement = preparedSQLInsertStatements[i];
        statement.clearParameters();
        statement.
        rowsAffected += statement.executeUpdate();
        insertStatementsExecuted++;
      }
    } catch (SQLException e) {
      throw new IOException(e);
    } finally {
      try {
        if (queryResults != null) {
          queryResults.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (Exception e) {
        throw new IOException(e);
      }
    }
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
