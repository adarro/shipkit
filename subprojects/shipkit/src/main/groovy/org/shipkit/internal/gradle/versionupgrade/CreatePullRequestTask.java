package org.shipkit.internal.gradle.versionupgrade;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

/**
 * Creates a pull request in {@link CreatePullRequestTask#upstreamRepositoryName} between
 * {@link UpgradeDependencyExtension#baseBranch} and {@link CreatePullRequestTask#versionBranch} from
 * {@link CreatePullRequestTask#forkRepositoryName}
 *
 * It is assumed that task is performed on fork repository, so {@link CreatePullRequestTask#forkRepositoryName}
 * is based on origin repo, see {@link org.shipkit.internal.gradle.git.tasks.GitOriginRepoProvider}
 * and {@link CreatePullRequestTask#upstreamRepositoryName} is based on {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getRepository()}
 */
public class CreatePullRequestTask extends DefaultTask {

    @Input private String upstreamRepositoryName;
    @Input private String gitHubApiUrl;
    @Input private String authToken;
    @Input private String versionBranch;
    @Input private String forkRepositoryName;
    @Input private String pullRequestDescription;
    @Input private String pullRequestTitle;
    @Input private String baseBranch;

    private boolean dryRun;

    @TaskAction
    public void createPullRequest() throws IOException {
        new CreatePullRequest().createPullRequest(this);
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getRepository()}
     */
    public String getUpstreamRepositoryName() {
        return upstreamRepositoryName;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getRepository()}
     */
    public void setUpstreamRepositoryName(String upstreamRepositoryName) {
        this.upstreamRepositoryName = upstreamRepositoryName;
    }

    /**
     * It is assumed that this task is performed on fork of the upstream repo, so this value is taken from
     * git remote origin. See {@link org.shipkit.internal.gradle.git.tasks.GitOriginRepoProvider}
     */
    public String getForkRepositoryName() {
        return forkRepositoryName;
    }

    /**
     * See {@link #getForkRepositoryName()}
     */
    public void setForkRepositoryName(String forkRepositoryName) {
        this.forkRepositoryName = forkRepositoryName;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getApiUrl()}
     */
    public String getGitHubApiUrl() {
        return gitHubApiUrl;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getApiUrl()}
     */
    public void setGitHubApiUrl(String gitHubApiUrl) {
        this.gitHubApiUrl = gitHubApiUrl;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getWriteAuthToken()}
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#getWriteAuthToken()}
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Head branch of pull request
     */
    public String getVersionBranch() {
        return versionBranch;
    }

    /**
     * See {@link #getVersionBranch()}
     */
    public void setVersionBranch(String versionBranch) {
        this.versionBranch = versionBranch;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#dryRun}
     */
    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    /**
     * See {@link org.shipkit.gradle.configuration.ShipkitConfiguration.GitHub#dryRun}
     */
    public boolean isDryRun() {
        return dryRun;
    }

    /**
     * Description of pull request.
     */
    public String getPullRequestDescription() {
        return pullRequestDescription;
    }

    /**
     * See {@link #getPullRequestDescription()}
     */
    public void setPullRequestDescription(String pullRequestDescription) {
        this.pullRequestDescription = pullRequestDescription;
    }

    /**
     * Title of pull request.
     */
    public String getPullRequestTitle() {
        return pullRequestTitle;
    }

    /**
     * See {@link #getPullRequestTitle()}
     */
    public void setPullRequestTitle(String pullRequestTitle) {
        this.pullRequestTitle = pullRequestTitle;
    }

    /**
     * See {@link UpgradeDependencyExtension#baseBranch}
     */
    public String getBaseBranch() {
        return baseBranch;
    }

    /**
     * See {@link UpgradeDependencyExtension#baseBranch}
     */
    public void setBaseBranch(String baseBranch) {
        this.baseBranch = baseBranch;
    }
}
