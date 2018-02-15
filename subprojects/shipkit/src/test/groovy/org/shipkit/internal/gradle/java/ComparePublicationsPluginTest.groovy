package org.shipkit.internal.gradle.java

import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.testfixtures.ProjectBuilder
import org.shipkit.gradle.configuration.ShipkitConfiguration
import org.shipkit.gradle.java.ComparePublicationsTask
import org.shipkit.gradle.java.DownloadPreviousPublicationsTask
import org.shipkit.internal.gradle.bintray.ShipkitBintrayPlugin
import org.shipkit.internal.gradle.configuration.ShipkitConfigurationPlugin
import org.shipkit.internal.gradle.java.tasks.CreateDependencyInfoFileTask
import testutil.PluginSpecification

class ComparePublicationsPluginTest extends PluginSpecification {

    def "applies"() {
        expect:
        project.plugins.apply(ComparePublicationsPlugin)
    }

    def "sets previousVersionSourcesJarLocalFile to Bintray defaults if BintrayPlugin is applied"() {
        given:
        conf.gitHub.repository = "repo"
        def child = new ProjectBuilder().withName("child").withParent(project).build()

        child.plugins.apply(ShipkitBintrayPlugin)
        child.getExtensions().getByType(BintrayExtension).user = "test"

        when:
        child.plugins.apply(ComparePublicationsPlugin)
        child.evaluate()

        then:
        DownloadPreviousPublicationsTask task = child.getTasks()
                .getByName(ComparePublicationsPlugin.DOWNLOAD_PUBLICATIONS_TASK)

        task.previousSourcesJarUrl.contains("bintray.com")
    }

    def "leaves previousVersionSourcesJarLocalFile null if BintrayPlugin is NOT applied"() {
        given:
        def parent = new ProjectBuilder().withProjectDir(tmp.root).withName("parent").build()
        def child = new ProjectBuilder().withName("child").withParent(parent).build()

        when:
        child.plugins.apply(ComparePublicationsPlugin)
        child.evaluate()

        then:
        DownloadPreviousPublicationsTask task = child.getTasks()
                .getByName(ComparePublicationsPlugin.DOWNLOAD_PUBLICATIONS_TASK)

        task.previousSourcesJarUrl == null
    }

    def "sets correctly local copy of previousSourcesJar in download and comparison tasks"() {
        given:
        def parent = new ProjectBuilder().withProjectDir(tmp.root).withName("parent").build()
        def child = new ProjectBuilder().withName("child").withParent(parent).build()

        child.plugins.apply(ShipkitConfigurationPlugin)
        def conf = parent.getExtensions().getByType(ShipkitConfiguration)
        conf.setPreviousReleaseVersion("1.0.0")

        when:
        child.plugins.apply(ComparePublicationsPlugin)
        child.evaluate()

        then:
        DownloadPreviousPublicationsTask downloadTask = child.getTasks()
                .getByName(ComparePublicationsPlugin.DOWNLOAD_PUBLICATIONS_TASK)
        ComparePublicationsTask comparisonTask = child.getTasks()
                .getByName(ComparePublicationsPlugin.COMPARE_PUBLICATIONS_TASK)

        def basePath = child.getBuildDir().absolutePath + "/previous-release-artifacts/child-1.0.0"
        def expectedSourcesJar = new File(basePath + "-sources.jar")

        downloadTask.previousSourcesJarFile == expectedSourcesJar
        comparisonTask.previousSourcesJar == expectedSourcesJar
    }

    def "failures to download artifact are ignored"() {
        given:
        project.plugins.apply(ComparePublicationsPlugin)
        project.plugins.apply(ShipkitBintrayPlugin)
        project.evaluate()

        when:
        project.tasks[ComparePublicationsPlugin.DOWNLOAD_PUBLICATIONS_TASK].execute()

        then:
        noExceptionThrown()
    }

    def "when no previous artifacts are supplied comparison is skipped"() {
        given:
        conf.previousReleaseVersion = "0.0.1"
        project.plugins.apply(ComparePublicationsPlugin)
        project.plugins.apply(ShipkitBintrayPlugin)
        project.evaluate()

        when:
        project.tasks[ComparePublicationsPlugin.COMPARE_PUBLICATIONS_TASK].execute()

        then:
        noExceptionThrown()
    }

    def "should configure createDependencyInfoFile"() {
        given:
        project.group = "projectGroup"
        project.version = "1.2.3"

        when:
        project.plugins.apply(ComparePublicationsPlugin)
        project.evaluate()

        then:
        CreateDependencyInfoFileTask task = project.tasks.createDependencyInfoFile
        task.configuration == project.configurations.getByName("runtime")
        task.outputFile == new File(project.buildDir, "dependency-info.md")
        task.projectGroup == "projectGroup"
        task.projectVersion == "1.2.3"
    }
}
