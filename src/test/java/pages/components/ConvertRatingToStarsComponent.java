package pages.components;

public class ConvertRatingToStarsComponent {

    public String ratingValueToStars(Integer ratingValue) {
        int ratingMax = 5;

        return "★".repeat(ratingValue) + "☆".repeat(ratingMax - ratingValue);
    }
}
