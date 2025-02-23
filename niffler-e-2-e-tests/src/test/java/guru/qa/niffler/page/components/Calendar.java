package guru.qa.niffler.page.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Month;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Selenide.$;
import static java.util.Calendar.*;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent<Calendar> {

    public Calendar(SelenideElement self) {
        super(self);
    }

    public Calendar() {
    super($(".MuiPickersLayout-root"));
  }

    private final SelenideElement
            self = $(".MuiPickersLayout-root"),
            dateInput = $("input[name='date']"),
            calendarBtn = $("button[aria-label*='Choose date']"),
            prevMonthBtn = self.$("button[title='Previous month']"),
            nextMonthBtn = self.$("button[title='Next month']"),
            currentMonthAndYear = self.$(".MuiPickersCalendarHeader-label");

    private final ElementsCollection dateRows = self.$$(".MuiDayCalendar-weekContainer");



    @Step("Ввести дату в календаре {0}")
    public Calendar typeDateInCalendar(String date) {
        self.setValue(date);
        return this;
    }

    @Step("Выбрать в календаре {0}")
    public void selectDateInCalendar(Date date) {
        java.util.Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        calendarBtn.click();
        final int desiredMonthIndex = cal.get(MONTH);
        selectYear(cal.get(YEAR));
        selectMonth(desiredMonthIndex);
        selectDay(cal.get(DAY_OF_MONTH));
    }

    private void selectYear(int expectedYear) {
        int actualYear = getActualYear();

        while (actualYear > expectedYear) {
            prevMonthBtn.click();
            Selenide.sleep(200);
            actualYear = getActualYear();
        }
        while (actualYear < expectedYear) {
            nextMonthBtn.click();
            Selenide.sleep(200);
            actualYear = getActualYear();
        }
    }

    private void selectMonth(int desiredMonthIndex) {
        int actualMonth = getActualMonthIndex();

        while (actualMonth > desiredMonthIndex) {
            prevMonthBtn.click();
            Selenide.sleep(200);
            actualMonth = getActualMonthIndex();
        }
        while (actualMonth < desiredMonthIndex) {
            nextMonthBtn.click();
            Selenide.sleep(200);
            actualMonth = getActualMonthIndex();
        }
    }

    private void selectDay(int desiredDay) {
        ElementsCollection rows = dateRows.snapshot();

        for (SelenideElement row : rows) {
            ElementsCollection days = row.$$("button").snapshot();
            for (SelenideElement day : days) {
                if (day.getText().equals(String.valueOf(desiredDay))) {
                    day.click();
                    return;
                }
            }
        }
    }

    private String getMonthNameByIndex(int mothIndex) {
        return Month.of(mothIndex + 1).name();
    }

    private int getActualMonthIndex() {
        return Month.valueOf(currentMonthAndYear.getText()
                        .split(" ")[0]
                        .toUpperCase())
                .ordinal();
    }

    private int getActualYear() {
        return Integer.parseInt(currentMonthAndYear.getText().split(" ")[1]);
    }
}
