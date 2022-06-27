package com.github.bogdanovmn.cmdtpl.core;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class CombinationTemplateExpression {
    private final String expression;

    private static final Pattern VARIABLE_PARAMS_PATTERN = Pattern.compile("\\{(\\d+:\\d+:\\d+(?::\\w+)?)\\}");
    private static final Pattern VARIABLE_PATTERN = Pattern.compile(
        String.format(".*%s.*", VARIABLE_PARAMS_PATTERN.pattern())
    );

    public CombinationTemplate template() {
        String[] tokens = expression.split("\\s");
        StringBuilder internalExpression = new StringBuilder();
        List<VariableToken> variableTokens = new ArrayList<>();
        int variableOrder = 1;
        for (String token : tokens) {
            Matcher matcher = VARIABLE_PATTERN.matcher(token);
            String processedToken = token;
            if (matcher.matches()) {
                processedToken = String.format("{#%d}", variableOrder++);

                variableTokens.add(
                    new VariableToken(
                        token.replaceFirst(VARIABLE_PARAMS_PATTERN.pattern(), VariableToken.VALUE_PLACEHOLDER),
                        VariableParameters.of(matcher.group(1))
                    )
                );
            }
            if (internalExpression.length() > 0) {
                internalExpression.append(" ");
            }
            internalExpression.append(processedToken);

        }
        return new CombinationTemplate(internalExpression.toString(), variableTokens);
    }
}
