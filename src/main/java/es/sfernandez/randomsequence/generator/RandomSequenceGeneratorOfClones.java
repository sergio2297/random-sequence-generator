package es.sfernandez.randomsequence.generator;

import es.sfernandez.randomsequence.RandomSequenceException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

class RandomSequenceGeneratorOfClones<T extends Cloneable>
            extends RandomSequenceGeneratorOfReferences<T> {

    //---- Attributes ----
    private Method cloneMethod = null;

    //---- Methods ----
    @Override
    public void add(Iterable<T> elems) {
        if(elems == null) {
            throw new IllegalArgumentException("Error. The iterable of elements can not be null.");
        }

        super.add(cloneIterable(elems));
    }

    private List<T> cloneIterable(final Iterable<T> elems) {
        List<T> clones = new LinkedList<>();

        for(T elem : elems) {
            if(elem == null) {
                clones.add(null);
            } else {
                if(cloneMethod == null) {
                    cloneMethod = referenceCloneMethod(elem.getClass());
                }

                clones.add(clone(elem));
            }
        }

        return clones;
    }

    private Method referenceCloneMethod(final Class<? extends Cloneable> clase) {
        try {
            return clase.getDeclaredMethod("clone");
        } catch (NoSuchMethodException e) {
            throw new RandomSequenceException("Error. " + clase.getSimpleName() + " implements "
                    + Cloneable.class.getSimpleName() + " but doesn't define an implementation for clone() method.");
        }
    }

    private T clone(final T elem) {
        try {
            return (T) cloneMethod.invoke(elem);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RandomSequenceException("Error. " + elem.getClass() + " defines an implementation for " +
                    "clone() method, but it is not visible.");
        }
    }

}
