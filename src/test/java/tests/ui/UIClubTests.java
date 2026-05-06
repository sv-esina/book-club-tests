package tests.ui;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import pages.ClubsPage;
import tests.TestBase;


@Owner("Esina Svetlana")
@Feature("Работа с книжными клубами")
@DisplayName("[UI] Проверяем работу с клубами")
public class UIClubTests extends TestBase {
    ClubsPage clubsPage = new ClubsPage();

    @Test
    @Tags({@Tag("UI"), @Tag("clubs")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность принятия участия в книжных клубах")
    @DisplayName("[UI] Попытка вступить в книжный клуб неавторизованным пользователем")
    public void tryToJoinClubUnauthorizedUserUITest() {
        clubsPage
                .openMainPage()
                .joinAnyClub();
    }

    @Test
    @Tags({@Tag("UI"), @Tag("clubs")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность поиска книжного клуба")
    @DisplayName("[UI] Поиск книжного клуба по наименованию")
    public void searchClubByNameUITest() {
        clubsPage
                .openMainPage()
                .searchRandomClub();

    }
}
