package drexel.info634.amazon.parser.output;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import drexel.info634.amazon.parser.dto.ProductDTO;

/**
 * Outputs product data to the SQL Server 2012 database "AmazonData" using Windows
 * authentication.<br><br> Created by Rob Wise <robert.wise@outlook.com> on 4/17/2015.
 */
public class SQLServer2012Output implements Output {

  private static final String
      SQL_SERVER_DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private static final String PC_NAME_WINDOWS_ENVIRONMENT_VARIABLE = "COMPUTERNAME";
  private final String serverUrl;
  private final String pcName = System.getenv(PC_NAME_WINDOWS_ENVIRONMENT_VARIABLE);
  private final String databaseName;
  private Connection conn;

  public SQLServer2012Output(String databaseName) {
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
  public static void main(String... args) throws IOException {
    SQLServer2012Output output = new SQLServer2012Output("AdventureWorks2012");
    output.open();
    output.createProduct(null);
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
  public void createProduct(ProductDTO productDTO) throws IOException {
    // TODO: actually write ProductDTO to server
    // Create and execute an SQL statement that returns some data.
    ResultSet queryResults = null;
    Statement statement = null;
    try {
      statement = conn.createStatement();
      String query = "SELECT TOP 10 * FROM Person.Person";
      queryResults = statement.executeQuery(query);
      // Iterate through the data in the result set and display it.
      while (queryResults.next()) {
        System.out.println(queryResults.getString(4) + " " + queryResults.getString(7));
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
