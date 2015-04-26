package amazon_product_metadata_parser.output;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import amazon_product_metadata_parser.dto.ProductDTO;

@SuppressWarnings("resource")
public class JDBCStatementsToExecuteImplExample extends JDBCStatementsToExecute {

  private final Map<String, String>            unpreparedBeforeStatements = new HashMap<>();
  private final Map<String, String>            unpreparedInsertStatements = new HashMap<>();
  private final Map<String, String>            unpreparedAfterStatements  = new HashMap<>();
  private final Map<String, CallableStatement> callableBeforeStatements   = new HashMap<>();
  private final Map<String, CallableStatement> callableInsertStatements   = new HashMap<>();
  private final Map<String, CallableStatement> callableAfterStatements    = new HashMap<>();

  public JDBCStatementsToExecuteImplExample() {
    // Define the schema-dependent statements.
    // Before: these are called once at the beginning
    unpreparedBeforeStatements.put("prepare load tables ", "{call Prepare_Load_Tables}");

    // Insert: these are called (sometimes multiple times) every time executeQueries() is called
    unpreparedInsertStatements.put("insert product", "{call Insert_Product (?,?,?,?,?,?,?)}");
    unpreparedInsertStatements.put("insert parent category", "{call Insert_Parent_Category (?,?,?,"
                                                             + "?,?,?,?)}");
    unpreparedInsertStatements.put("insert child category",
                                   "{call Insert_Child_Category (?,?,?,?,?,?,?)}");
    unpreparedInsertStatements.put("insert customer", "{call Insert_Customer (?)}");
    unpreparedInsertStatements.put("insert review", "{call Insert_Review (?,?,?,?,?,?,?)}");
    unpreparedInsertStatements.put("insert similar product", "{call Insert_Similar_Product (?,?,?,"
                                                             + "?,?,?,?)}");

    // After: these are called once at the end
    unpreparedAfterStatements.put("merge load tables", "{call Merge_Load_Tables}");
  }

  @Override
  public void executeProductStatements(ProductDTO productDTO) {
    if (super.hasValidConn()) {
      // TODO: execute stored procedures
    }
  }

  private void initializePreparedStatements() {
    unpreparedBeforeStatements.forEach((key, value) -> {
      prepareAndAddToPreparedStatements(key, value, callableBeforeStatements);
    });
    unpreparedInsertStatements.forEach((key, value) -> {
      prepareAndAddToPreparedStatements(key, value, callableInsertStatements);
    });
    unpreparedAfterStatements.forEach((key, value) -> {
      prepareAndAddToPreparedStatements(key, value, callableAfterStatements);
    });
  }

  private void prepareAndAddToPreparedStatements(String unpreparedStatementKey,
                                                 String unpreparedStatement,
                                                 Map<String, CallableStatement>
                                                     callableStatements) {
    try (CallableStatement callableStatement = getConn().prepareCall(unpreparedStatement)) {
      callableStatements.put(unpreparedStatementKey, callableStatement);
    } catch (SQLException e) {
      String reason = "There was an error while initializing prepared SQL statement '"
                      + unpreparedStatement + "'";
      throw new RuntimeException(reason, e);
    }
  }

}
