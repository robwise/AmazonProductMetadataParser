package amazon_product_metadata_parser.test_helpers;

import amazon_product_metadata_parser.dto.ProductDTO;
import amazon_product_metadata_parser.output.SQLServer2012.SQLServerOperations;

public class SQLServerOperationsMock extends SQLServerOperations {

  private boolean hasReceivedExecuteBeforeStatementsCall = false;
  private boolean hasReceivedExecuteProductStatementsCall = false;
  private boolean hasReceivedExecuteAfterStatementsCall = false;

  @Override
  public void executeBeforeStatements() {
    this.hasReceivedExecuteBeforeStatementsCall = true;
  }

  @Override
  public void executeProductStatements(ProductDTO productDTO) {
    this.hasReceivedExecuteProductStatementsCall = true;
  }

  @Override
  public void executeAfterStatements() {
    this.hasReceivedExecuteAfterStatementsCall = true;
  }

  public boolean hasReceivedExecuteAfterStatementsCall() {
    return hasReceivedExecuteAfterStatementsCall;
  }

  public boolean hasReceivedExecuteProductStatementsCall() {
    return hasReceivedExecuteProductStatementsCall;
  }

  public boolean hasReceivedExecuteBeforeStatementsCall() {
    return hasReceivedExecuteBeforeStatementsCall;
  }
}
