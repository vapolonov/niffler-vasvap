package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    // NAMESPACE - уникальный идентификатор конкретного extension
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // ищем тестовый метод через контекст context.getRequiredTestMethod() у которого есть аннотация Spending при помощи метода findAnnotation
        // метод findAnnotation возвращает тип Optional (микро коллекция - контейнер для одного элемента, который либо есть, либо его нет)
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Spending.class)
                // метод ifPresent будет вызван только в том случае, если у теста нашлась аннотация Spending
                .ifPresent(anno -> {
                    // создаем объект SpendJson - это POJO объект, объект который нужно серилизовать и отправить на back (Java record)
                    SpendJson spend = new SpendJson(
                            null,
                            new Date(),
                            new CategoryJson(
                                    null,
                                    anno.category(),
                                    anno.username(),
                                    false
                            ),
                            CurrencyValues.RUB,
                            anno.amount(),
                            anno.description(),
                            anno.username()
                    );
                    // создаем spending через API (возвращает объект - созданный spending с id)
                    SpendJson createdSpend = spendApiClient.addSpend(spend);
                    // кладем созданный spending в контекст:
                    // getStore - создаем хранилище (похож на Map, данные хранятся ключ-значение), которое будет
                    // предназначено для хранения данных работы данного extension (SpendingExtension)
                    // на вход getStore передается NAMESPACE (уникальный идентификатор extension)
                    // в getStore можно передавать разные NAMESPACE и каждый раз будет создаваться новое хранилище (может быть много)
                    context.getStore(NAMESPACE).put(
                            // ключ - getUniqueId() (метод которые есть в ExtensionContext) уникальный идентификатор теста
                            // никогда не может быть пустым
                            context.getUniqueId(),
                            // значение - созданные spending
                            createdSpend
                    );
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
