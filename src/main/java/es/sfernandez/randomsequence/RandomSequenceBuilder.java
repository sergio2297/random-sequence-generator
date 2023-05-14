package es.sfernandez.randomsequence;


import java.util.*;

/**
 * <p>A RandomSequenceBuilder provides a simple interface to generate {@link RandomSequence} from a given iterable of
 * elements.</p>
 * <p>It provides methods for indicate the length of the generated sequence and also to allow or not repetition.</p>
 * <p>An example of use:
 * <pre>
 * <code>
 * Iterable<Integer> elements = IntStream.range(0, 10).boxed().toList();
 *   RandomSequence<Integer> sequence = new RandomSequenceBuilder(elements)
 *      .allowRepetition()
 *      .withLength(20)
 *      .create();
 * </code>
 * </pre>
 * </p>
 * @param <T> Type of elements of the sequence
 * @see RandomSequence
 * @author SFernandez
 */
public final class RandomSequenceBuilder<T> {

    //---- Attributes ----
    private final Random rnd;
    private final List<T> elems;
    private boolean allowRepetition;
    private int length;

    //---- Constructor ----
    /**
     * <p>Instantiate a new RandomSequenceBuilder that will have available for generate {@link RandomSequence}
     * the elements received as arguments.</p>
     * <p>By default, the builder will start configured for create {@link RandomSequence} with all the elements of the
     * array received. That means that in the sequence will appear all elements and they will do once.</p>
     * @param elems Array of elements
     */
    @SafeVarargs
    public RandomSequenceBuilder(final T ... elems) {
        this(Arrays.stream(elems).toList());
    }

    /**
     * <p>Instantiate a new RandomSequenceBuilder that will have available for generate {@link RandomSequence}
     * the elements received as arguments.</p>
     * <p>By default, the builder will start configured for create {@link RandomSequence} with all the elements of the
     * Iterable received. That means that in the sequence will appear all elements and they will do once.</p>
     * @param elems Iterable of elements
     * @throws IllegalArgumentException if the iterable 'elems' is null
     */
    public RandomSequenceBuilder(final Iterable<T> elems) {
        if(elems == null) {
            throw new IllegalArgumentException("Error. The iterable of elements available to generate the sequence " +
                    "can not be null.");
        }
        this.rnd = new Random();
        this.elems = new ArrayList<>();
        elems.forEach(this.elems::add);

        notAllowRepetition();
        withFullLength();
    }

    //---- Methods ----

    /**
     * <p>Configure the builder to allow repetition of elements in the same sequence.</p>
     * @return the reference of the builder
     */
    public RandomSequenceBuilder<T> allowRepetition() {
        this.allowRepetition = true;
        return this;
    }

    /**
     * <p>Configure the builder to avoid repetition of elements in the same sequence.</p>
     * <p>This is the default behaviour.</p>
     * @return the reference of the builder
     */
    public RandomSequenceBuilder<T> notAllowRepetition() {
        this.allowRepetition = false;
        return this;
    }

    /**
     * <p>Configure the builder to create sequences that include every element available by the builder.</p>
     * <p>The builder starts configured this way.</p>
     * @return the reference of the builder
     */
    public RandomSequenceBuilder<T> withFullLength() {
        this.length = elems.size();
        return this;
    }

    /**
     * <p>Configure the builder to create sequences with the length indicated.</p>
     * <p><u>Note</u>: if length is greater than the amount of elements available, ensure to allow repetition. If not,
     * a RandomSequenceBuilder will be thrown trying to create a sequence.</p>
     * @param length Length of the sequence to create.
     * @throws RandomSequenceException if the length is less than 0.
     * @return the reference of the builder
     */
    public RandomSequenceBuilder<T> withLength(final int length) {
        if(length < 0) {
            throw new RandomSequenceException("Error. The length of the sequence must be greater or equal to 0." +
                    " (length=" + length + ")");
        }

        this.length = length;
        return this;
    }

    /**
     * <p>Create a new RandomSequence according to the current configuration.</p>
     * @throws RandomSequenceException if configured length is greater than the amount of elements available and repetition isn't allowed
     * @return a new {@link RandomSequence}
     */
    public RandomSequence<T> create() {
        Optional<RandomSequenceException> validationException = validate();
        if(validationException.isPresent()) {
            throw validationException.get();
        } else {
            return createRandomSequence();
        }
    }

    private Optional<RandomSequenceException> validate() {
        if(!allowRepetition && length > elems.size()) {
            return Optional.of(new RandomSequenceException("Error. If repetition isn't allowed, then the length of the sequence must be " +
                    "less or equal to the number of elements available. (length=" + length + ", elements.size()=" +
                    elems.size() + ")"));
        }

        return Optional.empty();
    }

    private RandomSequence<T> createRandomSequence() {
        List<T> sequence = new ArrayList<>();
        for(int i = 0; i < length; ++i) {
            sequence.add(getRandomElem());
        }
        return new ArrayRandomSequence<>(sequence);
    }

    private T getRandomElem() {
        int randomPosition = rnd.nextInt(elems.size());
        return allowRepetition ?
                elems.get(randomPosition) : elems.remove(randomPosition);
    }
}
