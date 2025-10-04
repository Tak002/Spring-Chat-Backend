package Spring_Websocket.websocket.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
 * Comprehensive tests for GitHub Actions deployment workflow
 * Validates workflow structure, job definitions, steps, and environment variables
 * for the deploy.yml workflow file
 */
@DisplayName("GitHub Actions Workflow Configuration Tests")
class GitHubActionsWorkflowTest {

    private final Yaml yaml = new Yaml();

    @Test
    @DisplayName("Should be able to parse deploy.yml without errors")
    void testDeployWorkflowYamlIsValid() throws IOException {
        Path workflowPath = Paths.get(".github/workflows/deploy.yml");
        assertTrue(Files.exists(workflowPath), "deploy.yml should exist");

        try (InputStream input = Files.newInputStream(workflowPath)) {
            Map<String, Object> workflow = yaml.load(input);
            assertNotNull(workflow, "Workflow YAML should be parseable");
        }
    }

    @Test
    @DisplayName("Workflow should have correct basic structure")
    void testWorkflowBasicStructure() throws IOException {
        Map<String, Object> workflow = loadWorkflow();

        assertAll("Basic Workflow Structure",
            () -> assertTrue(workflow.containsKey("name"), "Should have workflow name"),
            () -> assertTrue(workflow.containsKey("on"), "Should have trigger configuration"),
            () -> assertTrue(workflow.containsKey("jobs"), "Should have jobs defined"),
            () -> assertEquals("build-and-deploy", workflow.get("name"), 
                "Workflow name should be 'build-and-deploy'")
        );
    }

    @Test
    @DisplayName("Workflow should trigger on correct branches")
    void testWorkflowTriggers() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> on = (Map<String, Object>) workflow.get("on");
        Map<String, Object> push = (Map<String, Object>) on.get("push");
        List<String> branches = (List<String>) push.get("branches");

        assertNotNull(branches, "Should have branch triggers defined");
        assertAll("Branch Triggers",
            () -> assertTrue(branches.contains("main"), 
                "Should trigger on main branch"),
            () -> assertTrue(branches.contains("production"), 
                "Should trigger on production branch")
        );
    }

    @Test
    @DisplayName("Should define docker-build-and-push job")
    void testDockerBuildAndPushJobExists() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> jobs = getJobs(workflow);

        assertTrue(jobs.containsKey("docker-build-and-push"), 
            "Should have docker-build-and-push job");
    }

    @Test
    @DisplayName("Should define deploy job")
    void testDeployJobExists() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> jobs = getJobs(workflow);

        assertTrue(jobs.containsKey("deploy"), "Should have deploy job");
    }

    @Test
    @DisplayName("Deploy job should depend on docker-build-and-push")
    void testDeployJobDependency() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> jobs = getJobs(workflow);
        Map<String, Object> deployJob = getJob(jobs, "deploy");

        assertTrue(deployJob.containsKey("needs"), 
            "Deploy job should have needs dependency");
        assertEquals("docker-build-and-push", deployJob.get("needs"), 
            "Deploy job should depend on docker-build-and-push");
    }

    @Test
    @DisplayName("Docker build job should run on ubuntu-latest")
    void testDockerBuildJobRunsOn() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> jobs = getJobs(workflow);
        Map<String, Object> buildJob = getJob(jobs, "docker-build-and-push");

        assertEquals("ubuntu-latest", buildJob.get("runs-on"), 
            "Docker build job should run on ubuntu-latest");
    }

    @Test
    @DisplayName("Docker build job should have all required steps")
    void testDockerBuildJobSteps() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> buildJob = getJob(getJobs(workflow), "docker-build-and-push");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) buildJob.get("steps");

        assertNotNull(steps, "Build job should have steps");
        assertTrue(steps.size() >= 4, "Build job should have at least 4 steps");

        // Find specific steps by name or action
        boolean hasCheckout = steps.stream()
            .anyMatch(step -> "actions/checkout@v4".equals(step.get("uses")));
        boolean hasDockerLogin = steps.stream()
            .anyMatch(step -> "docker/login-action@v3".equals(step.get("uses")));
        boolean hasBuildx = steps.stream()
            .anyMatch(step -> "docker/setup-buildx-action@v3".equals(step.get("uses")));
        boolean hasBuildPush = steps.stream()
            .anyMatch(step -> "docker/build-push-action@v6".equals(step.get("uses")));

        assertAll("Required Build Steps",
            () -> assertTrue(hasCheckout, "Should checkout code"),
            () -> assertTrue(hasDockerLogin, "Should login to Docker registry"),
            () -> assertTrue(hasBuildx, "Should setup Docker Buildx"),
            () -> assertTrue(hasBuildPush, "Should build and push Docker image")
        );
    }

    @Test
    @DisplayName("Docker build step should use GitHub Actions cache")
    void testDockerBuildUsesCache() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> buildJob = getJob(getJobs(workflow), "docker-build-and-push");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) buildJob.get("steps");

        Map<String, Object> buildStep = steps.stream()
            .filter(step -> "docker/build-push-action@v6".equals(step.get("uses")))
            .findFirst()
            .orElse(null);

        assertNotNull(buildStep, "Should have Docker build step");
        Map<String, Object> with = (Map<String, Object>) buildStep.get("with");
        
        assertAll("Docker Build Cache Configuration",
            () -> assertTrue(with.containsKey("cache-from"), 
                "Should configure cache source"),
            () -> assertTrue(with.containsKey("cache-to"), 
                "Should configure cache destination"),
            () -> assertEquals("type=gha", with.get("cache-from"), 
                "Should use GitHub Actions cache as source"),
            () -> assertEquals("type=gha,mode=max", with.get("cache-to"), 
                "Should use GitHub Actions cache with max mode")
        );
    }

    @Test
    @DisplayName("Docker build should tag images correctly")
    void testDockerBuildTags() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> buildJob = getJob(getJobs(workflow), "docker-build-and-push");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) buildJob.get("steps");

        Map<String, Object> buildStep = steps.stream()
            .filter(step -> "docker/build-push-action@v6".equals(step.get("uses")))
            .findFirst()
            .orElse(null);

        assertNotNull(buildStep, "Should have Docker build step");
        Map<String, Object> with = (Map<String, Object>) buildStep.get("with");
        String tags = (String) with.get("tags");

        assertNotNull(tags, "Should have tags configuration");
        assertTrue(tags.contains(":latest"), "Should tag with latest");
        assertTrue(tags.contains("github.sha"), "Should tag with commit SHA");
    }

    @Test
    @DisplayName("Deploy job should have all required steps")
    void testDeployJobSteps() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> deployJob = getJob(getJobs(workflow), "deploy");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) deployJob.get("steps");

        assertNotNull(steps, "Deploy job should have steps");
        assertTrue(steps.size() >= 3, "Deploy job should have at least 3 steps");

        boolean hasCheckout = steps.stream()
            .anyMatch(step -> "actions/checkout@v4".equals(step.get("uses")));
        boolean hasScpAction = steps.stream()
            .anyMatch(step -> "appleboy/scp-action@v0.1.7".equals(step.get("uses")));
        boolean hasSshAction = steps.stream()
            .anyMatch(step -> "appleboy/ssh-action@v1.0.3".equals(step.get("uses")));

        assertAll("Required Deploy Steps",
            () -> assertTrue(hasCheckout, "Should checkout code"),
            () -> assertTrue(hasScpAction, "Should use SCP action to upload compose file"),
            () -> assertTrue(hasSshAction, "Should use SSH action to deploy")
        );
    }

    @Test
    @DisplayName("SCP step should upload docker-compose.prod.yml correctly")
    void testScpStepConfiguration() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> deployJob = getJob(getJobs(workflow), "deploy");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) deployJob.get("steps");

        Map<String, Object> scpStep = steps.stream()
            .filter(step -> "appleboy/scp-action@v0.1.7".equals(step.get("uses")))
            .findFirst()
            .orElse(null);

        assertNotNull(scpStep, "Should have SCP step");
        Map<String, Object> with = (Map<String, Object>) scpStep.get("with");

        assertAll("SCP Configuration",
            () -> assertTrue(with.containsKey("host"), "Should specify host"),
            () -> assertTrue(with.containsKey("username"), "Should specify username"),
            () -> assertTrue(with.containsKey("port"), "Should specify port"),
            () -> assertTrue(with.containsKey("key"), "Should specify SSH key"),
            () -> assertEquals("infra/docker-compose.prod.yml", with.get("source"), 
                "Should upload docker-compose.prod.yml"),
            () -> assertEquals("/opt/chat/", with.get("target"), 
                "Should upload to /opt/chat/ directory"),
            () -> assertEquals(1, with.get("strip_components"), 
                "Should strip infra/ prefix"),
            () -> assertEquals(true, with.get("overwrite"), 
                "Should overwrite existing file")
        );
    }

    @Test
    @DisplayName("SSH deploy step should pass required environment variables")
    void testSshDeployEnvironmentVariables() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> deployJob = getJob(getJobs(workflow), "deploy");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) deployJob.get("steps");

        Map<String, Object> sshStep = steps.stream()
            .filter(step -> "appleboy/ssh-action@v1.0.3".equals(step.get("uses")))
            .findFirst()
            .orElse(null);

        assertNotNull(sshStep, "Should have SSH step");
        Map<String, Object> env = (Map<String, Object>) sshStep.get("env");
        String envs = (String) sshStep.get("with", Map.of()).get("envs");

        assertAll("SSH Environment Variables",
            () -> assertTrue(env.containsKey("DOCKER_IMAGE"), 
                "Should define DOCKER_IMAGE"),
            () -> assertTrue(env.containsKey("IMAGE_TAG"), 
                "Should define IMAGE_TAG"),
            () -> assertTrue(env.containsKey("REGISTRY_USERNAME"), 
                "Should define REGISTRY_USERNAME"),
            () -> assertTrue(env.containsKey("REGISTRY_TOKEN"), 
                "Should define REGISTRY_TOKEN"),
            () -> assertNotNull(envs, "Should specify which env vars to pass"),
            () -> assertTrue(envs.contains("DOCKER_IMAGE"), 
                "Should pass DOCKER_IMAGE to SSH"),
            () -> assertTrue(envs.contains("IMAGE_TAG"), 
                "Should pass IMAGE_TAG to SSH"),
            () -> assertTrue(envs.contains("REGISTRY_USERNAME"), 
                "Should pass REGISTRY_USERNAME to SSH"),
            () -> assertTrue(envs.contains("REGISTRY_TOKEN"), 
                "Should pass REGISTRY_TOKEN to SSH")
        );
    }

    @Test
    @DisplayName("SSH deploy script should use docker compose commands")
    void testSshDeployScriptUsesDockerCompose() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> deployJob = getJob(getJobs(workflow), "deploy");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) deployJob.get("steps");

        Map<String, Object> sshStep = steps.stream()
            .filter(step -> "appleboy/ssh-action@v1.0.3".equals(step.get("uses")))
            .findFirst()
            .orElse(null);

        assertNotNull(sshStep, "Should have SSH step");
        Map<String, Object> with = (Map<String, Object>) sshStep.get("with");
        String script = (String) with.get("script");

        assertNotNull(script, "Should have deployment script");
        assertAll("Deploy Script Commands",
            () -> assertTrue(script.contains("docker compose"), 
                "Should use docker compose commands"),
            () -> assertTrue(script.contains("docker-compose.prod.yml"), 
                "Should reference production compose file"),
            () -> assertTrue(script.contains("docker login"), 
                "Should login to Docker registry"),
            () -> assertTrue(script.contains("pull app-chat-ws"), 
                "Should pull latest app image"),
            () -> assertTrue(script.contains("up -d"), 
                "Should start services in detached mode"),
            () -> assertTrue(script.contains("--no-recreate postgres redis"), 
                "Should not recreate database services"),
            () -> assertTrue(script.contains("--no-deps app-chat-ws"), 
                "Should update app service without dependencies"),
            () -> assertTrue(script.contains("docker image prune"), 
                "Should clean up old images")
        );
    }

    @Test
    @DisplayName("Deploy script should use error handling")
    void testDeployScriptErrorHandling() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> deployJob = getJob(getJobs(workflow), "deploy");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) deployJob.get("steps");

        Map<String, Object> sshStep = steps.stream()
            .filter(step -> "appleboy/ssh-action@v1.0.3".equals(step.get("uses")))
            .findFirst()
            .orElse(null);

        assertNotNull(sshStep, "Should have SSH step");
        Map<String, Object> with = (Map<String, Object>) sshStep.get("with");
        String script = (String) with.get("script");

        assertTrue(script.contains("set -euo pipefail"), 
            "Script should use strict error handling (set -euo pipefail)");
    }

    @Test
    @DisplayName("Deploy script should create required directories")
    void testDeployScriptCreatesDirectories() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> deployJob = getJob(getJobs(workflow), "deploy");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) deployJob.get("steps");

        Map<String, Object> sshStep = steps.stream()
            .filter(step -> "appleboy/ssh-action@v1.0.3".equals(step.get("uses")))
            .findFirst()
            .orElse(null);

        assertNotNull(sshStep, "Should have SSH step");
        Map<String, Object> with = (Map<String, Object>) sshStep.get("with");
        String script = (String) with.get("script");

        assertTrue(script.contains("mkdir -p /opt/chat"), 
            "Script should create deployment directory");
        assertTrue(script.contains("cd /opt/chat"), 
            "Script should change to deployment directory");
    }

    @Test
    @DisplayName("All jobs should use ubuntu-latest runner")
    void testAllJobsUseUbuntuLatest() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> jobs = getJobs(workflow);

        for (String jobName : jobs.keySet()) {
            Map<String, Object> job = getJob(jobs, jobName);
            assertEquals("ubuntu-latest", job.get("runs-on"), 
                jobName + " should run on ubuntu-latest");
        }
    }

    @Test
    @DisplayName("Workflow should use GitHub Actions v4 for checkout")
    void testCheckoutActionVersion() throws IOException {
        Map<String, Object> workflow = loadWorkflow();
        Map<String, Object> jobs = getJobs(workflow);

        for (String jobName : jobs.keySet()) {
            Map<String, Object> job = getJob(jobs, jobName);
            List<Map<String, Object>> steps = (List<Map<String, Object>>) job.get("steps");
            
            boolean hasCheckoutV4 = steps.stream()
                .anyMatch(step -> "actions/checkout@v4".equals(step.get("uses")));
            
            assertTrue(hasCheckoutV4, 
                jobName + " should use actions/checkout@v4");
        }
    }

    // Helper methods
    private Map<String, Object> loadWorkflow() throws IOException {
        Path workflowPath = Paths.get(".github/workflows/deploy.yml");
        try (InputStream input = Files.newInputStream(workflowPath)) {
            return yaml.load(input);
        }
    }

    private Map<String, Object> getJobs(Map<String, Object> workflow) {
        return (Map<String, Object>) workflow.get("jobs");
    }

    private Map<String, Object> getJob(Map<String, Object> jobs, String jobName) {
        Map<String, Object> job = (Map<String, Object>) jobs.get(jobName);
        assertNotNull(job, "Job '" + jobName + "' should be defined");
        return job;
    }
}