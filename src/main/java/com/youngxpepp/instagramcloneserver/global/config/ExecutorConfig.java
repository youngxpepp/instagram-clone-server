package com.youngxpepp.instagramcloneserver.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.youngxpepp.instagramcloneserver.runnable.ArticleCreatedRelay;

@Configuration
@RequiredArgsConstructor
public class ExecutorConfig {

	private final ArticleCreatedRelay articleCreatedRelay;

	@Bean
	TaskExecutor articleCreatedExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(10);
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.initialize();

		taskExecutor.execute(articleCreatedRelay);

		return taskExecutor;
	}
}
