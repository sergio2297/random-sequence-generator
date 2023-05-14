package es.sfernandez.randomsequence.generator;

import es.sfernandez.randomsequence.RandomSequenceBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class RandomSequenceGeneratorOfReferences<T>
            implements RandomSequenceGenerator<T> {

    //---- Attributes ----
    private final List<T> elems = new ArrayList<>();

    //---- Methods ----
    @SafeVarargs
    @Override
    public final void add(T... elems) {
        this.add(Arrays.stream(elems).toList());
    }

    @Override
    public void add(Iterable<T> elems) {
        if(elems == null) {
            throw new IllegalArgumentException("Error. The iterable of elements can not be null.");
        }

        for(T elem : elems) {
            this.elems.add(elem);
        }
    }

    @Override
    public final void clear() {
        this.elems.clear();
    }

    @Override
    public final RandomSequenceBuilder<T> newSequence() {
        return new RandomSequenceBuilder<>(elems);
    }

    // Visible for testing only. I don't use @VisibleForTesting because I would have to import too many dependencies
    Iterable<T> getElements() {
        return elems;
    }
}
