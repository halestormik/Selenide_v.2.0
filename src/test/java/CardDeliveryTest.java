import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    public String generateDate(int days) { // метод генерации даты плюс n дней
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    public void shouldTestFormWithCorrectData() { // проверка Happy Path
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");
        String planningDate = generateDate(4); // дата +4 дня от текущей
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(planningDate);

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();
        // $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldTestFormWithInCorrectCityName() { // проверка валидации поля "город"
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Моосква");

        String planningDate = generateDate(4); // дата +4 дня от текущей
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(planningDate);

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();

        $("[data-test-id=city].input_invalid")
                .shouldHave(Condition.text("Доставка в выбранный город недоступна"))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldTestFormWithInCorrectDate() { // проверка валидации поля "дата"
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");

        String planningDate = generateDate(-5); // дата +4 дня от текущей
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(planningDate);

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();

        $("[data-test-id=date] .input_invalid")
                .shouldHave(Condition.text("Заказ на выбранную дату невозможен"))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldTestFormWithInCorrectName() { // проверка валидации поля "Фамилия и имя"
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");

        String planningDate = generateDate(4); // дата +4 дня от текущей
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(planningDate);

        form.$("[data-test-id=name] input").setValue("568# $$@");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();

        $("[data-test-id=name].input_invalid")
                .shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldTestFormWithInCorrectPhone() { // проверка валидации поля "Телефон"
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");

        String planningDate = generateDate(4); // дата +4 дня от текущей
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(planningDate);

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+7981sacsa881255s+");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();

        $("[data-test-id=phone].input_invalid")
                .shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldTestFormWithInactiveCheckbox() { // проверка валидации чекбокса
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");

        String planningDate = generateDate(4); // дата +4 дня от текущей
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(planningDate);

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$$("button").last().click();

        $("[data-test-id=agreement].input_invalid").shouldBe(visible); // проверка наличия класса input_invalid
    }
}
