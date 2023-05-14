package es.sfernandez.randomsequence.generator;

import es.sfernandez.randomsequence.RandomSequence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RandomSequenceGeneratorOfReferencesTest {

    //---- Constants and Definitions ----
    static class Foo {}

    //---- Attributes ----
    private RandomSequenceGeneratorOfReferences<Foo> generator;

    //---- Fixtures ----
    private final Iterable<Foo> ITERABLE_OF_ELEMS = Stream.generate(Foo::new).limit(5).toList();

    //---- Configuration ----
    @BeforeEach
    void setup() {
        generator = new RandomSequenceGeneratorOfReferences<>();
    }

    //---- Tests ----
    @Test
    void addNoElems_doesNotThrowExceptionTest() {
        Iterable<Foo> elementsBeforeAdd = generator.getElements();

        generator.add();
        Iterable<Foo> elementsAfterAdd = generator.getElements();

        assertThat(elementsAfterAdd).containsExactlyElementsOf(elementsBeforeAdd);
    }

    @Test
    void addOneNullElem_doesNotThrowExceptionTest() {
        Foo elem = null;

        assertDoesNotThrow(() -> generator.add(elem));
    }

    @Test
    void addNullIterable_throwsExceptionTest() {
        Iterable<Foo> iterable = null;

        assertThrows(IllegalArgumentException.class, () -> generator.add(iterable));
    }

    @Test
    void addIterableOfNull_doesNotThrowExceptionTest() {
        Iterable<Foo> iterable = Stream.generate(() -> (Foo) null).limit(3).collect(Collectors.toList());

        assertDoesNotThrow(() -> generator.add(iterable));
    }

    @Test
    void addArrayOfElements_storesReferencesCorrectlyTest() {
        Foo[] array = Stream.generate(Foo::new).limit(10).toArray(Foo[]::new);

        generator.add(array);

        // Compares by equals, in this case by references
        assertThat(generator.getElements()).containsExactlyElementsOf(Arrays.stream(array).toList());
    }

    @Test
    void addIterableOfElements_storesReferencesCorrectlyTest() {
        generator.add(ITERABLE_OF_ELEMS);

        // Compares by equals, in this case by references
        assertThat(generator.getElements()).containsExactlyElementsOf(ITERABLE_OF_ELEMS);
    }

    @Test
    void afterClear_noElementsWillRemainTest() {
        generator.add(ITERABLE_OF_ELEMS);
        assertThat(generator.getElements()).isNotEmpty();

        generator.clear();

        assertThat(generator.getElements()).isEmpty();
    }

    @Test
    void newSequence_createsRandomSequenceBuilderWith_generatorsElementsTest() {
        generator.add(ITERABLE_OF_ELEMS);

        RandomSequence<Foo> sequence = generator.newSequence()
                .withFullLength()
                .notAllowRepetition()
                .create();

        assertThat(sequence).containsExactlyInAnyOrderElementsOf(generator.getElements());
    }

}