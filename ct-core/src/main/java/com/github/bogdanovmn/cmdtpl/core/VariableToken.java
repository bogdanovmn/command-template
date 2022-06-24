package com.github.bogdanovmn.cmdtpl.core;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
class VariableToken {
    private final String expression;
    private final VariableParameters parameters;

    static final String VALUE_PLACEHOLDER = "\\{#\\}";

    long instancesCount() {
        return (parameters.getFinishValue() - parameters.getStartValue()) / parameters.getIncrementStep();
    }

    List<String> instances() {
        List<String> result = new ArrayList<>();
        for (int i = parameters.getStartValue(); i <= parameters.getFinishValue(); i += parameters.getIncrementStep()) {
            result.add(
                expression.replaceFirst(VALUE_PLACEHOLDER, String.valueOf(i))
            );
        }
        return result;
    }
}
