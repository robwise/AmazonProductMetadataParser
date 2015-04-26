package amazon_product_metadata_parser.output;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import amazon_product_metadata_parser.dto.CategoryDTO;
import amazon_product_metadata_parser.dto.CategoryItemDTO;
import amazon_product_metadata_parser.dto.ProductDTO;

@SuppressWarnings("resource")
public class JDBCStatementsToExecuteImplExample extends JDBCStatementsToExecute {

  private final Map<String, String>            unpreparedBeforeStatements  = new HashMap<>();
  private final Map<String, String>            unpreparedProductStatements = new HashMap<>();
  private final Map<String, String>            unpreparedAfterStatements   = new HashMap<>();
  private final Map<String, CallableStatement> callableBeforeStatements    = new HashMap<>();
  private final Map<String, CallableStatement> callableProductStatements   = new HashMap<>();
  private final Map<String, CallableStatement> callableAfterStatements     = new HashMap<>();

  public JDBCStatementsToExecuteImplExample() {
    // Define the schema-dependent statements.
    // Before: these are called once at the beginning
    unpreparedBeforeStatements.put("prepare load tables ", "{call Prepare_Load_Tables}");

    // Insert: these are called (sometimes multiple times) every time executeQueries() is called
    unpreparedProductStatements.put("insert product",
                                    "{call Insert_Product (?,?,?,?,?,?,?,?,?)}");
    unpreparedProductStatements.put("insert category",
                                    "{call Insert_Category (?,?,?,?,?,?,?)}");
    unpreparedProductStatements.put("insert customer",
                                    "{call Insert_Customer (?)}");
    unpreparedProductStatements.put("insert review",
                                    "{call Insert_Review (?,?,?,?,?,?,?)}");

    // After: these are called once at the end
    unpreparedAfterStatements.put("merge load tables", "{call Merge_Load_Tables}");
  }

  @Override
  public void executeBeforeStatements() throws SQLException {
    initializeCallableStatements();
    CallableStatement mergeTables = callableBeforeStatements.get("prepare merge tables");
    executeCallableStatement(mergeTables);
  }

  @Override
  public void executeProductStatements(ProductDTO productDTO) throws SQLException {
    CallableStatement insertProduct = callableProductStatements.get("insert product");
    insertProduct.clearParameters();
    insertProduct.setString(1, productDTO.asin);
    insertProduct.setString(2, productDTO.title);
    insertProduct.setString(3, productDTO.group);
    insertProduct.setInt(4, productDTO.salesrank);
    insertProduct.setInt(5, productDTO.similarItems.count);
    insertProduct.setInt(6, productDTO.categories.count);
    insertProduct.setInt(7, productDTO.reviews.total);
    insertProduct.setInt(8, productDTO.reviews.downloaded);
    insertProduct.setDouble(9, productDTO.reviews.avgRating);
    executeCallableStatement(insertProduct);

    for (int i = 0; i < productDTO.categories.count; i++) {
      CategoryDTO category = productDTO.categories.categories[i];
      for (int j = 0; j < category.categoryItemCount; j++) {
        CategoryItemDTO categoryItem = category.categoryItems[j];
        CallableStatement insertCategory = callableProductStatements.get("insert category");
        insertCategory.clearParameters();
        insertCategory.setString(1, productDTO.asin);
        insertCategory.setString(2, categoryItem.id);
        insertCategory.setString(3, categoryItem.name);
        insertCategory.setString(4, categoryItem.parentCategoryItem);
        insertCategory.setBoolean(5, categoryItem.highestParentFlag);
        insertCategory.setBoolean(6, categoryItem.lowestChildFlag);
        insertCategory.setInt(7, categoryItem.depthFromParent);
        executeCallableStatement(insertCategory);
      }
    }

    // TODO: add call to insert customer, insert review,

  }

  @Override
  public void executeAfterStatements() throws SQLException {
    callableAfterStatements.get("merge load tables").execute();
    closeAllStatements();
  }

  private void closeAllStatements() {
    // TODO: implement closeAllStatements()
  }

  private void initializeCallableStatements() {
    unpreparedBeforeStatements.forEach((unpreparedStatementKey, unpreparedBeforeStatement) -> {
      prepareAndAddToPreparedStatements(unpreparedStatementKey,
                                        unpreparedBeforeStatement,
                                        callableBeforeStatements);
    });
    unpreparedProductStatements.forEach((unpreparedStatementKey, unpreparedProductStatement) -> {
      prepareAndAddToPreparedStatements(unpreparedStatementKey,
                                        unpreparedProductStatement,
                                        callableProductStatements);
    });
    unpreparedAfterStatements.forEach((unpreparedStatementKey, unpreparedAfterStatement) -> {
      prepareAndAddToPreparedStatements(unpreparedStatementKey,
                                        unpreparedAfterStatement,
                                        callableAfterStatements);
    });
  }

  private void executeCallableStatement(CallableStatement statement) throws SQLException {
    statement.execute();
    int updateCount = statement.getUpdateCount();
    incrementNumRowsAffected(updateCount);
    incrementNumStatementsExecuted();
  }

  private void prepareAndAddToPreparedStatements(String unpreparedStatementKey,
                                                 String unpreparedStatement,
                                                 Map<String, CallableStatement>
                                                     callableStatements) {
    try (CallableStatement callableStatement = getConn().prepareCall(unpreparedStatement)) {
      callableStatements.put(unpreparedStatementKey, callableStatement);
    } catch (SQLException e) {
      String reason = "There was an error while preparing callable SQL statement '"
                      + unpreparedStatement + "'";
      throw new RuntimeException(reason, e); // TODO: fix this so I can catch it in Parser
    }
  }

}
