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
	 * @param threadRunnable
	 *            the ThreadRunnable
	 */
	static void run(ThreadRunnable threadRunnable) {
		// Run the thread runnable
		threadRunnable.run();
	}

	/**
	 * Run a Runnable like a ThreadRunnable object
	 *
	 * @param runnable
	 *            the ThreadRunnable
	 */
	static void runRunnable(Runnable runnable) {
		// Run the runnable
		run(runnable::run);
	}

	/**
	 * The content to execute. Can be used with lambda
	 */
	void content();

	/**
	 * Run the content in a thread
	 */
	default void run() {
		// Create a thread
		new Thread() {
			/**
			 * Run
			 */
			@Override
			public void run() {
				// Run
				super.run();
				// And process content
				content();
			}
			// Start the thread
		}.start();
	}
}
