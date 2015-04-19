package drexel.info634.amazon.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import drexel.info634.amazon.parser.dto.CategoriesDTO;
import drexel.info634.amazon.parser.dto.CategoryDTO;
import drexel.info634.amazon.parser.dto.CategoryItemDTO;
import drexel.info634.amazon.parser.dto.ProductDTO;
import drexel.info634.amazon.parser.dto.ReviewDTO;
import drexel.info634.amazon.parser.dto.ReviewsDTO;

/**
 * Creates a {@code ProductDTO} out of raw text data. Created by Rob on 4/16/2015.
 */
class ProductFactory {

  private List<String> dataLines;
  private ProductDTO productDTO;
  private int currentLineIndex;

  ProductDTO build(String[] data) throws InvalidAttributesException {
    dataLines = new ArrayList<>();
    Collections.addAll(dataLines, data);
    currentLineIndex = 0;
    productDTO = new ProductDTO();

    setID();
    setASIN();
    setDiscontinued();
    if (!productDTO.discontinued.equals("discontinued product")) {
      setTitle();
      setGroup();
      setSalesrank();
      setSimilar();
      setCategories();
      setReviews();
    }
    return productDTO;
  }

  // PRIVATE METHODS

  private void setID() throws InvalidAttributesException {
    String line = dataLines.get(currentLineIndex);
    if (LineParser.isID(line)) {
      productDTO.id = LineParser.parseID(line);
      currentLineIndex++;
    } else {
      throw new InvalidAttributesException(
          "ID not found on line " + currentLineIndex + ": " + line);
    }
  }

  private void setASIN() throws InvalidAttributesException {
    String line = dataLines.get(currentLineIndex);
    if (LineParser.isASIN(line)) {
      productDTO.asin = LineParser.parseASIN(line);
      currentLineIndex++;
    } else {
      throw new InvalidAttributesException(
          "ASIN not found on line " + currentLineIndex + ": " + line);
    }
  }

  private void setDiscontinued() {
    String line = dataLines.get(currentLineIndex);
    if (LineParser.isDiscontinuedProduct(line)) {
      productDTO.discontinued = "discontinued product";
      currentLineIndex++;
    } else {
      productDTO.discontinued = "product available";
    }
  }

  private void setTitle() throws InvalidAttributesException {
    String line = dataLines.get(currentLineIndex);
    if (LineParser.isTitle(line)) {
      productDTO.title = LineParser.parseTitle(line);
      currentLineIndex++;
    } else {
      throw new InvalidAttributesException(
          "Title not found on line " + currentLineIndex + ": " + line);
    }
  }

  private void setGroup() throws InvalidAttributesException {
    String line = dataLines.get(currentLineIndex);
    if (LineParser.isGroup(line)) {
      productDTO.group = LineParser.parseGroup(line);
      currentLineIndex++;
    } else {
      throw new InvalidAttributesException(
          "Group not found on line " + currentLineIndex + ": " + line);
    }
  }

  private void setSalesrank() throws InvalidAttributesException {
    String line = dataLines.get(currentLineIndex);
    if (LineParser.isSalesrank(line)) {
      productDTO.salesrank = LineParser.parseSalesrank(line);
      currentLineIndex++;
    } else {
      throw new InvalidAttributesException(
          "Salesrank not found on line " + currentLineIndex + ": " + line);
    }
  }

  private void setSimilar() throws InvalidAttributesException {
    String line = dataLines.get(currentLineIndex);
    if (!LineParser.isSimilar(line)) {
      throw new InvalidAttributesException(
          "Similar not found on line " + currentLineIndex + ": " + line);
    }
    int similarCount = Integer.parseInt(LineParser.parseSimilarCount(line));
    productDTO.similarCount = String.valueOf(similarCount);
    if (similarCount > 0) {
      productDTO.similarItems = LineParser.parseSimilarItems(line);
    }
    currentLineIndex++;
  }

  private void setCategories() throws InvalidAttributesException {
    // Ensure line is actually the beginning of the categories data
    String line = dataLines.get(currentLineIndex);
    if (!LineParser.isCategories(line)) {
      throw new InvalidAttributesException(
          "Categories not found on line " + currentLineIndex + ": " + line);
    }

    // Initialize collection we will eventually set ProductDTO.categories as
    CategoriesDTO categories = new CategoriesDTO();

    // Set categories count
    int categoriesCount = Integer.parseInt(LineParser.parseCategoriesCount(line));
    categories.setCount(String.valueOf(categoriesCount));

    // Use categories count to determine how many lines we need to parse
    int lastCategoryLineIndex = currentLineIndex + categoriesCount;

    // Parse each line into a CategoryDTO and add to categories
    while (currentLineIndex < lastCategoryLineIndex) {
      currentLineIndex++;
      line = dataLines.get(currentLineIndex);
      CategoryDTO category = new CategoryDTO();

      // Parse line into category items and add to CategoryDTO
      String[] categoryItemStrings = LineParser.parseCategoryItems(line);
      for (String categoryItemString : categoryItemStrings) {
        String name = LineParser.parseCategoryItemName(categoryItemString);
        String id = LineParser.parseCategoryItemID(categoryItemString);
        CategoryItemDTO item = new CategoryItemDTO(name, id);
        category.add(item);
      }
    }

    currentLineIndex++;
    productDTO.categories = categories;
  }

  private void setReviews() throws InvalidAttributesException {
    String line = dataLines.get(currentLineIndex);
    if (!LineParser.isReviews(line)) {
      throw new InvalidAttributesException(
          "Reviews not found on line " + currentLineIndex + ": " + line);
    }

    // Create the ReviewsDTO we will set
    ReviewsDTO reviews = new ReviewsDTO();

    // Set attributes
    reviews.setTotal(LineParser.parseReviewsTotal(line));
    reviews.setDownloaded(LineParser.parseReviewsDownloaded(line));
    reviews.setAvgRating(LineParser.parseReviewsAvgRating(line));

    // Move to start of reviews
    currentLineIndex++;

    // Parse each review into ReviewDTO until we run out of lines (reviews are last in the data)
    while (currentLineIndex <= dataLines.size() - 1) {
      line = dataLines.get(currentLineIndex);
      ReviewDTO review = new ReviewDTO();
      review.setDate(LineParser.parseReviewDate(line));
      review.setCustomer(LineParser.parseReviewCustomer(line));
      review.setRating(LineParser.parseReviewRating(line));
      review.setVotes(LineParser.parseReviewVotes(line));
      review.setHelpful(LineParser.parseReviewHelpful(line));
      reviews.add(review);
      currentLineIndex++;
    }

    // Set reviews to productDTO
    productDTO.reviews = reviews;
  }

}
