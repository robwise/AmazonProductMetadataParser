package amazon_product_metadata_parser.output;

import java.sql.Connection;
import java.sql.SQLException;

import amazon_product_metadata_parser.dto.ProductDTO;

abstract public class transactSQLStatements {
  private final Connection conn;
  private final ProductDTO productDTO;

  transactSQLStatements(Connection conn, ProductDTO productDTO) throws SQLException {
    if (!conn.isValid(5)) {
      throw new SQLException("Attempted to execute SQL Queries with invalid connection");
    }
    this.conn = conn;
    this.productDTO = productDTO;
  }

  Connection getConn() {
    return conn;
  }

  ProductDTO getProductDTO() {
    return productDTO;
  }

  public abstract void executeQueries();

  public abstract int getNumRowsAffected();

  public abstract int getNumStatementsExecuted();

}
