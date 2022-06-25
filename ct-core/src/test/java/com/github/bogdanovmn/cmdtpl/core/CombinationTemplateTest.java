package com.github.bogdanovmn.cmdtpl.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombinationTemplateTest {

    private static Stream<Arguments> instancesTotalParams() {
        return Stream.of(
            Arguments.of("cmd -x{1:3:1} -y{10:100:20} something else", 15)
//            Arguments.of("cmd -x -y something else", 1),
//            Arguments.of("cmd -x{1:1:1} -y{1:1:1} something else", 1)
        );
    }

    private static Stream<Arguments> instancesParams() {
        return Stream.of(
            Arguments.of("x{#}", "1:3:1", Arrays.asList("x1", "x2", "x3")),
            Arguments.of("x{#}", "1:5:2", Arrays.asList("x1", "x3", "x5")),
            Arguments.of("x{#}", "1:5:3", Arrays.asList("x1", "x4", "x5")),
            Arguments.of("x{#}", "1:2:3", Arrays.asList("x1", "x2")),
            Arguments.of("x{#}", "-1:6:3", Arrays.asList("x-1", "x2", "x5", "x6")),
            Arguments.of("x{#}", "-5:-1:3", Arrays.asList("x-5", "x-2", "x-1")),
            Arguments.of("x{#}", "-5:0:3", Arrays.asList("x-5", "x-2", "x0"))
        );
    }

    @ParameterizedTest
    @MethodSource("instancesTotalParams")
    void instancesTotal(String expr, int countInstancesExpected) {
        CombinationTemplate template = new CombinationTemplateExpression(expr).template();
        assertEquals(countInstancesExpected, template.totalInstances());
    }
}