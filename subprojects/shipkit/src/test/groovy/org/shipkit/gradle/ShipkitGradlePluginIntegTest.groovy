package org.shipkit.gradle

import org.gradle.testkit.runner.BuildResult
import testutil.GradleSpecification

class ShipkitGradlePluginIntegTest extends GradleSpecification {

    def "all tasks in dry run (using gradle version #gradleVersionToTest)"() {
        given:
        gradleVersion = gradleVersionToTest

        and:
        projectDir.newFolder("gradle")
        projectDir.newFile("gradle/shipkit.gradle") << """
            shipkit {
                gitHub.readOnlyAuthToken = "foo"
                gitHub.repository = "repo"
                releaseNotes.publicationRepository = "publicRepo"
            }
        """

        buildFile << """
            apply plugin: "org.shipkit.gradle-plugin"
            apply plugin: "com.gradle.plugin-publish"
            ext.'gradle.publish.key' = 'key'
            ext.'gradle.publish.secret' = 'secret'
        """

        projectDir.newFile("version.properties") << "version=1.0.0"

        expect:
        BuildResult result = pass("performRelease", "-m", "-s")
        skippedTaskPathsGradleBugWorkaround(result.output).join("\n") == """:bumpVersionFile
:identifyGitBranch
:fetchContributors
:fetchReleaseNotes
:updateReleaseNotes
:gitCommit
:compileJava
:processResources
:classes
:jar
:publishPluginJar
:javadoc
:publishPluginJavaDocsJar
:buildArchives
:gitTag
:gitPush
:performGitPush
:discoverPlugins
:publishPlugins
:performRelease"""

        where:
            gradleVersionToTest << determineGradleVersionsToTest()
    }
}
