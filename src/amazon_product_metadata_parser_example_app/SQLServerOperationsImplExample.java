package amazon_product_metadata_parser_example_app;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import amazon_product_metadata_parser.dto.CategoryDTO;
import amazon_product_metadata_parser.dto.CategoryItemDTO;
import amazon_product_metadata_parser.dto.ProductDTO;
import amazon_product_metadata_parser.dto.ReviewDTO;
import amazon_product_metadata_parser.output.ProductOutputException;
import amazon_product_metadata_parser.output.SQLServer2012.SQLServerOperations;

@SuppressWarnings({"resource", "CodeBlock2Expr"})
public class SQLServerOperationsImplExample extends SQLServerOperations {

  private final Map<String, String>            unpreparedBeforeStatements  = new HashMap<>();
  private final Map<String, String>            unpreparedProductStatements = new HashMap<>();
  private final Map<String, String>            unpreparedAfterStatements   = new HashMap<>();
  private final Map<String, CallableStatement> callableBeforeStatements    = new HashMap<>();
  private final Map<String, CallableStatement> callableProductStatements   = new HashMap<>();
  private final Map<String, CallableStatement> callableAfterStatements     = new HashMap<>();

  public SQLServerOperationsImplExample() {
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
                                    "{call Insert_Review (?,?,?,?,?,?)}");

    // After: these are called once at the end
    unpreparedAfterStatements.put("merge load tables", "{call Merge_Load_Tables}");
  }

  @Override
  public void executeBeforeStatements() {
    initializeCallableStatements();
    CallableStatement mergeTables = callableBeforeStatements.get("prepare merge tables");
    try {
      executeCallableStatement(mergeTables);
    } catch (SQLException e) {
      throw new ProductOutputException("Encountered error while executing before statements", e);
    }
  }

  @Override
  public void executeProductStatements(ProductDTO productDTO) {
    CallableStatement insertProduct = callableProductStatements.get("insert product");
    try {
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
    } catch (SQLException e) {
      throw new ProductOutputException("Encountered error while executing insert product "
                                       + "statement", e);
    }

    for (int i = 0; i < productDTO.categories.count; i++) {
      CategoryDTO category = productDTO.categories.categories[i];
      for (int j = 0; j < category.categoryItemCount; j++) {
        CategoryItemDTO categoryItem = category.categoryItems[j];
        CallableStatement insertCategory = callableProductStatements.get("insert category");
        try {
          insertCategory.clearParameters();
          insertCategory.setString(1, productDTO.asin);
          insertCategory.setString(2, categoryItem.id);
          insertCategory.setString(3, categoryItem.name);
          insertCategory.setString(4, categoryItem.parentCategoryItem);
          insertCategory.setBoolean(5, categoryItem.highestParentFlag);
          insertCategory.setBoolean(6, categoryItem.lowestChildFlag);
          insertCategory.setInt(7, categoryItem.depthFromParent);
          executeCallableStatement(insertCategory);
        } catch (SQLException e) {
          throw new ProductOutputException("Encountered error while executing insert category "
                                           + "statement", e);
        }
      }
    }

    CallableStatement insertCustomer = callableProductStatements.get("insert customer");
    CallableStatement insertReview = callableProductStatements.get("insert review");
    for (int i = 0; i < productDTO.reviews.reviews.length; i++) {
      ReviewDTO reviewDTO = productDTO.reviews.reviews[i];

      try {
        insertCustomer.clearParameters();

        insertCustomer.setString(1, reviewDTO.customer);
        executeCallableStatement(insertCustomer);
      } catch (SQLException e) {
        throw new ProductOutputException("Encountered error while executing insert customer "
                                         + "statement", e);
      }

      try {
        insertReview.clearParameters();
        insertReview.setDate(1, Date.valueOf(reviewDTO.date));
        insertReview.setString(2, productDTO.asin);
        insertReview.setString(3, reviewDTO.customer);
        insertReview.setInt(4, reviewDTO.rating);
        insertReview.setInt(5, reviewDTO.votes);
        insertReview.setInt(6, reviewDTO.helpful);
        executeCallableStatement(insertReview);
      } catch (SQLException e) {
        throw new ProductOutputException("Encountered error while executing insert review "
                                         + "statement", e);
      }
    }
  }

  @Override
  public void executeAfterStatements() {
    try {
      callableAfterStatements.get("merge load tables").execute();
    } catch (SQLException e) {
      throw new ProductOutputException("Encountered error while executing after statements", e);
    }
    closeAllStatements();
  }

  private void closeAllStatements() {
    callableBeforeStatements.forEach((callableBeforeStatementKey, callableBeforeStatement) -> {
      closeCallableStatement(callableBeforeStatement);
    });
    callableProductStatements.forEach((callableProductStatementKey, callableProductStatement) -> {
      closeCallableStatement(callableProductStatement);
    });
    callableAfterStatements.forEach((callableAfterStatementKey, callableAfterStatement) -> {
      closeCallableStatement(callableAfterStatement);
    });
  }

  @SuppressWarnings("MethodMayBeStatic")
  private void closeCallableStatement(CallableStatement callableStatement) {
    try {
      callableStatement.close();
    } catch (SQLException e) {
      String reason = "There was an error while closing callable SQL statement '"
                      + callableStatement + "'";
      throw new ProductOutputException(reason, e);
    }
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
      throw new ProductOutputException(reason, e);
    }
  }

}
