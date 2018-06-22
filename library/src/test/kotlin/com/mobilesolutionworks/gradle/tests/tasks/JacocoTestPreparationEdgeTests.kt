package com.mobilesolutionworks.gradle.tests.tasks

import com.mobilesolutionworks.gradle.GradleBaseOptions
import com.mobilesolutionworks.gradle.tasks.JacocoTestPreparation
import org.gradle.api.tasks.TaskInstantiationException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class JacocoTestPreparationEdgeTests {

    @Test(expected = TaskInstantiationException::class)
    fun `using task without jacoco plugin`() {
        with(ProjectBuilder.builder().build()) {
            project.extensions.create("worksOptions", GradleBaseOptions::class.java, project)
            project.apply {
                it.plugin("java")
            }

            tasks.create("jacocoTestPreparation", JacocoTestPreparation::class.java)
        }
    }

    @Test
    fun `using task with jacoco plugin`() {
        with(ProjectBuilder.builder().build()) {
            project.extensions.create("worksOptions", GradleBaseOptions::class.java, project)
            project.apply {
                it.plugin("java")
                it.plugin("jacoco")
            }

            tasks.create("jacocoTestPreparation", JacocoTestPreparation::class.java)
        }
    }
}