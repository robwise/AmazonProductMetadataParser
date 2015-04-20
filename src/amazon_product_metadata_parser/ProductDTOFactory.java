package amazon_product_metadata_parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import amazon_product_metadata_parser.dto.CategoriesDTO;
import amazon_product_metadata_parser.dto.CategoryDTO;
import amazon_product_metadata_parser.dto.CategoryItemDTO;
import amazon_product_metadata_parser.dto.FailedValidationException;
import amazon_product_metadata_parser.dto.ProductDTO;
import amazon_product_metadata_parser.dto.ReviewDTO;
import amazon_product_metadata_parser.dto.ReviewsDTO;
import amazon_product_metadata_parser.dto.SimilarItemsDTO;

/**
 * Creates a {@link ProductDTO} out of raw text data.
 */
class ProductDTOFactory {

  private String[] dataLines;
  private int      currentLineIndex;

  ProductDTO build(String[] data) throws FailedValidationException {
    if (null == data) {
      throw new FailedValidationException("Data is null");
    } else if (0 == data.length) {
      throw new FailedValidationException("Data has 0 elements");
    }

    // Initialize
    dataLines = data;
    currentLineIndex = 0;

    // Initialize arguments to pass to ProductDTO constructor
    String id = getID();
    String asin = getASIN();
    String title = null;
    String group = null;
    int salesrank = -1;
    SimilarItemsDTO similarItems = null;
    CategoriesDTO categories = null;
    ReviewsDTO reviews = null;
    boolean discontinued = getDiscontinued();

    // If not discontinued, get non-discontinued data
    if (!discontinued) {
      title = getTitle();
      group = getGroup();
      salesrank = getSalesrank();
      similarItems = getSimilarItems();
      categories = getCategories();
      reviews = getReviews();
    }

    return new ProductDTO(id, asin, title, group, salesrank, similarItems, categories, reviews,
                          discontinued);
  }

  private String getID() throws FailedValidationException {
    String line = dataLines[currentLineIndex];
    if (LineParser.isID(line)) {
      currentLineIndex++;
      return LineParser.parseID(line);
    } else {
      throw new FailedValidationException(
          "ID not found on line " + currentLineIndex + ": " + line);
    }
  }

  private String getASIN() throws FailedValidationException {
    String line = dataLines[currentLineIndex];
    if (LineParser.isASIN(line)) {
      currentLineIndex++;
      return LineParser.parseASIN(line);
    } else {
      throw new FailedValidationException(
          "ASIN not found on line " + currentLineIndex + ": " + line);
    }
  }

  private boolean getDiscontinued() {
    String line = dataLines[currentLineIndex];
    return LineParser.isDiscontinuedProduct(line);
  }

  private String getTitle() throws FailedValidationException {
    String line = dataLines[currentLineIndex];
    if (LineParser.isTitle(line)) {
      currentLineIndex++;
      return LineParser.parseTitle(line);
    } else {
      throw new FailedValidationException(
          "Title not found on line " + currentLineIndex + ": " + line);
    }
  }

  private String getGroup() throws FailedValidationException {
    String line = dataLines[currentLineIndex];
    if (LineParser.isGroup(line)) {
      currentLineIndex++;
      return LineParser.parseGroup(line);
    } else {
      throw new FailedValidationException(
          "Group not found on line " + currentLineIndex + ": " + line);
    }
  }

  private int getSalesrank() throws FailedValidationException {
    String line = dataLines[currentLineIndex];
    if (LineParser.isSalesrank(line)) {
      currentLineIndex++;
      return Integer.valueOf(LineParser.parseSalesrank(line));
    } else {
      throw new FailedValidationException(
          "Salesrank not found on line " + currentLineIndex + ": " + line);
    }
  }

  private SimilarItemsDTO getSimilarItems() throws FailedValidationException {
    String line = dataLines[currentLineIndex];
    if (!LineParser.isSimilar(line)) {
      throw new FailedValidationException(
          "Similar not found on line " + currentLineIndex + ": " + line);
    }
    int similarCount = Integer.parseInt(LineParser.parseSimilarCount(line));
    String[] similarItemASINs = LineParser.parseSimilarItems(line);
    currentLineIndex++;
    return new SimilarItemsDTO(similarItemASINs, similarCount);
  }

  private CategoriesDTO getCategories() throws FailedValidationException {
    // Ensure line is actually the beginning of the categories data
    String line = dataLines[currentLineIndex];
    if (!LineParser.isCategories(line)) {
      throw new FailedValidationException(
          "Categories not found on line " + currentLineIndex + ": " + line);
    }

    // Set categories total
    int categoriesCount = Integer.parseInt(LineParser.parseCategoriesCount(line));
    List<CategoryDTO> categories = new ArrayList<>();

    // Use categories total to determine how many lines we need to parse
    int lastCategoryLineIndex = currentLineIndex + categoriesCount;

    // Parse lines into a CategoryDTOs
    while (currentLineIndex < lastCategoryLineIndex) {
      currentLineIndex++;
      line = dataLines[currentLineIndex];
      String[] categoryItemStrings = LineParser.parseCategoryItems(line);
      CategoryItemDTO[] categoryItems = new CategoryItemDTO[categoryItemStrings.length];

      // Parse a line into CategoryItemDTOs
      for (int i = 0; i < categoryItemStrings.length; i++) {
        String categoryItemString = categoryItemStrings[i];
        String name = LineParser.parseCategoryItemName(categoryItemString);
        String id = LineParser.parseCategoryItemID(categoryItemString);

        // If item is the first in the array, it is a parent category item and should have its
        // parent category item field reference itself
        String parentCategoryItem;
        if (i == 0) {
          parentCategoryItem = id;
        } else {
          parentCategoryItem = categoryItems[i - 1].id;
        }
        boolean highestParentFlag = i == 0;
        boolean lowestChildFlag = i == categoryItemStrings.length - 1;
        CategoryItemDTO item = new CategoryItemDTO(name, id, parentCategoryItem, highestParentFlag,
                                                   lowestChildFlag, i);
        categoryItems[i] = item;
      }
      categories.add(new CategoryDTO(categoryItems, categoryItems.length));
    }

    currentLineIndex++;
    return new CategoriesDTO(categories.toArray(new CategoryDTO[categories.size()]),
                             categoriesCount);
  }

  private ReviewsDTO getReviews() throws FailedValidationException {
    String line = dataLines[currentLineIndex];
    if (!LineParser.isReviews(line)) {
      throw new FailedValidationException(
          "Reviews not found on line " + currentLineIndex + ": " + line);
    }

    // Get reviews attributes
    int total = Integer.valueOf(LineParser.parseReviewsTotal(line));
    int downloaded = Integer.valueOf(LineParser.parseReviewsDownloaded(line));
    double avgRating = Double.valueOf(LineParser.parseReviewsAvgRating(line));
    ReviewDTO[] reviews = new ReviewDTO[downloaded];

    // Move to start of reviews
    currentLineIndex++;

    // Parse each review into ReviewDTO until we run out of lines (reviews are last in the data)
    int reviewNumber = 0;
    while (currentLineIndex <= dataLines.length - 1) {
      line = dataLines[currentLineIndex];
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
      LocalDate date = LocalDate.parse(LineParser.parseReviewDate(line), formatter);
      String customer = LineParser.parseReviewCustomer(line);
      int rating = Integer.valueOf(LineParser.parseReviewRating(line));
      int votes = Integer.valueOf(LineParser.parseReviewVotes(line));
      int helpful = Integer.valueOf(LineParser.parseReviewHelpful(line));
      reviews[reviewNumber] = new ReviewDTO(date, customer, rating, votes, helpful);
      reviewNumber++;
      currentLineIndex++;
    }

    // Set reviews to productDTO
    return new ReviewsDTO(total, downloaded, avgRating, reviews);
  }

}
