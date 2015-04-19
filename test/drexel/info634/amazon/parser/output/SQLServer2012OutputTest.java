package drexel.info634.amazon.parser.output;

import org.junit.Test;

/**
 * Created by Rob Wise <robert.wise@outlook.com> on 4/18/2015.
 */
public class SQLServer2012OutputTest {

  @Test
  public void testConnection() throws Exception {
    SQLServer2012Output output = new SQLServer2012Output("AdventureWorks2012");
    output.open();
    output.close();
  }
}
