package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import util.ConutScheduler;
/*
 * Scheduling : 정기적인 실행을 하도록 설정
 */
@Configuration
//@EnableScheduling		// Scheduling 적용.
public class BatchConfig {
	@Bean
	public ConutScheduler ConutScheduler() {
		return new ConutScheduler(); //Scheduler 클래스. 의 설정대로 자동 실행.
	}
}
