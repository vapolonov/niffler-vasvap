package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username, String password, String friend, String income, String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("empty_vasap", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("vasvap", "12345", "takemusu", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("vasya", "12345", null, "takemusu", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("takemusu", "12345", null, null, "vasya"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> AnnotationSupport.isAnnotated(parameter, UserType.class) &&
                        parameter.getType().isAssignableFrom(StaticUser.class))
                .map(parameter -> parameter.getAnnotation(UserType.class))
                .forEach(userType -> {
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();

                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = switch (userType.value()) {
                            case EMPTY -> Optional.ofNullable(EMPTY_USERS.poll());
                            case WITH_FRIEND -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
                            case WITH_INCOME_REQUEST -> Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
                            case WITH_OUTCOME_REQUEST -> Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
                        };
                    }
                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );
                    user.ifPresentOrElse(
                            usr -> {
                                ((Map<UserType, StaticUser>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
                                        context.getUniqueId(),
                                        key -> new HashMap<>()
                                )).put(userType, usr);
                            },
                            () -> {
                                throw new IllegalStateException("Can`t obtain user after 30s.");
                            }
                    );
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);

        if (map != null) {
            for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
                switch (e.getKey().value()) {
                    case EMPTY -> EMPTY_USERS.add(e.getValue());
                    case WITH_FRIEND -> WITH_FRIEND_USERS.add(e.getValue());
                    case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(e.getValue());
                    case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(e.getValue());
                }
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return (StaticUser) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
        .get(AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class).get());
    }
}
