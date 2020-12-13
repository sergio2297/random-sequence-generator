/**
 * @author Sergio Fernández Aguilar
 * @date 13/12/2020
 * @version 0.1
 */
package randomSequenceGenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

/**
 * The class <i>ExclusiveRandomSequenceGenerator</i> extends {@link RandomSequenceGenerator}. It has a similar behavior to its parent except for 
 * the fact that it will now work with copies of objects and not with the references of them.
 * 
 * @author Sergio Fernández Aguilar
 *
 * @param <T> type of object that have to implements {@link Cloneable} for work properly. <u>Note that</u> the Generator will use copies of objects based
 * on the clone() method specified in the concrete class passed as type. So make sure the clone method really works as it should.
 * 
 * @see RandomSequenceGenerator
 */
public class ExclusiveRandomSequenceGenerator<T extends Cloneable> extends RandomSequenceGenerator<T> {
	
	//---- Attributes ----
	private Method clone;
	
	//---- Constructor ----
	/**
	 * Creates a new {@link ExclusiveRandomSequenceGenerator} with the specified objects passed as arguments. How is it about
	 * an <i>ExclusiveRandomSequenceGenerator</i> the constructor will make a copy of each object which belong to the collection.
	 * So the objects that are returned randomly by this Generator are copies and the user can modify them without alter the original objects.
	 * 
	 * @param objects collection of objects that the generator will use.
	 * @throws RandomSequenceGeneratorException if the object of the specified type doesn't support the opperation <i>clone()</i>.
	 */
	@SuppressWarnings("unchecked")
	public ExclusiveRandomSequenceGenerator(final Collection<T> objects) throws RandomSequenceGeneratorException {
		super();
		
		Iterator<T> it = objects.iterator();
		while(it.hasNext()) {
			T obj = it.next();
			try {
				clone = obj.getClass().getMethod("clone");
				originalObjects.add((T) clone.invoke(obj));
				remainingObjects.add((T) clone.invoke(obj));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RandomSequenceGeneratorException();
			}
		}
	}
	
	//---- Methods ----
	
	/**
	 * Add a copy from the object passed as argument to the original collection of objects stored by the generator. 
	 * <u>Note that</u>:
	 * <ul>
	 * 	<li>The addition of an object won't update the remaining objects, so it will be needed to reset the generator
	 *      to get the added object as a result to a call of the method <i>getNextRandomObject()</i>.</li>
	 *  <li>The Generator will create a copy of the object using his <i>clone()</i> method. So make sure the clone method 
	 *  really works as it should.</li>
	 * </ul>
	 * @param object to add. 
	 * @return true if the object has been added to the original collection with success.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean add(T object) {
		try {
			clone = object.getClass().getMethod("clone");
			return originalObjects.add((T) clone.invoke(object));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return false;
		}
	}
	
}
