package es.sfernandez.randomsequence.generator;

import es.sfernandez.randomsequence.RandomSequenceBuilder;

/**
 * <p>A RandomSequenceGenerator stores a collection of elements and provides a method for build
 * {@link es.sfernandez.randomsequence.RandomSequence} using {@link RandomSequenceBuilder}</p>
 *
 * @param <T> Type of elements to use in the sequences
 * @author Sergio Fernández
 */
public interface RandomSequenceGenerator<T> {

    /**
     * <p>Add the elements received as arguments to the generator.</p>
     * @param elems Array of elements
     */
    void add(final T ... elems);

    /**
     * <p>Add the elements received as arguments to the generator.</p>
     * @param elems Iterable of elements
     */
    void add(final Iterable<T> elems);

    /**
     * <p>Remove all the elements stored by the generator.</p>
     */
    void clear();

    /**
     * @return a new {@link RandomSequenceBuilder} with the elements of the generator
     */
    RandomSequenceBuilder<T> newSequence();

    /**
     * <p>Instantiate and return a {@link RandomSequenceGenerator} that will work with the real references of the
     * objects that are added to it. This means that the sequences generated by it, will reference to the real objects
     * added to the generator.</p>
     * @return a new {@link RandomSequenceGenerator} that will work with the real references of the elements that
     * are added to it.
     * @param <T> type of elements of the new generator
     */
    static <T> RandomSequenceGenerator<T> newGeneratorWorkingWithReferences() {
        return new RandomSequenceGeneratorOfReferences<>();
    }

    /**
     * <p>Instantiate and return a {@link RandomSequenceGenerator} that will work with clones of the
     * objects that are added to it. This means that the sequences generated by it, will reference to clones and not to
     * the real objects added to the generator.</p>
     * <p>You must ensure that the type of elements defines an implementation for the {@link Object#clone()} method.</p>
     * @return a new {@link RandomSequenceGenerator} that will work with clones of the elements that
     * are added to it.
     * @param <T> type of elements of the new generator. It must implement {@link Cloneable}
     */
    static <T extends Cloneable> RandomSequenceGenerator<T> newGeneratorWorkingWithClones() {
        return new RandomSequenceGeneratorOfClones<>();
    }

}
