# AmazonProductMetadataParser
Parses Amazon product metadata contained in the file `amazon-meta.txt` scraped by Stanford University's SNAP library:

**[Amazon Metadata Project on SNAP](https://snap.stanford.edu/data/amazon-meta.html)**

## Getting Started
+ Clone the project to your hard drive
+ Download [amazon-meta.txt.gz](https://snap.stanford.edu/data/bigdata/amazon/amazon-meta.txt.gz) from the SNAP library
+ Unzip it into the project's `Resources/` folder and ensure it is named `amazon-meta.txt`
+ Make sure you are using Java SE 8
+ Compile and run `Parser.java` to see the parsed data output to your console

## Modifying
You will most likely want to do something other than simply print the data to your console. To output the data according to your own needs, use one of the preconfigured `Output`-implementing classes in the `output` package—or make your own! Parsed data is represented as a `ProductDTO`, which is loosely speaking (*sigh* programmer semantics...) a [Data Transfer Objects](http://en.wikipedia.org/wiki/Data_transfer_object). The main type is `ProductDTO`, but it has children DTOs for multivalued attributes such as reviews. This design was chosen to make mapping to relational databases more conceptually clear.

Here is an example of using the `Parser` with `ConsoleOutput`.

```java
import drexel.info634.amazon.parser.Parser;
import drexel.info634.amazon.parser.output.ConsoleOutput;
import drexel.info634.amazon.parser.output.Output;

import java.io.IOException;

public class YourClass {
    public void yourMethod() {
        String pathToAmazonMetaFile = "{your path here}";
        Output output = new ConsoleOutput();

        // Create the Parser object
        Parser parser = new Parser(pathToAmazonMetaFile, output);

        // Start the parsing process, which will call the Output object's
        // createProduct() method 548,552 times, each time passing in a ProductDTO object
        try {
            parser.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```

Because the dataset is so large, the program sends the parsed `ProductDTO` objects to the designated `Output` class as it parses them (as opposed to waiting until the end) much like a stream. This avoids trying to keep 548,552 objects in memory! All you need to do is create an object that implements the interface and put what you want to do with each `ProductDTO` in the `createProduct(ProductDTO productDTO`) method. For example, here is the `ConsoleOutput` object:

```java
package drexel.info634.amazon.parser.output;

import java.io.IOException;

import drexel.info634.amazon.parser.dto.ProductDTO;

/**
 * Simply prints product data to the console
 */
public class ConsoleOutput implements Output {

  @Override
  public void open() throws IOException {
    // do nothing
  }

  @Override
  public void createProduct(ProductDTO productDTO) throws IOException {
    System.out.println(productDTO.toString());
  }

  @Override
  public void close() throws IOException {
    // do nothing
  }
}
```

The `open` and `close` methods will be called before and after parsing, respectively, and are provided in case there are setup or teardown tasks (`Output` implements `java.io.Closeable`). If an `IOException` occurs, parsing will cease.

### Output

#### SQLServer2012Output (in development)
Currently in development. Connects to your default SQL Server 2012 instance via Windows authentication. *If you don't know what an instance is, don't worry about it, but it's usually named "MSSQLSERVER" or "SQLEXPRESS").*

##### Requirements
+ Requires use of the `sqljdbc41.jar` library ([Microsoft Download](https://www.microsoft.com/en-us/download/details.aspx?displaylang=en&id=11774))
+ Requires the `sqljdbc_auth.dll` (included with the download from Microsoft described above) be loaded in the JavaVM. The easiest method is to copy the file from either `sqljdbc_4.1\enu\auth\x64\sqljdbc_auth.dll` or `sqljdbc_4.1\enu\auth\x86\sqljdbc_auth.dll` depending on if your JRE is 32-bit or 64-bit, and place it your JRE's `bin`folder. For example, place the x64 version of the .dll on a 64-bit machine in the: `C:\Program Files\Java\jdk1.8.0_31\jre\bin\` directory. Make sure to use the appropriate 32-bit or 64-bit version of the `.dll` to match your JRE.
+ Requires `SQL Server` service to be running for your default instance
+ Requires `SQL Server Browser` service also be running

## Contributing
Feel free to fork this project or submit a [pull request](https://github.com/robwise/AmazonProductMetadataParser/pulls).

## Reporting Bugs
Please use the [issues page](https://github.com/robwise/AmazonProductMetadataParser/issues) for reporting any bugs you encounter.

## Authors
Rob Wise
