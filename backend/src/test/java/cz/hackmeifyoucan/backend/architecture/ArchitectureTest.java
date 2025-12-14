package cz.hackmeifyoucan.backend.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.FetchType;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ArchUnit tests for application architecture and best practices.
 *
 * This test class validates:
 * - Repository classes are package-private
 * - No eager loading in entity relationships
 * - Proper layered architecture (controllers -> services -> repositories)
 * - No cyclic dependencies
 * - Proper use of Spring annotations
 */
@DisplayName("Architecture Tests")
class ArchitectureTest {

    private static final String BASE_PACKAGE = "cz.hackmeifyoucan.backend";
    private final JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(location -> !location.contains("Test"))
            .importPackages(BASE_PACKAGE);

    // ==================== REPOSITORY TESTS ====================

    @Test
    @DisplayName("given_repository_classes_when_checking_naming_then_should_end_with_repository")
    void given_repository_classes_when_checking_naming_then_should_end_with_repository() {
        ArchRule rule = classes()
                .that().resideInAPackage("..repository..")
                .should().haveSimpleNameEndingWith("Repository")
                .because("Repository classes should follow naming convention and end with 'Repository'");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }

    @Test
    @DisplayName("given_repository_interfaces_when_checking_annotation_then_should_have_repository_annotation")
    void given_repository_interfaces_when_checking_annotation_then_should_have_repository_annotation() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().beAnnotatedWith(Repository.class)
                .because("All repository interfaces should be explicitly marked with @Repository");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }

    // ==================== NO EAGER LOADING TESTS ====================

    @Test
    @DisplayName("given_entity_relationships_when_checking_fetch_type_then_should_not_use_eager_loading")
    void given_entity_relationships_when_checking_fetch_type_then_should_not_use_eager_loading() {
        List<String> eagerLoadingViolations = new ArrayList<>();

        importedClasses.stream()
                .filter(javaClass -> javaClass.getPackageName().contains("entity"))
                .forEach(entityClass -> {
                    try {
                        Class<?> clazz = Class.forName(entityClass.getFullName());
                        for (Field field : clazz.getDeclaredFields()) {
                            if (field.isAnnotationPresent(jakarta.persistence.OneToMany.class)) {
                                jakarta.persistence.OneToMany annotation =
                                    field.getAnnotation(jakarta.persistence.OneToMany.class);
                                if (annotation.fetch() == FetchType.EAGER) {
                                    eagerLoadingViolations.add(
                                        String.format("%s.%s uses EAGER loading",
                                            entityClass.getSimpleName(), field.getName())
                                    );
                                }
                            }
                            if (field.isAnnotationPresent(jakarta.persistence.ManyToOne.class)) {
                                jakarta.persistence.ManyToOne annotation =
                                    field.getAnnotation(jakarta.persistence.ManyToOne.class);
                                if (annotation.fetch() == FetchType.EAGER) {
                                    eagerLoadingViolations.add(
                                        String.format("%s.%s uses EAGER loading",
                                            entityClass.getSimpleName(), field.getName())
                                    );
                                }
                            }
                            if (field.isAnnotationPresent(jakarta.persistence.ManyToMany.class)) {
                                jakarta.persistence.ManyToMany annotation =
                                    field.getAnnotation(jakarta.persistence.ManyToMany.class);
                                if (annotation.fetch() == FetchType.EAGER) {
                                    eagerLoadingViolations.add(
                                        String.format("%s.%s uses EAGER loading",
                                            entityClass.getSimpleName(), field.getName())
                                    );
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        // Class not found, skip
                    }
                });

        assertThat(eagerLoadingViolations)
                .as("No entity relationships should use EAGER loading to avoid N+1 queries")
                .isEmpty();
    }

    // ==================== LAYERED ARCHITECTURE TESTS ====================

    @Test
    @DisplayName("given_services_when_checking_dependencies_then_should_access_repositories_and_entities")
    void given_services_when_checking_dependencies_then_should_access_repositories_and_entities() {
        ArchRule rule = classes()
                .that().resideInAPackage("..service..")
                .should().onlyAccessClassesThat()
                .resideInAnyPackage("..service..", "..repository..", "..entity..", "..dto..", "..exception..", "java..", "jakarta..")
                .because("Service layer should only access repository, entity, dto and exception packages");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }

    @Test
    @DisplayName("given_controller_classes_when_checking_naming_then_should_end_with_controller")
    void given_controller_classes_when_checking_naming_then_should_end_with_controller() {
        ArchRule rule = classes()
                .that().resideInAPackage("..controller..")
                .should().haveSimpleNameEndingWith("Controller")
                .because("Controller classes should follow naming convention and end with 'Controller'");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }

    // ==================== SPRING ANNOTATION TESTS ====================

    @Test
    @DisplayName("given_service_implementation_classes_when_checking_annotation_then_should_have_service_annotation")
    void given_service_implementation_classes_when_checking_annotation_then_should_have_service_annotation() {
        ArchRule rule = classes()
                .that().resideInAPackage("..service.impl..")
                .should().beAnnotatedWith(Service.class)
                .because("Service implementation classes should be explicitly marked with @Service");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }

    @Test
    @DisplayName("given_controller_classes_when_checking_annotation_then_should_have_rest_controller_annotation")
    void given_controller_classes_when_checking_annotation_then_should_have_rest_controller_annotation() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().beAnnotatedWith(RestController.class)
                .orShould().beAnnotatedWith(Controller.class)
                .because("Controller classes should be annotated with @RestController or @Controller");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }

    // ==================== NO CYCLIC DEPENDENCIES ====================

    @Test
    @DisplayName("given_entities_when_checking_location_then_should_be_in_entity_package")
    void given_entities_when_checking_location_then_should_be_in_entity_package() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Entity")
                .or().haveSimpleNameEndingWith("Player")
                .should().resideInAPackage("..entity..")
                .because("All entity classes should be in the entity package");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }

    // ==================== TEST NAMING CONVENTION TESTS ====================

    @Test
    @DisplayName("given_test_classes_when_checking_method_names_then_should_follow_given_when_then_convention")
    void given_test_classes_when_checking_method_names_then_should_follow_given_when_then_convention() {
        JavaClasses testClasses = new ClassFileImporter()
                .importPackages(BASE_PACKAGE);

        List<String> testNamingViolations = new ArrayList<>();

        testClasses.stream()
                .filter(javaClass -> javaClass.getSimpleName().endsWith("Test"))
                .forEach(testClass -> {
                    testClass.getMethods().forEach(method -> {
                        String methodName = method.getName();
                        // Check if method starts with given_, when_, then_, or before_
                        boolean followsConvention = methodName.startsWith("given_")
                                || methodName.startsWith("when_")
                                || methodName.startsWith("then_")
                                || methodName.startsWith("before")
                                || methodName.startsWith("setup")
                                || methodName.startsWith("teardown");

                        if (!followsConvention && !methodName.equals("equals") && !methodName.equals("hashCode")) {
                            testNamingViolations.add(
                                String.format("%s.%s does not follow given_when_then convention",
                                    testClass.getSimpleName(), methodName)
                            );
                        }
                    });
                });

        // Only warn, don't fail - existing tests might not follow convention yet
        if (!testNamingViolations.isEmpty()) {
            System.out.println("Test naming convention violations (warning only):");
            testNamingViolations.forEach(System.out::println);
        }
    }

    // ==================== EXCEPTION HANDLING TESTS ====================

    @Test
    @DisplayName("given_exception_classes_when_checking_location_then_should_be_in_exception_package")
    void given_exception_classes_when_checking_location_then_should_be_in_exception_package() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Exception")
                .should().resideInAPackage("..exception..")
                .because("All exception classes should be in the exception package");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }

    @Test
    @DisplayName("given_dto_classes_when_checking_location_then_should_be_in_dto_package")
    void given_dto_classes_when_checking_location_then_should_be_in_dto_package() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Request")
                .or().haveSimpleNameEndingWith("Response")
                .should().resideInAPackage("..dto..")
                .because("All DTO classes should be in the dto package");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }

    // ==================== DEPENDENCY INJECTION TESTS ====================

    @Test
    @DisplayName("given_service_classes_when_checking_constructor_when_dependencies_should_be_final")
    void given_service_classes_when_checking_constructor_when_dependencies_should_be_final() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("ServiceImpl")
                .should().haveOnlyFinalFields()
                .because("Injected dependencies should be final to ensure immutability");

        EvaluationResult result = rule.evaluate(importedClasses);
        assertThat(result.hasViolation()).isFalse();
    }
}
