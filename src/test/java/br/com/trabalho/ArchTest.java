package br.com.trabalho;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("br.com.trabalho");

        noClasses()
            .that()
            .resideInAnyPackage("br.com.trabalho.service..")
            .or()
            .resideInAnyPackage("br.com.trabalho.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..br.com.trabalho.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
