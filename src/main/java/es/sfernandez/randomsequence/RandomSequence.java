package es.sfernandez.randomsequence;

/**
 * <p>A RandomSequence represents an ordered sequence of elements that have been disposed randomly by a
 * {@link RandomSequenceBuilder}.</p>
 * <p>Every RandomSequence is an {@link Iterable}, so it's possible to iterate over its elements easily.
 * Additionally, it provides methods to get the length and the ith element of the sequence.</p>
 *
 * @param <T> Type of elements of the sequence
 * @author SFernandez
 */
public interface RandomSequence<T>
            extends Iterable<T> {

    /**
     * @return the number of elements of the sequence
     */
    int length();

    /**
     * @param position number which indicates the element to return
     * @return the element at the position passed as argument
     * @throws RandomSequenceException if position is out of bounds
     */
    T get(final int position);

}
