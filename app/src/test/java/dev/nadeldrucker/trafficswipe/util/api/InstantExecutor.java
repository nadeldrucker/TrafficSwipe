package dev.nadeldrucker.trafficswipe.util.api;

import java.util.concurrent.Executor;

/**
 * Class for testing, executes runnables instantly.
 */
class InstantExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        command.run();
    }

}
