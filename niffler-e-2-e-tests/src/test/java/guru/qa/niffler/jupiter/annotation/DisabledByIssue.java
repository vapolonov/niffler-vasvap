package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.IssueExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  // означает, что мы сможем прочитать аннотацию через Java Reflection API
@Target({ElementType.METHOD, ElementType.TYPE})  // где мы будем ставить данную аннотацию METHOD - метод (над тестом), TYPE - класс (над тестовым классом)
@ExtendWith(IssueExtension.class)
public @interface DisabledByIssue {
    String value();
}
