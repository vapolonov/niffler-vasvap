package guru.qa.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestMethodContextExtension implements BeforeEachCallback, AfterEachCallback {

    // в beforeEach к нам приходит context (сам Junit нам его дает) в этот момент мы можем его куда-то сохранить,
    // а потом, при вызове метода context, из этого хранилища достать и вернуть
    // а в afterEach мы можем это хранилище за собой почистить
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Holder.INSTANCE.set(context);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Holder.INSTANCE.remove();
    }

    private enum Holder {
        INSTANCE;

        // ThreadLocal - тип данных в Java, который позволяет хранить какие-то объекты с привязкой к потоку, в котором они создавались
        private final  ThreadLocal<ExtensionContext> store = new ThreadLocal<>();

        public void set(ExtensionContext context) {
            store.set(context);
        }

        public ExtensionContext get() {
            return store.get();
        }

        public void remove() {
            store.remove();;
        }
    }

    public static ExtensionContext context() {
        return Holder.INSTANCE.get();
    }
}
