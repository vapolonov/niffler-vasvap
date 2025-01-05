package guru.qa.niffler.test.web.utils;

import net.datafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUsername() {
        return faker.internet().username();
    }

    public static String randomPassword() {
        return faker.internet().password(5, 10);
    }

    public static String randomName() {
        return faker.name().name();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return faker.name().fullName();
    }

    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }


}
