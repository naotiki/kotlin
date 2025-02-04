/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("PackageDirectoryMismatch") // Old package for compatibility
package org.jetbrains.kotlin.gradle.plugin.mpp

import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.dsl.CompilerJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.HasCompilerOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilationWithResources
import org.jetbrains.kotlin.gradle.plugin.internal.JavaSourceSetsAccessor
import org.jetbrains.kotlin.gradle.plugin.variantImplementationFactory
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import javax.inject.Inject

abstract class KotlinJvmCompilation @Inject constructor(
    compilationDetails: CompilationDetailsWithRuntime<KotlinJvmOptions>,
) : AbstractKotlinCompilationToRunnableFiles<KotlinJvmOptions>(compilationDetails),
    KotlinCompilationWithResources<KotlinJvmOptions> {
    override val target: KotlinJvmTarget get() = compilationDetails.target as KotlinJvmTarget

    @Suppress("UNCHECKED_CAST")
    override val compilerOptions: HasCompilerOptions<CompilerJvmOptions>
        get() = super.compilerOptions as HasCompilerOptions<CompilerJvmOptions>

    override val processResourcesTaskName: String
        get() = disambiguateName("processResources")

    @Deprecated("Replaced with compileTaskProvider", replaceWith = ReplaceWith("compileTaskProvider"))
    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override val compileKotlinTaskProvider: TaskProvider<out org.jetbrains.kotlin.gradle.tasks.KotlinCompile>
        get() = super.compileKotlinTaskProvider as TaskProvider<out org.jetbrains.kotlin.gradle.tasks.KotlinCompile>

    @Suppress("DEPRECATION")
    @Deprecated("Accessing task instance directly is deprecated", replaceWith = ReplaceWith("compileTaskProvider"))
    override val compileKotlinTask: org.jetbrains.kotlin.gradle.tasks.KotlinCompile
        get() = super.compileKotlinTask as org.jetbrains.kotlin.gradle.tasks.KotlinCompile

    @Suppress("UNCHECKED_CAST")
    override val compileTaskProvider: TaskProvider<out KotlinCompilationTask<CompilerJvmOptions>>
        get() = super.compileTaskProvider as TaskProvider<KotlinCompilationTask<CompilerJvmOptions>>

    val compileJavaTaskProvider: TaskProvider<out JavaCompile>?
        get() = if (target.withJavaEnabled) {
            val project = target.project
            val javaSourceSets = project.gradle.variantImplementationFactory<JavaSourceSetsAccessor.JavaSourceSetsAccessorVariantFactory>()
                .getInstance(project)
                .sourceSets
            val javaSourceSet = javaSourceSets.getByName(compilationPurpose)
            project.tasks.withType(JavaCompile::class.java).named(javaSourceSet.compileJavaTaskName)
        } else null
}