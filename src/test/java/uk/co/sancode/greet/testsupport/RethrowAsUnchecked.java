package uk.co.sancode.greet.testsupport;


public class RethrowAsUnchecked {
    @SuppressWarnings("unchecked")
    public static <T, E extends Throwable> T uncheck(ThrowingCallable<T> callable) throws E {
        try {
            return callable.call();
        } catch (Throwable e) {
            throw (E) e;
        }
    }

    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void uncheck(ThrowingRunnable runnable) throws E {
        try {
            runnable.run();
        } catch (Throwable e) {
            throw (E) e;
        }
    }

    @FunctionalInterface
    @SuppressWarnings("java:S112")
    public interface ThrowingRunnable {
        void run() throws Throwable;
    }

    @FunctionalInterface
    @SuppressWarnings("java:S112")
    public interface ThrowingCallable<T> {
        T call() throws Throwable;
    }

    @FunctionalInterface
    @SuppressWarnings("java:S112")
    public interface ThrowingConsumer<T> {
        void accept(T t) throws Throwable;
    }

    @FunctionalInterface
    @SuppressWarnings("java:S112")
    public interface ThrowingFunction<T, R> {
        R apply(T t) throws Throwable;
    }
}
