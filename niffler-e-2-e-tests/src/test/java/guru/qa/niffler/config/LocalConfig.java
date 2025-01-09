package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

enum LocalConfig implements Config {
  INSTANCE;

  static {
    Configuration.timeout = 8000;
  }

  @Override
  public String frontUrl() {
    return "http://127.0.0.1:3000/";
  }

  @Override
  public String spendUrl() {
    return "http://127.0.0.1:8093/";
  }

  @Override
  public String ghUrl() {
    return "https://api.github.com/";
  }
}
