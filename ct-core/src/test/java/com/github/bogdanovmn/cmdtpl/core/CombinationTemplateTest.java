package com.github.bogdanovmn.cmdtpl.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombinationTemplateTest {

    private static Stream<Arguments> instancesTotalParams() {
        return Stream.of(
            Arguments.of("cmd -x{1:3:1} -y{10:100:20} something else", 18),
            Arguments.of("cmd -x -y something else", 1),
            Arguments.of("cmd -x{1:1:1} -y{1:1:1} something else", 1)
        );
    }

    private static Stream<Arguments> instancesParams() {
        return Stream.of(
            Arguments.of(
                "cmd -x{1:2:1} -y{10:40:20} something else",
                Arrays.asList(
                    "cmd -x1 -y10 something else",
                    "cmd -x1 -y30 something else",
                    "cmd -x1 -y40 something else",
                    "cmd -x2 -y10 something else",
                    "cmd -x2 -y30 something else",
                    "cmd -x2 -y40 something else"
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("instancesTotalParams")
    void instancesTotal(String expr, int countInstancesExpected) {
        CombinationTemplate template = new CombinationTemplateExpression(expr).template();
        assertEquals(countInstancesExpected, template.totalInstances());
    }

    @ParameterizedTest
    @MethodSource("instancesParams")
    void instances(String expr, List<String> instancesExpected) {
        CombinationTemplate template = new CombinationTemplateExpression(expr).template();
        assertEquals(instancesExpected, template.instances());
    }
}
