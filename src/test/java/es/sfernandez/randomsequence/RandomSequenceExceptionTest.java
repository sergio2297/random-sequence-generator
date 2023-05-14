package es.sfernandez.randomsequence;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RandomSequenceExceptionTest {

    @Test // To ensure that never is changed
    void aRandomSequenceExceptions_isARuntimeExceptionTest() {
        assertThat(new RandomSequenceException("")).isInstanceOf(RuntimeException.class);
    }

}