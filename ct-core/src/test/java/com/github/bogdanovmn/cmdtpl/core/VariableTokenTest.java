package com.github.bogdanovmn.cmdtpl.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VariableTokenTest {

    private static Stream<Arguments> parameters() {
        return Stream.of(
            Arguments.of("x{#}", "1:3:1", Arrays.asList("x1", "x2", "x3")),
            Arguments.of("x{#}", "1:5:2", Arrays.asList("x1", "x3", "x5")),
            Arguments.of("x{#}", "1:5:3", Arrays.asList("x1", "x4", "x5")),
            Arguments.of("x{#}", "1:2:3", Arrays.asList("x1", "x2")),
            Arguments.of("x{#}", "-1:6:3", Arrays.asList("x-1", "x2", "x5", "x6")),
            Arguments.of("x{#}", "-5:-1:3", Arrays.asList("x-5", "x-2", "x-1")),
            Arguments.of("x{#}", "-5:0:3", Arrays.asList("x-5", "x-2", "x0")),
            Arguments.of("x{#}", "10:100:20", Arrays.asList("x10", "x30", "x50", "x70", "x90", "x100"))
        );
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void test(String expr, String params, List<String> instances) {
        VariableToken token = new VariableToken(
            expr,
            VariableParameters.of(params)
        );

        assertEquals(instances.size(), token.instancesCount());
        assertEquals(instances, token.instances());
    }
}
