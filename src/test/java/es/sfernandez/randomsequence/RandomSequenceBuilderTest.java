package es.sfernandez.randomsequence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RandomSequenceBuilderTest {

    //---- Constants and Definitions ----
    static class Foo {}

    //---- Attributes ----
    private RandomSequenceBuilder<Foo> builder;

    //---- Fixtures ----
    private final int NUM_OF_ELEMS = 10;
    private final List<Foo> ITERABLE_OF_ELEMS = Stream.generate(Foo::new).limit(NUM_OF_ELEMS).toList();

    //---- Configuration ----
    @BeforeEach
    void setup() {
        builder = null;
    }

    //---- Tests ----
    @Test
    void instantiateBuilderWithoutElems_doesNotThrowExceptionTest() {
        assertDoesNotThrow(() -> new RandomSequenceBuilder<>());
    }

    @Test
    void instantiateBuilderWithOneNullElem_doesNotThrowExceptionTest() {
        Foo elem = null;

        assertDoesNotThrow(() -> new RandomSequenceBuilder<>(elem));
    }

    @Test
    void instantiateBuilderWithArrayOfNulls_doesNotThrowExceptionTest() {
        Foo[] elems = {null, null};

        assertDoesNotThrow(() -> new RandomSequenceBuilder<>(elems));
    }

    @Test
    void instantiateBuilderWithNullIterable_doesNotThrowExceptionTest() {
        Iterable<Foo> nullIterable = null;

        assertThrows(IllegalArgumentException.class, () -> new RandomSequenceBuilder<>(nullIterable));
    }

    @Test
    void instantiateBuilderWithIterableOfNulls_doesNotThrowExceptionTest() {
        Iterable<Foo> elems = Stream.generate(() -> (Foo) null).limit(2).collect(Collectors.toList());

        assertDoesNotThrow(() -> new RandomSequenceBuilder<>(elems));
    }

    @Test
    void generatSequenceFromNoElems_returnEmptySequenceTest() {
        builder = new RandomSequenceBuilder<>();

        RandomSequence<Foo> sequence = builder.create();

        assertThat(sequence.length()).isEqualTo(0);
    }

    @Test
    void generateSequenceFromOneElem_returnThatElemTest() {
        Foo elem = new Foo();
        builder = new RandomSequenceBuilder<>(elem);

        RandomSequence<Foo> sequence = builder.create();

        assertThat(sequence.get(0)).isSameAs(elem);
    }

    @Test
    void generateSequenceFromOneNullElem_returnThatNullElemTest() {
        Foo elem = null;
        builder = new RandomSequenceBuilder<>(elem);

        RandomSequence<Foo> sequence = builder.create();

        assertThat(sequence.get(0)).isSameAs(elem);
    }

    @Test
    void byDefault_generatedSequenceLengthIsEqualTo_availableNumberOfElemsTest() {
        builder = new RandomSequenceBuilder<>(ITERABLE_OF_ELEMS);

        RandomSequence<Foo> sequence = builder.create();

        assertThat(sequence.length()).isEqualTo(ITERABLE_OF_ELEMS.size());
    }

    @Test
    void byDefault_generateSequence_withLengthGreaterThan_availableNumberOfElems_throwExceptionTest() {
        builder = new RandomSequenceBuilder<>(ITERABLE_OF_ELEMS);

        builder.withLength(ITERABLE_OF_ELEMS.size() + 1);

        assertThrows(RandomSequenceException.class, () -> builder.create());
    }

    @Test
    void tryToConfigureLengthSmallerThanZero_throwExceptionTest() {
        builder = new RandomSequenceBuilder<>();

        assertThrows(RandomSequenceException.class, () -> builder.withLength(-1));
    }

    @Test
    void byDefault_generatedSequenceDoesNotContainAnyRepeatedElemTest() {
        builder = new RandomSequenceBuilder<>(ITERABLE_OF_ELEMS);

        RandomSequence<Foo> sequence = builder.create();

        assertThat(sequence).containsExactlyInAnyOrderElementsOf(ITERABLE_OF_ELEMS);
    }

    @Test
    void generateSequence_withRepeatedElems_andLengthGreaterThanAvailableElems_WorksTest() {
        builder = new RandomSequenceBuilder<>(ITERABLE_OF_ELEMS);

        RandomSequence<Foo> sequence = builder.allowRepetition()
                .withLength(ITERABLE_OF_ELEMS.size() + 1)
                .create();

        assertThat(sequence.length()).isGreaterThan(ITERABLE_OF_ELEMS.size());
        assertThat(sequence).containsAnyElementsOf(ITERABLE_OF_ELEMS);
    }

    @Test
    void generateSequence_fromOneAvailableElem_withRepetition_willReturnSequenceWithThatElemNTimesTest() {
        Foo elem = new Foo();
        builder = new RandomSequenceBuilder<>(elem);
        int length = 10;

        RandomSequence<Foo> sequence = builder.allowRepetition()
                .withLength(10)
                .create();

        assertThat(sequence).containsSequence(
                Stream.generate(() -> elem).limit(length).toList());
    }

    @Test
    void sameElementCanBePartOf_severalGeneratedSequencesTest() {
        Foo element = new Foo();
        builder = new RandomSequenceBuilder<>(element);

        RandomSequence<Foo> sequence1 = builder.create();
        RandomSequence<Foo> sequence2 = builder.create();

        assertThat(sequence1.get(0)).isSameAs(element);
        assertThat(sequence2.get(0)).isSameAs(element);
    }

}