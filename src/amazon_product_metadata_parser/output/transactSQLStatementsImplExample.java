package amazon_product_metadata_parser.output;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import amazon_product_metadata_parser.dto.ProductDTO;

public class transactSQLStatementsImplExample extends transactSQLStatements {

  private final String similarProductDimInsert = "INSERT INTO [similar_product_dimension]"
                                                 + " VALUES (?,?,?,?,?)";

  private List<PreparedStatement> preparedSQLStatements = new ArrayList<>();
  private int                     numRowsAffected       = 0;
  private int                     numStatementsExecuted = 0;

  transactSQLStatementsImplExample(Connection conn,
                                   ProductDTO productDTO) throws SQLException {
    super(conn, productDTO);
    initializePreparedStatements();
  }

  private void initializePreparedStatements() throws SQLException {
    String statement = null;
    try {
      getConn().prepareStatement(similarProductDimInsert);

    } catch (SQLException e) {
      String reason = "There was an error while initializing prepared SQL statement '" +
                      statement + "'";
      throw new SQLException(reason, e);
    }
  }

  @Override
  public void executeQueries() {
    clearParametersFromAllStatements();
  }

  private void clearParametersFromAllStatements() {
    Map
  }

  @Override
  public int getNumRowsAffected() {
    return numRowsAffected;
  }

  @Override
  public int getNumStatementsExecuted() {
    return numStatementsExecuted;
  }
}
