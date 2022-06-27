package com.github.bogdanovmn.cmdtpl.cli.execute;

import com.github.bogdanovmn.cmdline.CmdLineAppBuilder;
import com.github.bogdanovmn.cmdtpl.core.CombinationTemplate;
import com.github.bogdanovmn.cmdtpl.core.CombinationTemplateExpression;
import com.github.bogdanovmn.common.concurrent.ConcurrentConsuming;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class App {

    private static final String ARG_THREAD_POOL_SIZE = "thread-pool-size";
    private static final String ARG_FIRE = "fire";
    private static final String ARG_COMMAND_TEMPLATE = "command-template";
    private static final String ARG_EXECUTION_LIMIT = "execution-limit";

    private static final long DEFAULT_THREAD_POOL_SIZE = 1;
    private static final long DEFAULT_EXECUTION_LIMIT = 1000;

    public static void main(String[] args) throws Exception {

        new CmdLineAppBuilder(args)
            .withJarName("cmdtplexec")
            .withDescription("Execute a command with all possible combinations of parameters")

            .withFlag(ARG_FIRE,            "run with real command execution")
            .withArg(ARG_THREAD_POOL_SIZE, String.format("concurrent screenshot making thread pool size (default: %d)", DEFAULT_THREAD_POOL_SIZE))

            .withRequiredArg(ARG_COMMAND_TEMPLATE, "command templates")
            .withArg(ARG_EXECUTION_LIMIT, String.format("max amount of the command instances (default: %d)", DEFAULT_EXECUTION_LIMIT))

            .withEntryPoint(
                cmdLine -> {
                    boolean fire = cmdLine.hasOption(ARG_FIRE);
                    int threadPoolSize = Integer.parseInt(
                        cmdLine.getOptionValue(ARG_THREAD_POOL_SIZE, String.valueOf(DEFAULT_THREAD_POOL_SIZE))
                    );
                    long execLimit = Long.parseLong(
                        cmdLine.getOptionValue(ARG_EXECUTION_LIMIT, String.valueOf(DEFAULT_EXECUTION_LIMIT))
                    );

                    CombinationTemplate template = new CombinationTemplateExpression(
                        cmdLine.getOptionValue(ARG_COMMAND_TEMPLATE)
                    ).template();

                    if (fire) {
                        if (template.totalInstances() > execLimit) {
                            throw new RuntimeException(
                                String.format(
                                    "There are too many command's instances: %d (%d allowed). You can override the restriction using the '%s' option",
                                        template.totalInstances(), execLimit, ARG_EXECUTION_LIMIT
                                )
                            );
                        }
                        new ConcurrentConsuming(threadPoolSize)
                            .consume(
                                template.instances(),
                                App::execute
                            );
                    } else {
                        template.instances().forEach(log::trace);
                        log.info("Total commands: {}", template.totalInstances());
                    }
                }
            ).build().run();
    }

    private static void execute(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            throw new RuntimeException(
                String.format("Command '%s' execution error: %s", cmd, ex.getMessage()),
                ex
            );
        }
    }
}
