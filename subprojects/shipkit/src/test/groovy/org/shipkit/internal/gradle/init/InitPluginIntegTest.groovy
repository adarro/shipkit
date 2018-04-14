package org.shipkit.internal.gradle.init

import testutil.GradleSpecification

class InitPluginIntegTest extends GradleSpecification {

    def "runs initShipkit task in a project without any Shipkit configuration (using gradle version #gradleVersionToTest)"() {
        given:
        gradleVersion = gradleVersionToTest
        assert file("version.properties").delete()

        and:
        buildFile << "apply plugin: 'org.shipkit.java'"

        expect:
        pass("initShipkit", "-s")

        // check if configuration files were generated
        new File(projectDir.root, "gradle/shipkit.gradle").exists()
        new File(projectDir.root, "version.properties").exists()
        new File(projectDir.root, ".travis.yml").exists()

        where:
        gradleVersionToTest << determineGradleVersionsToTest()
    }
}
