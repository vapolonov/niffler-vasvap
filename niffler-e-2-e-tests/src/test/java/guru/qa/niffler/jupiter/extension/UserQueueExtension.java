package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.*;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);

    public record StaticUser(String username, String password, boolean empty) {}

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    // ConcurrentLinkedQueue — это класс в Java, который формирует неблокирующую, основанную на связанных узлах очередь,
    // ориентированную на многопоточное исполнение.
    private static final Queue<StaticUser> NOT_EMPTY_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("empty_vasvap", "12345", true));
        NOT_EMPTY_USERS.add(new StaticUser("takemusu", "12345", false));
        NOT_EMPTY_USERS.add(new StaticUser("vasya", "12345", false));
        NOT_EMPTY_USERS.add(new StaticUser("vasvap", "12345", false));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        boolean empty() default true;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(UserType.class))
                .findFirst()
                .map(parameter -> parameter.getAnnotation(UserType.class))
                .ifPresent(type -> {
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {  // создаем цикл, который будет ждать до 30 сек
                        user = type.empty()  // если userType empty
                                ? Optional.ofNullable(EMPTY_USERS.poll())  // достаем пользователя EMPTY_USERS из очереди
                                : Optional.ofNullable(NOT_EMPTY_USERS.poll());  // достаем пользователя NOT_EMPTY_USERS из очереди
                    }
                    Allure.getLifecycle().updateTestCase(testCase ->  // сбрасываем время начала теста в аллюр отчете
                            testCase.setStart(new Date().getTime())  // чтобы время считалось с момента выбора пользователя
                    );
                    user.ifPresentOrElse(
                            u -> {
                                context.getStore(NAMESPACE)
                                        .put(context.getUniqueId(), u);
                            },
                            () -> {
                                throw new IllegalStateException("Can`t obtain user after 30s.");
                            }
                    );
                });
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        StaticUser user = context.getStore(NAMESPACE).get(context.getUniqueId(), StaticUser.class);
        if (user.empty()) {
            EMPTY_USERS.add(user);
        } else {
            NOT_EMPTY_USERS.add(user);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), StaticUser.class);
    }
}
