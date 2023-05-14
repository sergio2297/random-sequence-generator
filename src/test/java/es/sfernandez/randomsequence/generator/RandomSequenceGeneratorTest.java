package es.sfernandez.randomsequence.generator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RandomSequenceGeneratorTest {

    //---- Constants and Definitions ----
    private static class Foo {}

    private static class CloneableFoo implements Cloneable {}

    //---- Tests ----
    @Test
    void generatorReturnedBy_newGeneratorWorkingWithReferences_isInstanceOf_RandomSequenceGeneratorOfReferencesTest() {
        RandomSequenceGenerator<Foo> generator = RandomSequenceGenerator.newGeneratorWorkingWithReferences();
        assertThat(generator).isInstanceOf(RandomSequenceGeneratorOfReferences.class);
    }

    @Test
    void generatorReturnedBy_newGeneratorWorkingWithClones_isInstanceOf_RandomSequenceGeneratorOfClonesTest() {
        RandomSequenceGenerator<CloneableFoo> generator = RandomSequenceGenerator.newGeneratorWorkingWithClones();
        assertThat(generator).isInstanceOf(RandomSequenceGeneratorOfClones.class);
    }

}