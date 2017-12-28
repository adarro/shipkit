package org.shipkit.gradle.notes;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.shipkit.internal.gradle.notes.tasks.FetchContributors;

import java.io.File;
import java.util.Collection;

/**
 * Fetches data about all project contributors and stores it in file.
 * The data feeds release notes generation and pom.xml content.
 * It uses GitHub repos/contributors endpoint: https://developer.github.com/v3/repos/#list-contributors
 * This endpoint is cached by GitHub and may return information a few hours old.
 * Therefore, we also fetch recent contributors from GitHub using the "commit" end point:
 * https://developer.github.com/v3/repos/commits/
 * This way, we can also fetch the most recent contributors, necessary for correct release notes information.
 */
public class FetchGitHubContributorsTask extends DefaultTask {

    @Input private String apiUrl;
    @Input private String repository;
    @Input private String readOnlyAuthToken;
    @OutputFile private File outputFile;
    @Input private Collection<String> ignoredContributors;

    @TaskAction
    public void fetchContributors() {
        new FetchContributors().fetchContributors(this);
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getApiUrl()}
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * See {@link #getApiUrl()}
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getRepository()}
     */
    public String getRepository() {
        return repository;
    }

    /**
     * See {@link #getRepository()}
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getReadOnlyAuthToken()}
     */
    public String getReadOnlyAuthToken() {
        return readOnlyAuthToken;
    }

    /**
     * See {@link #getReadOnlyAuthToken()}
     */
    public void setReadOnlyAuthToken(String readOnlyAuthToken) {
        this.readOnlyAuthToken = readOnlyAuthToken;
    }

    /**
     * Where serialized information about contributors will be stored.
     */
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * See {@link #getOutputFile()}
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.Team#getIgnoredContributors()}
     */
    public Collection<String> getIgnoredContributors() {
        return ignoredContributors;
    }

    /**
     * See {@link #getIgnoredContributors()}
     */
    public void setIgnoredContributors(Collection<String> ignoredContributors) {
        this.ignoredContributors = ignoredContributors;
    }
}
