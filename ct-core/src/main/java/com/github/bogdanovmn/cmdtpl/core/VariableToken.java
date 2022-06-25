package com.github.bogdanovmn.cmdtpl.core;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
class VariableToken {
    private final String expression;
    private final VariableParameters parameters;

    static final String VALUE_PLACEHOLDER = "\\{#\\}";§

    long instancesCount() {
        return 1
            + (parameters.getFinishValue() - parameters.getStartValue()) / parameters.getIncrementStep()
            + ((parameters.getFinishValue() - parameters.getStartValue()) % parameters.getIncrementStep() > 0 ? 1 : 0);
    }

    long instancesCount1() {
        long count = 0;
        for (int i = parameters.getStartValue(); i <= parameters.getFinishValue(); i += parameters.getIncrementStep()) {
            count++;
        }
        if (shouldIncludeLastValueAsReminder()) {
            count++;
        }
        return count;
    }

    List<String> instances() {
        List<String> result = new ArrayList<>();
        for (int i = parameters.getStartValue(); i <= parameters.getFinishValue(); i += parameters.getIncrementStep()) {
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
                    String.valueOf(parameters.getFinishValue())
                )
            );
        }
        return result;
    }

    private boolean shouldIncludeLastValueAsReminder() {
        return (parameters.getFinishValue() - parameters.getStartValue()) % parameters.getIncrementStep() != 0;
    }
}
