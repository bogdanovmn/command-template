package com.github.bogdanovmn.cmdtpl.core;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class VariableParameters {
    int startValue;
    int finishValue;
    int incrementStep;
    String label;

    static VariableParameters of(String expression) {
        String[] components = expression.split(":");
        if (components.length < 3 || components.length > 4) {
            throw new IllegalArgumentException(
                String.format("Expected 3 or 4 components, but %d. Expression: '%s'", components.length, expression)
            );
        }
        return VariableParameters.builder()
            .startValue(Integer.parseInt(components[0]))
            .finishValue(Integer.parseInt(components[1]))
            .incrementStep(Integer.parseInt(components[2]))
            .label(components.length == 4 ? components[3] : null)
        .build();
    }
}
