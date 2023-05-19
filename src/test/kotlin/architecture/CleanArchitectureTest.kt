/*
 * Copyright (c) 2023. Smart Operating Block
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package architecture

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import io.kotest.core.spec.style.StringSpec

class CleanArchitectureTest : StringSpec({
    "Test that the layers of the Clean Architecture are respected" {
        layeredArchitecture()
            .consideringAllDependencies()
            .layer("entity").definedBy("..entity..")
            .layer("usecase").definedBy("..usecase..")
            .layer("application").definedBy("..application..")
            .layer("infrastructure").definedBy("..infrastructure..")
            .whereLayer("entity").mayOnlyBeAccessedByLayers("usecase", "application", "infrastructure")
            .whereLayer("usecase").mayOnlyBeAccessedByLayers("application", "infrastructure")
            .whereLayer("application").mayOnlyBeAccessedByLayers("infrastructure")
            .whereLayer("infrastructure").mayNotBeAccessedByAnyLayer()
            .check(
                ClassFileImporter()
                    .withImportOption { !it.contains("/test/") } // ignore tests classes
                    .importPackages("entity", "usecase", "application", "infrastructure"),
            )
    }
})
