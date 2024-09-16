package es.sfernandez.randomsequence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>An array implementation of {@link RandomSequence}. </p>
 * @param <T> Type of elements of the sequence
 * @author Sergio Fernández
 */
class ArrayRandomSequence<T>
            implements RandomSequence<T> {

    //---- Attributes ----
    private final List<T> sequence = new LinkedList<>();

    //---- Constructor ----
    ArrayRandomSequence(final Iterable<T> sequenceOfElems) {
        if(sequenceOfElems == null) {
            throw new IllegalArgumentException("Error. The sequenceOfElems can't be null.");
        }
        sequenceOfElems.forEach(sequence::add);
    }

    //---- Methods ----
    public int length() {
        return sequence.size();
    }

    public T get(final int position) {
        try {
            return sequence.get(position);
        } catch(IndexOutOfBoundsException ex) {
            throw new RandomSequenceException("Error. The position is out of bounds. (position: " + position + ", " +
                    "bounds: [0, " + sequence.size() + "))");
        }
    }

    public Iterator<T> iterator() {
        return sequence.iterator();
    }

}
