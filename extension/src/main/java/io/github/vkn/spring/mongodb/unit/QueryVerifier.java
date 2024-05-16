package io.github.vkn.spring.mongodb.unit;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

class QueryVerifier {

    private QueryVerifier() {
    }

    private static List<MongoDbQueryTest> getAnnotations(Method method) {
        return Optional.ofNullable(method)
                .map(mtd -> mtd.getAnnotationsByType(MongoDbQueryTest.class))
                .map(a -> Stream.of(a).toList())
                .orElse(List.of());
    }

    static void verify(Method requiredTestMethod, MongoDbUnitCommandListener listener) {

        for (var annotation : getAnnotations(requiredTestMethod)) {
            var cfg = new Config(annotation);
            long commandCount = listener.getCommands()
                    .stream()
                    .filter(cfg::matchesDb)
                    .filter(cfg::matchesCollection)
                    .filter(cfg::matchesName)
                    .count();
            assertCounts(annotation, commandCount, cfg);
        }

        listener.stop();
    }

    private static void assertCounts(MongoDbQueryTest annotation, long commandCount, Config cfg) {
        int atLeast = annotation.atLeast();
        int atMost = annotation.atMost();
        long exactly = annotation.exactly();
        assertCounts(atLeast, cnt -> cnt < atLeast, "at least", commandCount, cfg);
        assertCounts(atMost, cnt -> cnt > atMost, "at most", commandCount, cfg);
        assertCounts(exactly, cnt -> cnt != exactly, "exactly", commandCount, cfg);
    }

    private static void assertCounts(Number expected, Predicate<? super Long> isViolated, String errorType, long commandCount, Config cfg) {
        if (expected != null && expected.intValue() > -1 && isViolated.test(commandCount)) {
            String constrains = cfg.constrains().isEmpty() ? "" : " for " + cfg.constrains();
            throw new AssertionError("Mongodb extension requires %s %d commands%s, but count is %d".formatted(errorType,
                    expected.longValue(), constrains, commandCount));
        }
    }
}
