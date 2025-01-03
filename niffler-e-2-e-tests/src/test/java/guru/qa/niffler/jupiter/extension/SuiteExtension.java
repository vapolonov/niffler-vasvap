/**
 * Т.к. в Junit пока нет extension BeforeSuiteCallback и AfterSuiteCallback, поэтому будем использовать интерфейсы
 * BeforeAllCallback и AfterAllCallBack, чтобы реализовать свой extension, который будет вести себя как BeforeSuiteCallback и AfterSuiteCallback
 */

package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public interface SuiteExtension extends BeforeAllCallback {

    /*
    1. Быть уверенными, что SuiteExtension будет выполняться перед каждым тестовым классом
    2. Если мы выполним какой-то код перед загрузкой самого первого тестового класса, то это и будет beforeSuite()
    3. При этом для 2, 3, 4 и т.д. тестовых классов не будем вызывать beforeSuite()
    4. Когда все тесты завершаться вызовем afterSuite()
     */
    @Override
    default void beforeAll(ExtensionContext context) throws Exception {
        // ExtensionContext это интерфейс и у него есть несколько реализаций и они как бы вложены друг в друга
        // (есть контекст всего test run-а, есть всего suite-а, есть тестового класса, тестового метода).
        // rootContext - контекст самого высокого уровня, это контекст запуска всего test run-а (всегда один)
        final ExtensionContext rootContext = context.getRoot();
        // Глобальное хранилище, вызывается по Namespace.GLOBAL, также является singleton т.е. один на весь проект
        rootContext.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent(  // позволяет достать значение по ключу, либо вычислить это значение с помощью функции, которая передана вторым аргументом
                        this.getClass(),
                        key -> { // попадаем только в самый первый раз
                            beforeSuite(rootContext);
                            // интерфейс CloseableResource наделяет любой объект, который его имплементирует, таким поведением,
                            // что при закрытии контекста (rootContext будет закрыт, когда закончатся все тесты),
                            // если в хранилище данного контекста есть объект, который является интерфейсом CloseableResource,
                            // то у него будет вызван метод close
                            return new ExtensionContext.Store.CloseableResource() {
                                @Override
                                public void close() throws Throwable {
                                    afterSuite();
                                }
                            };
                        }
                );
    }

    default void beforeSuite(ExtensionContext context) {
    }

    default void afterSuite() {
    }
}
