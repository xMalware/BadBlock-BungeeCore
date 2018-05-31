package fr.badblock.bungee.utils.time;

/**
 * Wrapper for run content in a thread with an interface lambda
 *
 * @author RedSpri
 */
public interface ThreadRunnable {
    /**
     * Run a ThreadRunnable object
     *
     * @param threadRunnable the ThreadRunnable
     */
    static void run(ThreadRunnable threadRunnable) {
        threadRunnable.run();
    }

    /**
     * Run a Runnable like a ThreadRunnable object
     *
     * @param runnable the ThreadRunnable
     */
    static void runRunnable(Runnable runnable) {
        run(runnable::run);
    }

    /**
     * The content to execute.
     * Can be used with lambda
     */
    void content();

    /**
     * Run the content in a thread
     */
    default void run() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                content();
            }
        }.start();
    }
}
