package Spring_Websocket.websocket.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for application-local.properties configuration
 * Validates that the local development properties are correctly configured
 * and that the Redis port change from 16379 to 6379 is properly applied.
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
@DisplayName("Application Local Properties Configuration Tests")
class ApplicationLocalPropertiesTest {

    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${spring.data.redis.host:}")
    private String redisHost;

    @Value("${spring.data.redis.port:0}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.timeout:0}")
    private int redisTimeout;

    @Test
    @DisplayName("Should have valid application name configured")
    void testApplicationNameIsConfigured() {
        assertNotNull(applicationName, "Application name should not be null");
        assertFalse(applicationName.trim().isEmpty(), "Application name should not be empty");
        assertEquals("websocket", applicationName, "Application name should be 'websocket'");
    }

    @Test
    @DisplayName("Should have Redis host configured for local development")
    void testRedisHostIsConfigured() {
        assertNotNull(redisHost, "Redis host should not be null");
        assertFalse(redisHost.trim().isEmpty(), "Redis host should not be empty");
        assertEquals("localhost", redisHost, "Redis host should be 'localhost' for local development");
    }

    @Test
    @DisplayName("Should use standard Redis port 6379 (not custom port 16379)")
    void testRedisPortIsStandardPort() {
        assertNotEquals(0, redisPort, "Redis port should be configured");
        assertEquals(6379, redisPort, "Redis port should be 6379 (standard port) for docker-compose compatibility");
        assertNotEquals(16379, redisPort, "Redis port should not be 16379 (old custom port)");
    }

    @Test
    @DisplayName("Should have Redis port in valid range")
    void testRedisPortIsInValidRange() {
        assertTrue(redisPort > 0, "Redis port should be positive");
        assertTrue(redisPort <= 65535, "Redis port should not exceed maximum port number");
        assertTrue(redisPort >= 1024 || redisPort == 6379, 
            "Redis port should either be standard 6379 or in unprivileged range (>= 1024)");
    }

    @Test
    @DisplayName("Should have Redis password placeholder configured")
    void testRedisPasswordPlaceholderExists() {
        // The password should reference an environment variable
        assertNotNull(redisPassword, "Redis password configuration should not be null");
    }

    @Test
    @DisplayName("Should have Redis timeout configured")
    void testRedisTimeoutIsConfigured() {
        assertNotEquals(0, redisTimeout, "Redis timeout should be configured");
        assertEquals(2000, redisTimeout, "Redis timeout should be 2000ms as per configuration");
        assertTrue(redisTimeout > 0, "Redis timeout should be positive");
        assertTrue(redisTimeout <= 30000, "Redis timeout should be reasonable (not exceed 30 seconds)");
    }

    @Test
    @DisplayName("Should have all required Redis properties present")
    void testAllRequiredRedisPropertiesPresent() {
        assertAll("Redis Configuration",
            () -> assertNotNull(redisHost, "Redis host should be present"),
            () -> assertNotEquals(0, redisPort, "Redis port should be present"),
            () -> assertNotNull(redisPassword, "Redis password configuration should be present"),
            () -> assertNotEquals(0, redisTimeout, "Redis timeout should be present")
        );
    }

    @Test
    @DisplayName("Should be compatible with docker-compose configuration")
    void testDockerComposeCompatibility() {
        // Verify that the local properties align with docker-compose.yml expectations
        assertEquals("localhost", redisHost, 
            "Local Redis host should be localhost to connect to docker-compose Redis service");
        assertEquals(6379, redisPort, 
            "Local Redis port should match docker-compose Redis exposed port");
    }
}