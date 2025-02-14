package guru.qa.niffler.utils;

import net.datafaker.Faker;

public class RandomDataUtils {

  private static final Faker faker = new Faker();

  public static String randomUsername() {
    return faker.internet().username();
  }

  public static String randomName() {
    return faker.name().firstName();
  }

  public static String randomSurname() {
    return faker.name().lastName();
  }

  public static String randomCategoryName() {
    return faker.food().fruit();
  }

  public static String randomSentence(int wordsCount) {
    return faker.lorem().sentence(wordsCount);
  }
}
