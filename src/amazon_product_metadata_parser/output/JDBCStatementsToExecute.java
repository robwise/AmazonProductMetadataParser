package amazon_product_metadata_parser.output;

import java.sql.Connection;
import java.sql.SQLException;

import amazon_product_metadata_parser.dto.ProductDTO;

abstract public class JDBCStatementsToExecute {

  private final Connection conn;
  private int numRowsAffected;
  private int numStatementsExecuted;

  JDBCStatementsToExecute(Connection conn) throws SQLException {
    if (!conn.isValid(5)) {
      throw new SQLException("Attempted to execute SQL Queries with invalid connection");
    }
    this.conn = conn;
    this.numStatementsExecuted = 0;
    this.numRowsAffected = 0;
  }

  Connection getConn() {
    return conn;
  }

  public abstract void executeQueries(ProductDTO productDTO);

  public void incrementNumStatementsExecuted(int increment) {
    numStatementsExecuted += increment;
  }

  public void incrementNumStatementsExecuted() {
    incrementNumStatementsExecuted(1);
  }

  public void incrementNumRowsAffected(int increment) {
    numRowsAffected += increment;
  }

  public void incrementNumRowsAffected() {
    incrementNumRowsAffected(1);
  }

  public int getNumRowsAffected() {
    return numRowsAffected;
  }

  public int getNumStatementsExecuted() {
    return numStatementsExecuted;
  }

}
