package com.mobilesolutionworks.gradle.tasks

import com.mobilesolutionworks.gradle.test.utils.CopyResourceFolder
import com.mobilesolutionworks.gradle.util.withPaths
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class JacocoTestKitConfigureRunnerTests {

    @JvmField
    @Rule
    val tempDir = CopyResourceFolder("JacocoTestKitConfigureRunner")

    @Test
    fun `test with onlyRunCoverageWhenReporting = false`() {
        tempDir.root.withPaths("target", "build.gradle").apply {
            appendText("")
            appendText("""
            worksOptions {
                hasTestKit = true
            }
        """.trimMargin())
        }

        val runner = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withProjectDir(tempDir.root)

        runner.withArguments("test", "--tests", "example.ExampleTest.verifyResourceExists")
                .build()
                .let {
                    assertTrue(it.task(":target:jacocoTestKitConfigureRunner")?.outcome == TaskOutcome.SUCCESS)
                }
    }

    @Test
    fun `test with onlyRunCoverageWhenReporting = true`() {
        tempDir.root.withPaths("target", "build.gradle").apply {
            appendText("")
            appendText("""
            worksOptions {
                hasTestKit = true
                onlyRunCoverageWhenReporting = true
            }
        """.trimMargin())
        }

        val runner = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withProjectDir(tempDir.root)

        runner.withArguments("test", "--tests", "example.ExampleTest.verifyResourceNotCreated")
                .build()
                .let {
                    assertNull(it.task(":target:jacocoTestKitConfigureRunner"))
                }
    }

    @Test
    fun `test with onlyRunCoverageWhenReporting = true, and with report task`() {
        tempDir.root.withPaths("target", "build.gradle").apply {
            appendText("")
            appendText("""
            worksOptions {
                hasTestKit = true
                onlyRunCoverageWhenReporting = true
            }
        """.trimMargin())
        }

        val runner = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withProjectDir(tempDir.root)

        runner.withArguments("test", "--tests", "example.ExampleTest.verifyResourceExists", "jacocoTestReport")
                .build()
                .let {
                    assertTrue(it.task(":target:jacocoTestKitConfigureRunner")?.outcome == TaskOutcome.SUCCESS)
                }
    }

    @Test
    fun `verify configure works incrementally`() {
        tempDir.root.withPaths("target", "build.gradle").apply {
            appendText("")
            appendText("""
            worksOptions {
                hasTestKit = true
            }
        """.trimMargin())
        }

        val runner = GradleRunner.create()
                .forwardOutput()
                .withPluginClasspath()
                .withProjectDir(tempDir.root)

        runner.withArguments("jacocoTestKitConfigureRunner")
                .build()
                .let {
                    assertTrue(it.task(":target:jacocoTestKitConfigureRunner")?.outcome == TaskOutcome.SUCCESS)
                }

        runner.withArguments("jacocoTestKitConfigureRunner")
                .build()
                .let {
                    assertTrue(it.task(":target:jacocoTestKitConfigureRunner")?.outcome == TaskOutcome.UP_TO_DATE)
                }
    }
}
