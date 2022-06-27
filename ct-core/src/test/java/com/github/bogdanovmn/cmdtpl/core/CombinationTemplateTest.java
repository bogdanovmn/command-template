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
            Arguments.of("cmd -x{1:1:1} -y{1:1:1} something else", 1),
            Arguments.of("cmd -m{1:4:1} -p{0:100:1} -e{0:20:1} -n{2:50:2} -b{50:200:15} -s{0:200:20} something else", 25664100)
        );
    }

    private static Stream<Arguments> instancesParams() {
        return Stream.of(
            Arguments.of(
                "cmd -x{1:2:1} -y {10:40:20} something else",
                Arrays.asList(
                    "cmd -x1 -y 10 something else",
                    "cmd -x1 -y 30 something else",
                    "cmd -x1 -y 40 something else",
                    "cmd -x2 -y 10 something else",
                    "cmd -x2 -y 30 something else",
                    "cmd -x2 -y 40 something else"
                )
            ),
            Arguments.of(
                "cmd -x{1:2:1} -y {10:30:20:z} /path/to/file_{#}.png",
                Arrays.asList(
                    "cmd -x1 -y 10 /path/to/file_x1_z10.png",
                    "cmd -x1 -y 30 /path/to/file_x1_z30.png",
                    "cmd -x2 -y 10 /path/to/file_x2_z10.png",
                    "cmd -x2 -y 30 /path/to/file_x2_z30.png"
                )
            ),
            Arguments.of(
                "cmd -x {1:2:1} -y {1:1:1:z} /path/to/file_{#}.png",
                Arrays.asList(
                    "cmd -x 1 -y 1 /path/to/file_1_z1.png",
                    "cmd -x 2 -y 1 /path/to/file_2_z1.png"
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
