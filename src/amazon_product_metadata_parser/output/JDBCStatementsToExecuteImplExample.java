package amazon_product_metadata_parser.output;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import amazon_product_metadata_parser.dto.ProductDTO;

public class JDBCStatementsToExecuteImplExample extends JDBCStatementsToExecute {

  private final String similarProductDimInsert = "INSERT INTO [similar_product_dimension]"
                                                 + " VALUES (?,?,?,?,?)";

  private List<PreparedStatement> preparedSQLStatements = new ArrayList<>();

  public JDBCStatementsToExecuteImplExample() throws SQLException {
    // Constructor does nothing
  }

  @Override
  public void executeQueries(ProductDTO productDTO) {
    if (super.hasValidConn()) {
      // TODO: execute stored procedures
    }
  }

  private void initializePreparedStatements() throws SQLException {
    PreparedStatement statement = null;
    try {
      statement = getConn().prepareStatement(similarProductDimInsert);

    } catch (SQLException e) {
      String reason = "There was an error while initializing prepared SQL statement '" +
                      statement.toString() + "'";
      throw new SQLException(reason, e);
    }
  }

}
