package amazon_product_metadata_parser.output;

import org.junit.Test;

import amazon_product_metadata_parser.output.SQLServer2012.SQLServer2012Output;
import amazon_product_metadata_parser.test_helpers.SQLServerOperationsMock;

import static org.junit.Assert.assertTrue;

public class SQLServer2012OutputTest {

  @Test
  public void testConnectionIsValid() throws Exception {
    SQLServer2012Output
        output =
        new SQLServer2012Output("AdventureWorks2012", new SQLServerOperationsMock());
    output.open();
    output.close();
  }

  @Test
  public void testOpenMethodCallsExecuteBeforeStatements() throws Exception {
    SQLServerOperationsMock operations = new SQLServerOperationsMock();
    SQLServer2012Output output = new SQLServer2012Output("AdventureWorks2012", operations);
    output.open();
    output.close();
    assertTrue(operations.hasReceivedExecuteBeforeStatementsCall());
  }

  @Test
  public void testExecuteMethodCallsExecuteProductStatements() throws Exception {
    SQLServerOperationsMock operations = new SQLServerOperationsMock();
    SQLServer2012Output output = new SQLServer2012Output("AdventureWorks2012", operations);
    output.execute(null);
    output.close();
    assertTrue(operations.hasReceivedExecuteProductStatementsCall());
  }

  @Test
  public void testExecuteCallsExecuteAfterStatements() throws Exception {
    SQLServerOperationsMock operations = new SQLServerOperationsMock();
    SQLServer2012Output output = new SQLServer2012Output("AdventureWorks2012", operations);
    output.close();
    assertTrue(operations.hasReceivedExecuteAfterStatementsCall());
  }
}
