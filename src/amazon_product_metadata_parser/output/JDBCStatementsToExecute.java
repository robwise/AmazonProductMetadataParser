package amazon_product_metadata_parser.output;

import java.sql.Connection;
import java.sql.SQLException;

import amazon_product_metadata_parser.dto.ProductDTO;

abstract public class JDBCStatementsToExecute {

  private Connection conn;
  private int        numRowsAffected;
  private int        numStatementsExecuted;

  public JDBCStatementsToExecute() {
    this.numStatementsExecuted = 0;
    this.numRowsAffected = 0;
  }

  public Connection getConn() {
    return conn;
  }

  public void setConn(Connection conn) throws SQLException {
    if (!conn.isValid(5)) {
      throw new SQLException("Attempted to execute SQL Queries with invalid connection");
    }
    this.conn = conn;
  }

  public abstract void executeProductStatements(ProductDTO productDTO);

  public void incrementNumStatementsExecuted() {
    incrementNumStatementsExecuted(1);
  }

  public void incrementNumStatementsExecuted(int increment) {
    numStatementsExecuted += increment;
  }

  public void incrementNumRowsAffected() {
    incrementNumRowsAffected(1);
  }

  public void incrementNumRowsAffected(int increment) {
    numRowsAffected += increment;
  }

  public int getNumRowsAffected() {
    return numRowsAffected;
  }

  public int getNumStatementsExecuted() {
    return numStatementsExecuted;
  }

  public boolean hasValidConn() {
    try {
      return (null == conn) || !conn.isValid(5);
    } catch (SQLException e) {
      String reason = "Called isValid() on connection using an integer less than 0";
      throw new RuntimeException(reason, e);
    }
  }
}
