package es.sfernandez.randomsequence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArrayRandomSequenceTest {

    //---- Constants and Definitions ----
    private static class Foo {}

    //---- Attributes ----
    private RandomSequence<Foo> sequence;

    //---- Fixtures ----
    private static final int NUM_OF_ELEMS = 10;
    private final List<Foo> ITERABLE_OF_ELEMS = Stream.generate(Foo::new)
            .limit(NUM_OF_ELEMS).toList();

    //---- Configuration ----
    @BeforeEach
    void setup() {
        sequence = new ArrayRandomSequence<>(ITERABLE_OF_ELEMS);
    }

    //---- Tests ----
    @Test
    void createAnArrayRandomSequence_withNullIterable_throwsExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayRandomSequence<>(null));
    }

    @Test
    void sequencesLength_isEqualToIterablesNumberOfElementsTest() {
        assertThat(sequence.length()).isEqualTo(ITERABLE_OF_ELEMS.size());
    }

    @Test
    void itsPossibleToIterateOverAnArrayRandomSequenceTest() {
        assertThat(sequence).isInstanceOf(Iterable.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, NUM_OF_ELEMS})
    void getByPositionThat_isOutOfBoundsOfTheSequence_throwsExceptionTest(final int position) {
        assertThrows(RandomSequenceException.class, () -> sequence.get(position));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, NUM_OF_ELEMS/2, NUM_OF_ELEMS - 1})
    void getByPosition_returnTheIthElementOfTheSequenceTest(final int position) {
        Foo element = sequence.get(position);

        assertThat(element).isSameAs(ITERABLE_OF_ELEMS.get(position));
    }

}