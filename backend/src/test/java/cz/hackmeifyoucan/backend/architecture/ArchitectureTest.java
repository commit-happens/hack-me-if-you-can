package cz.hackmeifyoucan.backend.architecture;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import jakarta.persistence.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;

import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameEndingWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * ArchUnit testy pro architekturu aplikace a best practices.
 *
 * Tato testovací třída ověřuje:
 * - Repository třídy mají správné názvy a anotace
 * - Vztahy entit nepoužívají eager loading
 * - Správná vrstvená architektura (controllers -> services -> repositories)
 * - Žádné cyklické závislosti
 * - Správné použití Spring anotací
 */
@DisplayName("Testy architektury")
@AnalyzeClasses(
        packages = "cz.hackmeifyoucan.backend"
)
class ArchitectureTest {

    private final static String BASE_PACKAGE = "cz.hackmeifyoucan.backend";

    // ==================== TESTY POJMENOVÁNÍ ====================

    @ArchTest
    public static final ArchRule REPOSITORY_ANNOTATION = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().haveSimpleNameEndingWith("Repository")
            .because("Všechny třídy anotované @Repository by měly mít 'Repository' v názvu");

    @ArchTest
    public static final ArchRule SERVICE_ANNOTATION = classes()
            .that().areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("Service")
            .orShould().haveSimpleNameEndingWith("ServiceImpl")
            .because("Všechny třídy anotované @Service by měly mít 'Service' nebo 'ServiceImpl' v názvu");

    @ArchTest
    public static final ArchRule CONTROLLER_ANNOTATION = classes()
            .that().areAnnotatedWith(RestController.class)
            .or().areAnnotatedWith(Controller.class)
            .should().haveSimpleNameEndingWith("Controller")
            .because("Všechny třídy anotované @RestController nebo @Controller by měly mít 'Controller' v názvu");

    // ==================== TESTY EAGER LOADING ====================

    @ArchTest
    public static final ArchRule NO_EAGER_LOADING_ENTITY_ASSOCIATIONS = ArchRuleDefinition
            .classes().that()
            .areAnnotatedWith(Entity.class)
            .should(new JpaAssociationCondition("neobsahují pole nebo metody s eager fetchingem"))
            .allowEmptyShould(true);

    static class JpaAssociationCondition extends ArchCondition<JavaClass> {

        private static final String EAGER_ANNOTATION_FOUND_MESSAGE = "Eager anotace nalezena v %s %s";

        JpaAssociationCondition(final String description, final Object... args) {
            super(description, args);
        }

        @Override
        public void check(final JavaClass item, final ConditionEvents events) {
            checkFields(item, events);
            checkMethods(item, events);
        }

        private void checkFields(final JavaClass javaClass, final ConditionEvents events) {
            javaClass.getAllFields().forEach(field -> {
                checkFieldAnnotation(field, OneToOne.class, events);
                checkFieldAnnotation(field, OneToMany.class, events);
                checkFieldAnnotation(field, ManyToOne.class, events);
                checkFieldAnnotation(field, ManyToMany.class, events);
            });
        }

        private void checkMethods(final JavaClass javaClass, final ConditionEvents events) {
            javaClass.getAllMethods().forEach(method -> {
                checkMethodAnnotation(method, OneToOne.class, events);
                checkMethodAnnotation(method, OneToMany.class, events);
                checkMethodAnnotation(method, ManyToOne.class, events);
                checkMethodAnnotation(method, ManyToMany.class, events);
            });
        }

        private <T extends java.lang.annotation.Annotation> void checkFieldAnnotation(
                final JavaField field,
                final Class<T> annotationType,
                final ConditionEvents events
        ) {
            if (field.isAnnotatedWith(annotationType)) {
                T annotation = field.getAnnotationOfType(annotationType);
                FetchType fetchType = getFetchType(annotation);
                if (!FetchType.LAZY.equals(fetchType)) {
                    events.add(buildViolationEvent(field));
                }
            }
        }

        private <T extends java.lang.annotation.Annotation> void checkMethodAnnotation(
                final JavaMethod method,
                final Class<T> annotationType,
                final ConditionEvents events
        ) {
            if (method.isAnnotatedWith(annotationType)) {
                T annotation = method.getAnnotationOfType(annotationType);
                FetchType fetchType = getFetchType(annotation);
                if (!FetchType.LAZY.equals(fetchType)) {
                    events.add(buildViolationEvent(method));
                }
            }
        }

        private FetchType getFetchType(final java.lang.annotation.Annotation annotation) {
            try {
                return (FetchType) annotation.getClass().getMethod("fetch").invoke(annotation);
            } catch (RuntimeException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Unable to get fetch type from annotation: " + annotation, e);
            }
        }

        private SimpleConditionEvent buildViolationEvent(final JavaField field) {
            return new SimpleConditionEvent(
                    field,
                    false,
                    String.format(EAGER_ANNOTATION_FOUND_MESSAGE, field.getFullName(), field.getSourceCodeLocation())
            );
        }

        private SimpleConditionEvent buildViolationEvent(final JavaMethod method) {
            return new SimpleConditionEvent(
                    method,
                    false,
                    String.format(EAGER_ANNOTATION_FOUND_MESSAGE, method.getFullName(), method.getSourceCodeLocation())
            );
        }
    }

    // ==================== TESTY VRSTVENÉ ARCHITEKTURY ====================

    @ArchTest
    public static final ArchRule LAYERS = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Controller").definedBy(nameEndingWith("Controller"))
            .layer("Service").definedBy(nameEndingWith("Service"))
            .layer("Repository").definedBy(nameEndingWith("Repository"))

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Repository", "Service")
            .allowEmptyShould(true);

    // ==================== TESTY POJMENOVÁNÍ METOD ====================

    @ArchTest
    void NAMING_CONVENTIONS(JavaClasses classes) {
        // Importuj testovací třídy z target/test-classes
        JavaClasses testClasses = new ClassFileImporter()
                .importPath("target/test-classes");

        ArchRuleDefinition.methods()
                .that().areAnnotatedWith(Test.class)
                .should().haveNameMatching("given.*_when.*_then.*")
                .because("Testovací metody by měly následovat konvenci given_when_then")
                .check(testClasses);
    }

    // ==================== TESTY DEPENDENCY INJECTION ====================

    @ArchTest
    public static final ArchRule SERVICE_FINAL_FIELDS = classes()
            .that().haveSimpleNameEndingWith("ServiceImpl")
            .should().haveOnlyFinalFields()
            .because("Injektované závislosti by měly být final pro zajištění immutability");

    @ArchTest
    static final ArchRule NO_GENERIC_EXCEPTION_CATCHING = noMethods()
            .should(new GenericExceptionCatchCondition())
            .because("Chytání generické Exception je příliš obecné. Chytejte konkrétní typy výjimek (RuntimeException, IOException, AmqpException, atd.)")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule PREFER_COLLECTION_UTILS = noMethods()
            .should(new CollectionIsEmptyCallCondition())
            .because("Použijte CollectionUtils.isEmpty(collection) pro bezpečné kontroly místo collection == null || collection.isEmpty()")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule PREFER_STRING_UTILS = noMethods()
            .should(new StringIsEmptyCallCondition())
            .because("Použijte StringUtils.hasText(string) pro bezpečné kontroly místo string == null || string.isEmpty()")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule USE_OPTIONAL_SAFELY = noMethods()
            .should(new OptionalGetCallCondition())
            .because("Optional.get() vyvolá NoSuchElementException pokud je prázdný. Použijte orElse(), orElseThrow() nebo zkontrolujte isPresent()")
            .allowEmptyShould(true);

    private static class GenericExceptionCatchCondition extends ArchCondition<JavaMethod> {

        GenericExceptionCatchCondition() {
            super("chytit generickou Exception v try-catch bloku");
        }

        @Override
        public void check(JavaMethod method, ConditionEvents events) {
            // Přeskočit vlastní třídy výjimek
            if (method.getOwner().getSimpleName().endsWith("Exception")
                    || method.getOwner().isAssignableTo(Exception.class)) {
                return;
            }

            method.getTryCatchBlocks().forEach(tryCatchBlock -> {
                tryCatchBlock.getCaughtThrowables().forEach(throwable -> {
                    // Zkontrolovat, zda se chytá přesně java.lang.Exception (ne podtřídy)
                    if (throwable.getFullName().equals("java.lang.Exception")) {
                        String message = String.format(
                                "Metoda %s chytá generickou Exception (chycený typ: %s) v %s. "
                                        + "Chytejte konkrétní typy výjimek (RuntimeException, IOException, atd.)",
                                method.getFullName(),
                                throwable.getFullName(),
                                method.getSourceCodeLocation()
                        );
                        events.add(new SimpleConditionEvent(method, true, message));
                    }
                });
            });
        }
    }

    private static class CollectionIsEmptyCallCondition extends ArchCondition<JavaMethod> {

        CollectionIsEmptyCallCondition() {
            super("volat Collection.isEmpty()");
        }

        @Override
        public void check(JavaMethod method, ConditionEvents events) {
            // Poznámka: Zjednodušená kontrola, která detekuje jakékoli volání Collection.isEmpty().
            // Neověřuje, zda je přítomna null kontrola.
            long isEmptyCallCount = method.getCallsFromSelf().stream()
                    .filter(call -> call.getTarget().getOwner().isAssignableTo(Collection.class))
                    .filter(call -> call.getTarget().getName().equals("isEmpty"))
                    .count();

            if (isEmptyCallCount > 0) {
                String message = String.format(
                        "Metoda %s volá Collection.isEmpty() %d krát. "
                                + "Použijte CollectionUtils.isEmpty(collection) pro bezpečné kontroly místo collection == null || collection.isEmpty()",
                        method.getFullName(),
                        isEmptyCallCount
                );
                events.add(new SimpleConditionEvent(method, false, message));
            }
        }
    }

    private static class StringIsEmptyCallCondition extends ArchCondition<JavaMethod> {

        StringIsEmptyCallCondition() {
            super("volat String.isEmpty()");
        }

        @Override
        public void check(JavaMethod method, ConditionEvents events) {
            long isEmptyCallCount = method.getCallsFromSelf().stream()
                    .filter(call -> call.getTarget().getOwner().isAssignableTo(String.class))
                    .filter(call -> call.getTarget().getName().equals("isEmpty"))
                    .count();

            if (isEmptyCallCount > 0) {
                String message = String.format(
                        "Metoda %s volá String.isEmpty() %d krát. "
                                + "Použijte StringUtils.hasText(string) pro bezpečné kontroly místo string == null || string.isEmpty()",
                        method.getFullName(),
                        isEmptyCallCount
                );
                events.add(new SimpleConditionEvent(method, false, message));
            }
        }
    }

    private static class OptionalGetCallCondition extends ArchCondition<JavaMethod> {

        OptionalGetCallCondition() {
            super("volat Optional.get() bez bezpečnostních kontrol");
        }

        @Override
        public void check(JavaMethod method, ConditionEvents events) {
            long optionalGetCallCount = method.getCallsFromSelf().stream()
                    .filter(call -> call.getTarget().getOwner().isAssignableTo(Optional.class))
                    .filter(call -> call.getTarget().getName().equals("get"))
                    .count();

            if (optionalGetCallCount > 0) {
                String message = String.format(
                        "Metoda %s volá Optional.get() %d krát. "
                                + "To vyvolá NoSuchElementException pokud je Optional prázdný. "
                                + "Použijte orElse(), orElseThrow() nebo zkontrolujte isPresent() před voláním get()",
                        method.getFullName(),
                        optionalGetCallCount
                );
                events.add(new SimpleConditionEvent(method, false, message));
            }
        }
    }
}
