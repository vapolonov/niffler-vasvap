package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoriesApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.test.web.utils.RandomDataUtils.randomUsername;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoriesApiClient categoriesApiClient = new CategoriesApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    CategoryJson categoryJson;
                    String user;
                    if (anno.categories().length > 0) {
                        user = anno.username();
                        Category category = anno.categories()[0];
                        categoryJson = new CategoryJson(
                                null,
                                randomUsername(),
                                user,
                                category.archived()
                        );


                        CategoryJson created = categoriesApiClient.addCategories(categoryJson);

                        if (anno.categories()[0].archived()) {
                            CategoryJson archivedCategory = new CategoryJson(
                                    created.id(),
                                    created.name(),
                                    user,
                                    true
                            );
                            created = categoriesApiClient.updateCategory(archivedCategory);
                        }
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                created
                        );
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(CategoryExtension.NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (category != null) {
            if (!category.archived()) {
                category = new CategoryJson(
                        category.id(),
                        category.name(),
                        category.username(),
                        true
                );
                categoriesApiClient.updateCategory(category);
            }
        }
    }
}
