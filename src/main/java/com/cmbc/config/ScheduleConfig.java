package com.cmbc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置，其实现库如何配置进来
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ScheduleConfig {
	// right now there is nothing here
}
