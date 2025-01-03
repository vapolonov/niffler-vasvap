package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;

public class IssueExtension implements ExecutionCondition {

    private static final GhApiClient ghApiClient = new GhApiClient();

    @SneakyThrows
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        return AnnotationSupport.findAnnotation(  // ищем аннотацию над тестовым методом
                context.getRequiredTestMethod(),
                DisabledByIssue.class
        ).or(
                () -> AnnotationSupport.findAnnotation(  // если не находим над тестом, то ищем над тестовым классом и над родительскими классами
                        context.getRequiredTestClass(),  // ищем над тестовым классом
                        DisabledByIssue.class,
                        SearchOption.INCLUDE_ENCLOSING_CLASSES  // если класс является вложенным, то ищем над родительским классом
                )
        ).map(  // в методе map мы описываем функцию, она принимает на вход то, что мы нашли (аннотацию byIssue)
                byIssue -> "open".equals(ghApiClient.issueState(byIssue.value()))  // проверяем статус Issue
                        ? ConditionEvaluationResult.disabled("Disabled by issue #" + byIssue.value())  // если статус "open", то устанавливаем Disabled
                        : ConditionEvaluationResult.enabled("Issue closed")  // иначе Enabled
        ).orElseGet(
                () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue not found") // аннотация не найдена, тест по умолчанию Enabled
        );
    }
}
