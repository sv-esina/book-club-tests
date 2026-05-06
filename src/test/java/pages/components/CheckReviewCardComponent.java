package pages.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;

public class CheckReviewCardComponent {
    public void checkingResultValues(SelenideElement key, String value) {
        key.shouldHave(text(value));

    }

    public void findNotMineReviewCard(SelenideElement key, String value) {
        key.shouldNotHave(text(value));

    }

}
