package com.mobilesolutionworks.gradle.jacoco.tasks

import com.mobilesolutionworks.gradle.jacoco.util.withPaths
import com.mobilesolutionworks.gradle.jacoco.worksJacoco
import org.gradle.api.tasks.WriteProperties
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.tasks.JacocoBase
import org.gradle.util.GradleVersion

internal open class JacocoTestKitConfigureRunner : WriteProperties() {

    init {
        group = "works-others"
        description = "Prepare javaagent-for-testkit.properties in build/testKit/gradle for testKit coverage"

        with(project) {
            fun target() = when (worksJacoco.onlyRunCoverageWhenReporting) {
                true -> JacocoBase::class.java
                else -> Test::class.java
            }

            tasks.withType(target()).forEach { it ->
                it.dependsOn(this@JacocoTestKitConfigureRunner)
            }

            tasks.withType(Test::class.java).forEach { it ->
                it.shouldRunAfter(this@JacocoTestKitConfigureRunner)
            }

            tasks.withType(JacocoTestKitSetup::class.java).forEach { setup ->
                dependsOn(setup)
                setProperties(mapOf(
                        "agentPath" to setup.agentPath.absolutePath,
                        "outputDir" to file(worksJacoco.testKitExecDir).absolutePath,
                        "tmpDir" to file(worksJacoco.testKitTmpDir).absolutePath
                ))
            }
        }

        setOutput(project.worksJacoco.agentPropertiesName)
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun setOutput(name: String) {
        val file = project.buildDir.withPaths("testKit", "gradle", name)
        if (GradleVersion.current() >= GradleVersion.version("3.4")) {
            outputFile = file
        } else {
            setOutputFile(file.absolutePath)
        }
    }
}