package Spring_Websocket.websocket.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for Docker Compose configuration files
 * Validates YAML structure, service definitions, dependencies, and environment variables
 * for docker-compose.yml, docker-compose.dev.yml, and docker-compose.prod.yml
 */
@DisplayName("Docker Compose Configuration Tests")
class DockerComposeConfigurationTest {

    private final Yaml yaml = new Yaml();

    @Test
    @DisplayName("Should be able to parse base docker-compose.yml without errors")
    void testBaseDockerComposeYamlIsValid() throws IOException {
        Path composePath = Paths.get("infra/docker-compose.yml");
        assertTrue(Files.exists(composePath), "docker-compose.yml should exist");

        try (InputStream input = Files.newInputStream(composePath)) {
            Map<String, Object> config = yaml.load(input);
            assertNotNull(config, "Docker compose YAML should be parseable");
            assertTrue(config.containsKey("services"), "Docker compose should have services section");
        }
    }

    @Test
    @DisplayName("Should be able to parse docker-compose.dev.yml without errors")
    void testDevDockerComposeYamlIsValid() throws IOException {
        Path composePath = Paths.get("infra/docker-compose.dev.yml");
        assertTrue(Files.exists(composePath), "docker-compose.dev.yml should exist");

        try (InputStream input = Files.newInputStream(composePath)) {
            Map<String, Object> config = yaml.load(input);
            assertNotNull(config, "Docker compose dev YAML should be parseable");
            assertTrue(config.containsKey("services"), "Docker compose dev should have services section");
        }
    }

    @Test
    @DisplayName("Should be able to parse docker-compose.prod.yml without errors")
    void testProdDockerComposeYamlIsValid() throws IOException {
        Path composePath = Paths.get("infra/docker-compose.prod.yml");
        assertTrue(Files.exists(composePath), "docker-compose.prod.yml should exist");

        try (InputStream input = Files.newInputStream(composePath)) {
            Map<String, Object> config = yaml.load(input);
            assertNotNull(config, "Docker compose prod YAML should be parseable");
            assertTrue(config.containsKey("services"), "Docker compose prod should have services section");
        }
    }

    @Test
    @DisplayName("Base docker-compose.yml should define all required services")
    void testBaseDockerComposeDefinesAllServices() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> services = getServices(config);

        assertAll("Required Services",
            () -> assertTrue(services.containsKey("postgres"), "Should define postgres service"),
            () -> assertTrue(services.containsKey("redis"), "Should define redis service"),
            () -> assertTrue(services.containsKey("app-chat-ws"), "Should define app-chat-ws service")
        );
    }

    @Test
    @DisplayName("Postgres service should be properly configured in base compose")
    void testPostgresServiceConfiguration() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> services = getServices(config);
        Map<String, Object> postgres = getService(services, "postgres");

        assertAll("Postgres Configuration",
            () -> assertEquals("postgres:16-alpine", postgres.get("image"), 
                "Should use postgres:16-alpine image"),
            () -> assertTrue(postgres.containsKey("environment"), 
                "Should have environment variables"),
            () -> assertTrue(postgres.containsKey("ports"), 
                "Should expose ports"),
            () -> assertTrue(postgres.containsKey("volumes"), 
                "Should have volume configuration"),
            () -> assertTrue(postgres.containsKey("healthcheck"), 
                "Should have healthcheck configuration"),
            () -> assertEquals("unless-stopped", postgres.get("restart"), 
                "Should have restart policy unless-stopped")
        );
    }

    @Test
    @DisplayName("Postgres should have correct environment variables")
    void testPostgresEnvironmentVariables() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> postgres = getService(getServices(config), "postgres");
        Map<String, Object> env = (Map<String, Object>) postgres.get("environment");

        assertAll("Postgres Environment",
            () -> assertTrue(env.containsKey("POSTGRES_DB"), "Should define POSTGRES_DB"),
            () -> assertTrue(env.containsKey("POSTGRES_USER"), "Should define POSTGRES_USER"),
            () -> assertTrue(env.containsKey("POSTGRES_PASSWORD"), "Should define POSTGRES_PASSWORD"),
            () -> assertEquals("localDB", env.get("POSTGRES_DB"), "Database should be localDB"),
            () -> assertEquals("localUser", env.get("POSTGRES_USER"), "User should be localUser")
        );
    }

    @Test
    @DisplayName("Redis service should be properly configured in base compose")
    void testRedisServiceConfiguration() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> redis = getService(getServices(config), "redis");

        assertAll("Redis Configuration",
            () -> assertEquals("redis:7-alpine", redis.get("image"), 
                "Should use redis:7-alpine image"),
            () -> assertTrue(redis.containsKey("ports"), 
                "Should expose ports"),
            () -> assertTrue(redis.containsKey("healthcheck"), 
                "Should have healthcheck configuration"),
            () -> assertEquals("unless-stopped", redis.get("restart"), 
                "Should have restart policy unless-stopped")
        );
    }

    @Test
    @DisplayName("Redis should expose port 6379")
    void testRedisPortConfiguration() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> redis = getService(getServices(config), "redis");
        List<String> ports = (List<String>) redis.get("ports");

        assertNotNull(ports, "Redis should have ports defined");
        assertFalse(ports.isEmpty(), "Redis should expose at least one port");
        assertTrue(ports.stream().anyMatch(p -> p.contains("6379")), 
            "Redis should expose port 6379");
    }

    @Test
    @DisplayName("App service should have correct dependencies in base compose")
    void testAppServiceDependencies() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> app = getService(getServices(config), "app-chat-ws");
        Map<String, Object> dependsOn = (Map<String, Object>) app.get("depends_on");

        assertNotNull(dependsOn, "App should have depends_on configuration");
        assertAll("App Dependencies",
            () -> assertTrue(dependsOn.containsKey("postgres"), "App should depend on postgres"),
            () -> assertTrue(dependsOn.containsKey("redis"), "App should depend on redis")
        );
    }

    @Test
    @DisplayName("App service should wait for healthy dependencies")
    void testAppServiceWaitsForHealthyDependencies() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> app = getService(getServices(config), "app-chat-ws");
        Map<String, Object> dependsOn = (Map<String, Object>) app.get("depends_on");

        Map<String, Object> postgresCondition = (Map<String, Object>) dependsOn.get("postgres");
        Map<String, Object> redisCondition = (Map<String, Object>) dependsOn.get("redis");

        assertAll("Health Check Dependencies",
            () -> assertEquals("service_healthy", postgresCondition.get("condition"), 
                "App should wait for postgres to be healthy"),
            () -> assertEquals("service_healthy", redisCondition.get("condition"), 
                "App should wait for redis to be healthy")
        );
    }

    @Test
    @DisplayName("App service should have correct environment variables in base compose")
    void testAppServiceEnvironmentVariables() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> app = getService(getServices(config), "app-chat-ws");
        Map<String, Object> env = (Map<String, Object>) app.get("environment");

        assertAll("App Environment Variables",
            () -> assertTrue(env.containsKey("SPRING_DATASOURCE_URL"), 
                "Should define datasource URL"),
            () -> assertTrue(env.containsKey("SPRING_DATASOURCE_USERNAME"), 
                "Should define datasource username"),
            () -> assertTrue(env.containsKey("SPRING_DATASOURCE_PASSWORD"), 
                "Should define datasource password"),
            () -> assertTrue(env.containsKey("SPRING_DATA_REDIS_HOST"), 
                "Should define Redis host"),
            () -> assertTrue(env.containsKey("SPRING_DATA_REDIS_PORT"), 
                "Should define Redis port"),
            () -> assertEquals("redis", env.get("SPRING_DATA_REDIS_HOST"), 
                "Redis host should point to redis service"),
            () -> assertEquals("6379", env.get("SPRING_DATA_REDIS_PORT"), 
                "Redis port should be 6379")
        );
    }

    @Test
    @DisplayName("Production compose should use environment variable for Docker image")
    void testProductionComposeUsesImageEnvironmentVariable() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.prod.yml");
        Map<String, Object> app = getService(getServices(config), "app-chat-ws");
        String image = (String) app.get("image");

        assertNotNull(image, "App service should have image defined in production");
        assertTrue(image.contains("${DOCKER_IMAGE"), 
            "Production should use DOCKER_IMAGE environment variable");
        assertTrue(image.contains("${IMAGE_TAG"), 
            "Production should use IMAGE_TAG environment variable");
        assertTrue(image.contains(":-"), 
            "Should have default values for environment variables");
    }

    @Test
    @DisplayName("Production compose should support environment variable substitution")
    void testProductionComposeEnvironmentVariableDefaults() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.prod.yml");
        Map<String, Object> app = getService(getServices(config), "app-chat-ws");
        String image = (String) app.get("image");

        // Verify default values are provided
        assertTrue(image.contains(":-tak002/spring-chat-server"), 
            "Should have default Docker image name");
        assertTrue(image.contains(":-latest"), 
            "Should have default image tag");
    }

    @Test
    @DisplayName("Production compose should use parameterized passwords")
    void testProductionComposeUsesParameterizedPasswords() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.prod.yml");
        Map<String, Object> postgres = getService(getServices(config), "postgres");
        Map<String, Object> env = (Map<String, Object>) postgres.get("environment");

        String postgresPassword = (String) env.get("POSTGRES_PASSWORD");
        assertTrue(postgresPassword.contains("${POSTGRES_PASSWORD"), 
            "Production should use POSTGRES_PASSWORD environment variable");
        assertTrue(postgresPassword.contains(":-secret"), 
            "Should have default password for fallback");
    }

    @Test
    @DisplayName("Dev compose should override app service for development")
    void testDevComposeOverridesAppService() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.dev.yml");
        Map<String, Object> app = getService(getServices(config), "app-chat-ws");

        assertAll("Dev Override Configuration",
            () -> assertTrue(app.containsKey("volumes"), 
                "Dev should mount source code as volume"),
            () -> assertTrue(app.containsKey("working_dir"), 
                "Dev should set working directory"),
            () -> assertTrue(app.containsKey("command"), 
                "Dev should override command")
        );
    }

    @Test
    @DisplayName("Dev compose should use Gradle bootRun command")
    void testDevComposeUsesGradleBootRun() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.dev.yml");
        Map<String, Object> app = getService(getServices(config), "app-chat-ws");
        String command = (String) app.get("command");

        assertNotNull(command, "Dev should have command defined");
        assertTrue(command.contains("gradlew"), 
            "Dev should use Gradle wrapper");
        assertTrue(command.contains("bootRun"), 
            "Dev should use bootRun task");
    }

    @Test
    @DisplayName("Dev compose should set local Spring profile")
    void testDevComposeSetsLocalProfile() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.dev.yml");
        Map<String, Object> app = getService(getServices(config), "app-chat-ws");
        Map<String, Object> env = (Map<String, Object>) app.get("environment");

        assertTrue(env.containsKey("SPRING_PROFILES_ACTIVE"), 
            "Dev should set Spring profile");
        assertEquals("local", env.get("SPRING_PROFILES_ACTIVE"), 
            "Dev should use local profile");
    }

    @Test
    @DisplayName("All compose files should have volume definitions")
    void testVolumeDefinitions() throws IOException {
        Map<String, Object> baseConfig = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> prodConfig = loadDockerCompose("infra/docker-compose.prod.yml");

        assertAll("Volume Definitions",
            () -> assertTrue(baseConfig.containsKey("volumes"), 
                "Base compose should define volumes"),
            () -> assertTrue(prodConfig.containsKey("volumes"), 
                "Production compose should define volumes")
        );
    }

    @Test
    @DisplayName("Postgres volume should be defined for data persistence")
    void testPostgresVolumeDefinition() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> volumes = (Map<String, Object>) config.get("volumes");

        assertTrue(volumes.containsKey("pgdata"), 
            "Should define pgdata volume for postgres");
    }

    @Test
    @DisplayName("Health checks should be properly configured for all services")
    void testHealthCheckConfiguration() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> services = getServices(config);
        
        Map<String, Object> postgres = getService(services, "postgres");
        Map<String, Object> redis = getService(services, "redis");
        
        Map<String, Object> postgresHealth = (Map<String, Object>) postgres.get("healthcheck");
        Map<String, Object> redisHealth = (Map<String, Object>) redis.get("healthcheck");

        assertAll("Health Checks",
            () -> assertNotNull(postgresHealth, "Postgres should have healthcheck"),
            () -> assertNotNull(redisHealth, "Redis should have healthcheck"),
            () -> assertTrue(postgresHealth.containsKey("test"), 
                "Postgres healthcheck should have test command"),
            () -> assertTrue(postgresHealth.containsKey("interval"), 
                "Postgres healthcheck should have interval"),
            () -> assertTrue(postgresHealth.containsKey("timeout"), 
                "Postgres healthcheck should have timeout"),
            () -> assertTrue(postgresHealth.containsKey("retries"), 
                "Postgres healthcheck should have retries")
        );
    }

    @Test
    @DisplayName("All services should have restart policies")
    void testRestartPolicies() throws IOException {
        Map<String, Object> config = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> services = getServices(config);

        for (String serviceName : List.of("postgres", "redis", "app-chat-ws")) {
            Map<String, Object> service = getService(services, serviceName);
            assertTrue(service.containsKey("restart"), 
                serviceName + " should have restart policy defined");
            assertEquals("unless-stopped", service.get("restart"), 
                serviceName + " should use unless-stopped restart policy");
        }
    }

    @Test
    @DisplayName("Port mappings should be consistent across configurations")
    void testPortMappingsConsistency() throws IOException {
        Map<String, Object> baseConfig = loadDockerCompose("infra/docker-compose.yml");
        Map<String, Object> prodConfig = loadDockerCompose("infra/docker-compose.prod.yml");

        Map<String, Object> baseRedis = getService(getServices(baseConfig), "redis");
        Map<String, Object> prodRedis = getService(getServices(prodConfig), "redis");

        List<String> basePorts = (List<String>) baseRedis.get("ports");
        List<String> prodPorts = (List<String>) prodRedis.get("ports");

        // Both should expose Redis on 6379
        assertTrue(basePorts.stream().anyMatch(p -> p.contains("6379")), 
            "Base config should expose Redis on 6379");
        assertTrue(prodPorts.stream().anyMatch(p -> p.contains("6379")), 
            "Production config should expose Redis on 6379");
    }

    // Helper methods
    private Map<String, Object> loadDockerCompose(String path) throws IOException {
        try (InputStream input = Files.newInputStream(Paths.get(path))) {
            return yaml.load(input);
        }
    }

    private Map<String, Object> getServices(Map<String, Object> config) {
        return (Map<String, Object>) config.get("services");
    }

    private Map<String, Object> getService(Map<String, Object> services, String serviceName) {
        Map<String, Object> service = (Map<String, Object>) services.get(serviceName);
        assertNotNull(service, "Service '" + serviceName + "' should be defined");
        return service;
    }
}