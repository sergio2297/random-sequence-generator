package es.sfernandez.randomsequence.generator;

import es.sfernandez.randomsequence.RandomSequence;
import es.sfernandez.randomsequence.RandomSequenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RandomSequenceGeneratorOfClonesTest {

    //---- Constants and Definitions ----
    static class Foo implements Cloneable {
        
        //---- Constants and Definitions ----
        private static final Random rnd = new Random();
        
        //---- Attributes ----
        private final int num;

        //---- Constructor ----
        public Foo() {
            this.num = rnd.nextInt(10);
        }

        //---- Methods ----
        @Override
        public Foo clone() {
            try {
                return (Foo) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Foo foo = (Foo) o;
            return num == foo.num;
        }

        @Override
        public int hashCode() {
            return Objects.hash(num);
        }
    }

    static class ProtectedCloneFoo implements Cloneable {
        @Override
        protected Foo clone() {
            try {
                return (Foo) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    static class WrongFoo implements Cloneable {}

    //---- Attributes ----
    private RandomSequenceGeneratorOfClones<Foo> generator;

    //---- Fixtures ----
    private final Iterable<Foo> ITERABLE_OF_ELEMS = Stream.generate(Foo::new).limit(5).toList();

    //---- Configuration ----
    @BeforeEach
    void setup() {
        generator = new RandomSequenceGeneratorOfClones<>();
    }

    //---- Methods ----
    private <T> void checkThatClonesAreStored(final Iterable<T> i1, final Iterable<T> i2) {
        Iterator<T> it1 = i1.iterator();
        Iterator<T> it2 = i2.iterator();

        while(it1.hasNext() && it2.hasNext()) {
            T elem1 = it1.next();
            T elem2 = it2.next();

            assertThat(elem1).isNotSameAs(elem2);
            assertThat(elem1).isEqualTo(elem2);
        }

        assertThat(it1.hasNext()).isEqualTo(it2.hasNext());
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
    void addArrayOfElements_storesClonesCorrectlyTest() {
        Foo[] array = Stream.generate(Foo::new).limit(10).toArray(Foo[]::new);

        generator.add(array);

        checkThatClonesAreStored(Arrays.asList(array), generator.getElements());
    }

    @Test
    void addIterableOfElements_storesClonesCorrectlyTest() {
        generator.add(ITERABLE_OF_ELEMS);

        checkThatClonesAreStored(ITERABLE_OF_ELEMS, generator.getElements());
    }

    @Test
    void addElemsFromTypeThat_doesNotImplementsCloneMethod_throwRandomSequenceExceptionTest() {
        WrongFoo element = new WrongFoo();

        assertThrows(RandomSequenceException.class, () -> new RandomSequenceGeneratorOfClones<>().add(element));
    }

    @Test
    void addElemsFromTypeThat_implementsCloneMethodButItIsNotVisible_throwRandomSequenceExceptionTest() {
        ProtectedCloneFoo element = new ProtectedCloneFoo();

        assertThrows(RandomSequenceException.class, () -> new RandomSequenceGeneratorOfClones<>().add(element));
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