package com.github.bogdanovmn.cmdtpl.cli.execute;

import com.github.bogdanovmn.cmdline.CmdLineAppBuilder;
import com.github.bogdanovmn.cmdtpl.core.CombinationTemplate;
import com.github.bogdanovmn.cmdtpl.core.CombinationTemplateExpression;
import com.github.bogdanovmn.common.concurrent.ConcurrentConsuming;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class App {

    public static final String ARG_THREAD_POOL_SIZE = "thread-pool-size";
    public static final String ARG_DRY_RUN = "dry-run";
    public static final String ARG_COMMAND_TEMPLATE = "command-template";

    public static void main(String[] args) throws Exception {

        new CmdLineAppBuilder(args)
            .withJarName("cmdtplexec")
            .withDescription("Execute a command with all possible combinations of parameters")

            .withFlag(ARG_DRY_RUN,         "run without any changes")
            .withArg(ARG_THREAD_POOL_SIZE, "concurrent screenshot making thread pool size (default: 1)")

            .withRequiredArg(ARG_COMMAND_TEMPLATE, "command templates")

            .withEntryPoint(
                cmdLine -> {
                    int threadPoolSize = Integer.parseInt(
                        cmdLine.getOptionValue(ARG_THREAD_POOL_SIZE, "1")
                    );
                    boolean dryRun = cmdLine.hasOption(ARG_DRY_RUN);

                    CombinationTemplate template = new CombinationTemplateExpression(
                        cmdLine.getOptionValue(ARG_COMMAND_TEMPLATE)
                    ).template();
                    if (dryRun) {
                        template.instances().forEach(log::info);
                        log.info("Total commands: {}", template.totalInstances());
                    } else {
                        new ConcurrentConsuming(threadPoolSize)
                            .consume(
                                template.instances(),
                                App::execute
                            );
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
