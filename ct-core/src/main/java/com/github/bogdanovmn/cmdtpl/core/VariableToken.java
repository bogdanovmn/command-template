package com.github.bogdanovmn.cmdtpl.core;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
class VariableToken {
    private final String expression;
    private final VariableParameters parameters;

    static final String VALUE_PLACEHOLDER = "\\{#\\}";

    long instancesCount() {
        return 1
            + (parameters.finishValue() - parameters.startValue()) / parameters.incrementStep()
            + ((parameters.finishValue() - parameters.startValue()) % parameters.incrementStep() > 0 ? 1 : 0);
    }

    List<String> instances() {
        List<String> result = new ArrayList<>();
        for (int i = parameters.startValue(); i <= parameters.finishValue(); i += parameters.incrementStep()) {
            result.add(
                expression.replaceFirst(
                    VALUE_PLACEHOLDER,
                    String.valueOf(i)
                )
            );
        }
        if (shouldIncludeLastValueAsReminder()) {
            result.add(
                expression.replaceFirst(
                    VALUE_PLACEHOLDER,
                    String.valueOf(parameters.finishValue())
                )
            );
        }
        return result;
    }

    Optional<String> label() {
        return Optional.ofNullable(parameters.label());
    }

    private boolean shouldIncludeLastValueAsReminder() {
        return (parameters.finishValue() - parameters.startValue()) % parameters.incrementStep() != 0;
    }
}
