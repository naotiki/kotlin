/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin

import org.gradle.api.Action
import org.jetbrains.kotlin.gradle.dsl.CompilerCommonOptions

interface HasCompilerOptions<out CO : CompilerCommonOptions> {
    val options: CO

    fun configure(configuration: CO.() -> Unit) {
        configuration(options)
    }

    fun configure(configuration: Action<@UnsafeVariance CO>) {
        configuration.execute(options)
    }
}
