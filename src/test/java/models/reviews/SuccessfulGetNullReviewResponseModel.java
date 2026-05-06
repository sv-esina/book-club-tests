package models.reviews;

import java.util.List;


public record SuccessfulGetNullReviewResponseModel(Integer count, String next, String previous,
                                                   List<String> results) {}

