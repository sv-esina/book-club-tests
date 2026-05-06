package models.reviews;


import java.util.Map;

public record SuccessfulCreateAndUpdateReviewResponseModel(Integer id, Integer club, Map<String, Object> user, String review,
                                                           Integer assessment, Integer readPages, String created,
                                                           String modified) {}

