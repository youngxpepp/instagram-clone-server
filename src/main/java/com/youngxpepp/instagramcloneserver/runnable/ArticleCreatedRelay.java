package com.youngxpepp.instagramcloneserver.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;

import com.youngxpepp.instagramcloneserver.dao.ArticleCreatedRepository;
import com.youngxpepp.instagramcloneserver.domain.ArticleCreated;
import com.youngxpepp.instagramcloneserver.service.FeedService;

@Slf4j
public class ArticleCreatedRelay implements Runnable {

	private final Lock lock = new ReentrantLock();
	private final Condition signalCondition = lock.newCondition();
	private final Condition emptyCondition = lock.newCondition();
	private final AtomicBoolean stopped = new AtomicBoolean(false);
	private final AtomicBoolean paused = new AtomicBoolean(false);

	private final ArticleCreatedRepository articleCreatedRepository;
	private final FeedService feedService;

	public ArticleCreatedRelay(
		ArticleCreatedRepository articleCreatedRepository,
		FeedService feedService
	) {
		this.articleCreatedRepository = articleCreatedRepository;
		this.feedService = feedService;
	}

	public boolean isStopped() {
		return stopped.get();
	}

	public void toggleStopped(boolean value) {
		lock.lock();
		try {
			stopped.set(value);
			signalCondition.signal();
		} finally {
			lock.unlock();
		}
	}

	public boolean isPaused() {
		return paused.get();
	}

	public void togglePaused(boolean value) {
		lock.lock();
		try {
			paused.set(value);
			signalCondition.signal();
		} finally {
			lock.unlock();
		}
	}

	public void notifyEmptyCondition() {
		lock.lock();
		try {
			emptyCondition.signal();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void run() {
		int failedCount = 0;
		int emptyCount = 0;
		while (!stopped.get()) {
			try {
				lock.lock();
				try {
					while (!stopped.get() && paused.get()) {
						try {
							signalCondition.await(1000, TimeUnit.MILLISECONDS);
						} catch (InterruptedException ignored) {
						}
						failedCount = 0;
						emptyCount = 0;
					}

					if (stopped.get()) {
						break;
					}
				} finally {
					lock.unlock();
				}

				if (emptyCount > 0) {
					lock.lock();
					try {
						long delay = computeDelayForRepeatedEmpty(emptyCount);
						emptyCondition.await(delay, TimeUnit.MILLISECONDS);
					} catch (InterruptedException ignored) {
						emptyCount = 0;
					} finally {
						lock.unlock();
					}
				}

				List<ArticleCreated> articleCreateds = null;
				try {
					articleCreateds = articleCreatedRepository.findAll();
				} catch (RuntimeException e) {
					if (failedCount < Integer.MAX_VALUE) {
						failedCount++;
					}
					continue;
				}

				if (articleCreateds.isEmpty()) {
					if (emptyCount < Integer.MAX_VALUE) {
						emptyCount++;
					}
					continue;
				}
				emptyCount = 0;

				List<Long> producedIds = new ArrayList<>();
				for (ArticleCreated ac : articleCreateds) {
					try {
						feedService.createFeedsAsync(ac.getId());
						producedIds.add(ac.getId());
					} catch (TaskRejectedException ignored) {
					}
				}

				if (producedIds.isEmpty()) {
					failedCount++;
					continue;
				}

				int retries = 0;
				long deletedRows = 0;
				while (retries <= 3) {
					try {
						deletedRows += articleCreatedRepository.deleteAllInIds(producedIds);
						if (deletedRows == producedIds.size()) {
							break;
						}
					} catch (RuntimeException e) {
						log.error("Failed to delete produced ArticleCreateds", e);
					}
					retries++;
				}
			} catch (RuntimeException e) {
				log.error("RuntimeException occurred in a relay loop", e);
			}
		}
	}

	private long computeDelayForRepeatedEmpty(int emptyCount) {
		return emptyCount * 100;
	}
}
