package org.shipkit.internal.gradle.init.tasks;

import org.shipkit.gradle.init.InitShipkitFileTask;
import org.shipkit.internal.notes.util.IOUtil;
import org.shipkit.internal.util.TemplateResolver;

import java.io.File;

public class InitShipkitFile {

    public void initShipkitFile(InitShipkitFileTask task) {
        File shipkitFile = task.getShipkitFile();
        String originRepoName = task.getOriginRepoName();
        String relativePath = task.getProject().getRootProject().relativePath(shipkitFile);
        initShipkitFile(shipkitFile, relativePath, originRepoName);
    }

    static void initShipkitFile(File shipkitFile, String relativePath, String originRepoName) {
        if (shipkitFile.exists()) {
            InitMessages.skipping(relativePath);
        } else {
            createShipkitFile(shipkitFile, originRepoName);
            InitMessages.generated(relativePath);
        }
    }

    static void createShipkitFile(File shipkitFile, String originRepoName) {
        String content =
            new TemplateResolver(DEFAULT_SHIPKIT_CONFIG_FILE_CONTENT)
                .withProperty("gitHub.repository", originRepoName)
                .withProperty("gitHub.readOnlyAuthToken", "76826c9ec886612f504d12fd4268b16721c4f85d")

                .withProperty("bintray.key", "7ea297848ca948adb7d3ee92a83292112d7ae989")
                .withProperty("bintray.pkg.repo", "bootstrap")
                .withProperty("bintray.pkg.user", "shipkit-bootstrap-bot")
                .withProperty("bintray.pkg.userOrg", "shipkit-bootstrap")
                .withProperty("bintray.pkg.name", "maven")
                .withProperty("bintray.pkg.licenses", "['MIT']")
                .withProperty("bintray.pkg.labels", "['continuous delivery', 'release automation', 'shipkit']")

                .resolve();

        IOUtil.writeFile(shipkitFile, content);
    }

    private static final String DEFAULT_SHIPKIT_CONFIG_FILE_CONTENT =
        "//This default Shipkit configuration file was created automatically and is intended to be checked-in.\n" +
            "//Default configuration is sufficient for local testing and trying out Shipkit.\n" +
            "//To leverage Shipkit fully, please fix the TODO items, refer to our Getting Started Guide for help:\n" +
            "//\n" +
            "//     https://github.com/mockito/shipkit/blob/master/docs/getting-started.md\n" +
            "//\n" +
            "shipkit {\n" +
            "    //TODO is the repository correct?\n" +
            "    gitHub.repository = \"@gitHub.repository@\"\n" +
            "\n" +
            "    //TODO generate and use your own read-only GitHub personal access token\n" +
            "    //More: https://github.com/mockito/shipkit/blob/master/docs/getting-started.md#production-configuration\n" +
            "    gitHub.readOnlyAuthToken = \"@gitHub.readOnlyAuthToken@\"\n" +
            "\n" +
            "    //TODO generate GitHub write token, and ensure your Travis CI has this env variable exported\n" +
            "    //More: https://github.com/mockito/shipkit/blob/master/docs/getting-started.md#write-token\n" +
            "    gitHub.writeAuthToken = System.getenv(\"GH_WRITE_TOKEN\")\n" +
            "}\n" +
            "\n" +
            "allprojects {\n" +
            "    plugins.withId(\"org.shipkit.bintray\") {\n" +
            "\n" +
            "        //Bintray configuration is handled by JFrog Bintray Gradle Plugin\n" +
            "        //For reference see the official documentation: https://github.com/bintray/gradle-bintray-plugin\n" +
            "        bintray {\n" +
            "\n" +
            "            //TODO sign up for free open source account with https://bintray.com, then look up your API key on your profile page in Bintray\n" +
            "            key = '@bintray.key@'\n" +
            "            //TODO don't check in the key, remove above line and use env variable exported on CI:\n" +
            "            //key = System.getenv(\"BINTRAY_API_KEY\")\n" +
            "\n" +
            "            pkg {\n" +
            "                //TODO configure Bintray settings per your project (https://github.com/bintray/gradle-bintray-plugin)\n" +
            "                repo = '@bintray.pkg.repo@'\n" +
            "                user = '@bintray.pkg.user@'\n" +
            "                userOrg = '@bintray.pkg.userOrg@'\n" +
            "                name = '@bintray.pkg.name@'\n" +
            "                licenses = @bintray.pkg.licenses@\n" +
            "                labels = @bintray.pkg.labels@\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";
}
