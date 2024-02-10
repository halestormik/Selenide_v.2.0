import com.codeborne.selenide.SelectorMode;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static java.time.LocalTime.now;

public class CardDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldTestFormWithCorrectData() { // проверка Happy Path
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");

        Calendar c = Calendar.getInstance(); // текущая дата
        c.add(Calendar.DATE, 5); // прибавление 5 дней к текущей дате
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy"); // формат представления даты
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(formatForDateNow.format(c.getTime()));

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldTestFormWithInCorrectCityName() { // проверка валидации поля "город"
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Моосква");

        Calendar c = Calendar.getInstance(); // текущая дата
        c.add(Calendar.DATE, 5); // прибавление 5 дней к текущей дате
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy"); // формат представления даты
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(formatForDateNow.format(c.getTime()));

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();

        $("[data-test-id=city] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна")); // проверка текста ошибки
        boolean b = $("[data-test-id=city].input_invalid").isDisplayed(); // проверка наличия класса input_invalid
        Assertions.assertTrue(b);
    }

    @Test
    public void shouldTestFormWithInCorrectDate() { // проверка валидации поля "дата"
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");

        Calendar c = Calendar.getInstance(); // текущая дата
        c.add(Calendar.DATE, -5); // отнимание 5 дней от текущей даты
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy"); // формат представления даты
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(formatForDateNow.format(c.getTime()));

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();

        $("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен")); // проверка текста ошибки
        boolean b = $("[data-test-id=date] .input_invalid").isDisplayed(); // проверка наличия класса input_invalid
        Assertions.assertTrue(b);
    }

    @Test
    public void shouldTestFormWithInCorrectName() { // проверка валидации поля "Фамилия и имя"
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");

        Calendar c = Calendar.getInstance(); // текущая дата
        c.add(Calendar.DATE, 5); // прибавление 5 дней к текущей дате
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy"); // формат представления даты
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(formatForDateNow.format(c.getTime()));

        form.$("[data-test-id=name] input").setValue("568# $$@");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();

        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")); // проверка текста ошибки
        boolean b = $("[data-test-id=name].input_invalid").isDisplayed(); // проверка наличия класса input_invalid
        Assertions.assertTrue(b);
    }

    @Test
    public void shouldTestFormWithInCorrectPhone() { // проверка валидации поля "Телефон"
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");

        Calendar c = Calendar.getInstance(); // текущая дата
        c.add(Calendar.DATE, 5); // прибавление 5 дней к текущей дате
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy"); // формат представления даты
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(formatForDateNow.format(c.getTime()));

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+7981sacsa881255s+");
        form.$("[data-test-id=agreement]").click();
        form.$$("button").last().click();

        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")); // проверка текста ошибки
        boolean b = $("[data-test-id=phone].input_invalid").isDisplayed(); // проверка наличия класса input_invalid
        Assertions.assertTrue(b);
    }

    @Test
    public void shouldTestFormWithInactiveCheckbox() { // проверка валидации чекбокса
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Москва");

        Calendar c = Calendar.getInstance(); // текущая дата
        c.add(Calendar.DATE, 5); // прибавление 5 дней к текущей дате
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy"); // формат представления даты
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a" + Keys.BACK_SPACE); // очистка поля даты
        form.$("[data-test-id=date] input").setValue(formatForDateNow.format(c.getTime()));

        form.$("[data-test-id=name] input").setValue("Васильев Петр");
        form.$("[data-test-id=phone] input").setValue("+79811567894");
        form.$$("button").last().click();

        boolean b = $("[data-test-id=agreement].input_invalid").isDisplayed(); // проверка наличия класса input_invalid
        Assertions.assertTrue(b);
    }
}
