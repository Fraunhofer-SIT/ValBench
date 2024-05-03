package de.fraunhofer.sit.sse.valbench.eval;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelBase {

	public static int NUM_CORES = 30;
	public static AtomicInteger ID = new AtomicInteger();

	public static <T> void For(final T[] elements, final ParallelOperation<T> operation) {
		For(Arrays.asList(elements), operation);
	}

	public static <T> void For(List<T> elements, Runnable runDuringWaiting, ParallelOperation<T> operation) {
		ExecutorService executionService = Executors.newFixedThreadPool(Math.max(1, NUM_CORES), new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread thr = new Thread(r);
				thr.setName("Parallel Thread #" + ID.incrementAndGet());
				thr.setDaemon(true);
				return thr;
			}
		});
		try {
			final List<Callable<Void>> callables = createCallableList(elements, operation);
			for (final Callable<Void> command : callables)
				executionService.execute(new Runnable() {

					@Override
					public void run() {
						try {
							command.call();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			runDuringWaiting.run();
			executionService.shutdown();
			executionService.awaitTermination(100, TimeUnit.HOURS);
			// executionService.invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executionService.shutdownNow();
		}
	}

	public static <T> void ForUnblocking(final Collection<T> elements, final ParallelOperation<T> operation) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				For(elements, operation);
			}

		}).start();
	}

	public static <T> void For(final Collection<T> elements, final ParallelOperation<T> operation) {
		ExecutorService executionService = Executors.newFixedThreadPool(Math.max(1, NUM_CORES), new ThreadFactory() {

			@Override
			public Thread newThread(final Runnable r) {
				Thread thr = new Thread(r);
				thr.setName("Parallel Thread #" + ID.incrementAndGet());
				thr.setDaemon(true);
				return thr;
			}
		});
		try {
			final List<Callable<Void>> callables = createCallableList(elements, operation);
			List<Future<Void>> futures = executionService.invokeAll(callables);
			executionService.shutdown();
			executionService.awaitTermination(100, TimeUnit.HOURS);

			try {
				for (Future<Void> f : futures)
					f.get();
			} catch (ExecutionException ex) {
				if (ex.getCause() instanceof RuntimeException)
					throw (RuntimeException) ex.getCause();
				else
					throw new RuntimeException(ex);
			}
		} catch (InterruptedException e) {
		}
	}

	protected static <T> List<Callable<Void>> createCallableList(final Collection<T> callingList,
			final ParallelOperation<T> operation) {
		List<Callable<Void>> callables = new ArrayList<Callable<Void>>(callingList.size());

		for (final T callingElement : callingList) {
			callables.add(new Callable<Void>() {

				@Override
				public Void call() {

					if (operation.perform(callingElement, 0, callingList.size()))
					return null;
          return null;
				}
			});
		}

		return callables;
	}

	public static interface ParallelOperation<T> {
		public boolean perform(T pParameter, int progress, int max);
	}

	public static <T> void NonParallelFor(final Collection<T> elementList, final ParallelOperation<T> operation) {
		int i = 0;
		for (T d : elementList)
			operation.perform(d, i++, elementList.size());
	}

	public static <T> void NonParallelForBitSlower(final Collection<T> elementList,
			final ParallelOperation<T> operation) {
		int i = 0;
		for (T s : elementList)
			operation.perform(s, i++, elementList.size());
	}

	public static <T> void NonParallelFor(final T[] elements, final ParallelOperation<T> operation) {
		NonParallelFor(Arrays.asList(elements), operation);
	}
	
	public static void main(String[] args) {
		File[] f = new File("testcases").listFiles();
		File res = new File("testcaseresults");
		res.mkdirs();
		AtomicInteger x = new AtomicInteger();
		For(Arrays.asList(f), new ParallelOperation<File>() {

			@Override
			public boolean perform(File pParameter, int progress, int max) {
				try {
					EvaluationUtils.run("java", "-Xmx30g", "-jar", "jsa-2.1.1-SNAPSHOT.jar",
							"/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar", pParameter.getAbsolutePath(), new File(res, x.incrementAndGet() + ".json").getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}

				
				return false;
			}
		});
	}

}
