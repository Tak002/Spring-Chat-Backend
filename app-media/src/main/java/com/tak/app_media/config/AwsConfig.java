package com.tak.app_media.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.create(); // 기본 credential/provider 사용
    }
}
