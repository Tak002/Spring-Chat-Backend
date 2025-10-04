package com.tak.chat_history;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChatHistoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatHistoryApplication.class, args);
	}
	@Bean
	CommandLineRunner keepAlive() {
		return args1 -> {
			var latch = new java.util.concurrent.CountDownLatch(1);
			Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown));
			latch.await(); // 컨테이너 종료 시까지 블록
		};};
}
