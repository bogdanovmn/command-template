package com.github.bogdanovmn.cmdtpl.core;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CombinationTemplate {
    private final String internalExpression;
    private final List<VariableToken> variableTokens;

    public List<String> instances() {
        List<List<String>> variablesCombinations = new ArrayList<>();
        variableTokens.forEach(variableToken -> variablesCombinations.add(variableToken.instances()));
        return Lists.cartesianProduct(variablesCombinations).stream()
            .map(variables -> {
                String instance = internalExpression;
                for (int i = 1; i <= variables.size(); i++) {
                    instance = instance.replaceFirst(String.format("\\{#%d\\}", i), variables.get(i - 1));
                }
                return instance;
            })
            .collect(Collectors.toList());
    }

    public long totalInstances() {
        long result = 0;
        for (VariableToken token : variableTokens) {
            result += token.instancesCount();
        }
        return result;
    }
}
