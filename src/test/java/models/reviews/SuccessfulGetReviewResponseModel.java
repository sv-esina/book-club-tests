package models.reviews;

import java.util.List;


public record SuccessfulGetReviewResponseModel(Integer count, String next, String previous,
                                               List<SuccessfulCreateAndUpdateReviewResponseModel> results) {}

