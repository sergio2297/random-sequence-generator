/**
 * @author Sergio Fernández Aguilar
 * @date 13/12/2020
 * @version 0.1
 */
package randomSequenceGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * An instance of <i>RandomSequenceGenerator</i> will provide the basic behavior of manage and return sequence of objects generated randomly.
 * It provide methods that offer different ways to work with this sequences:
 * <ul>
 * 	<li><b>Completely sequence generated automatically</b> by the method <i>generateRandomSequence()</i>.</li>
 *  <li><b>Generate the sequence manually</b> with the methods <i>hasNextRandomObject()</i> and <i>getNextRandomObject()</i>.</li>
 *  <li>Other methods that not alter the state of the generator an simply return an object randomly from the original 
 *  objects or the remaining objects (objects that aren't returned yet by the method <i>getNextRandomObject().</i></li>
 * </ul>
 * 
 * @author Sergio Fernández Aguilar
 *
 * @param <T> type of object. <u>Note that</u> the Generator will use the references of the objects passed as arguments. So every change made over
 * an object returned by the generator will alter the state of the <i>original</i> object. If you want to avoid this you should use {@link ExclusiveRandomSequenceGenerator}. 
 * 
 * @see ExclusiveRandomSequenceGenerator
 */
public class RandomSequenceGenerator<T> {
	
	//---- Attributes ----
	private Random rnd;
	protected List<T> originalObjects;
	protected List<T> remainingObjects;
	
	//---- Constructor ----
	
	/**
	 * Creates a new instance of a {@link RandomSequenceGenerator} with all its parameters initialized.
	 */
	protected RandomSequenceGenerator() {
		this.rnd = new Random();
		this.originalObjects = new ArrayList<T>();
		this.remainingObjects = new ArrayList<T>();
	}
	
	/**
	 * Creates a new {@link RandomSequenceGenerator} with the references of the objects passed as arguments.
	 * 
	 * @param objects collection of objects that the generator will use.
	 */
	public RandomSequenceGenerator(final Collection<T> objects) {
		this();
		
		Iterator<T> it = objects.iterator();
		while(it.hasNext()) {
			T obj = it.next();
			originalObjects.add(obj);
			remainingObjects.add(obj);
			
		}
	}
	
	//---- Methods ----
	/**
	 * Returns an {@link Iterator} over the original objects collection stored by the generator.
	 * @return an <i>iterator</i> over the original objects collection stored by the generator.
	 */
	public Iterator<T> getOriginalObjects() {
		return originalObjects.iterator();
	}
	
	/**
	 * Returns an {@link Iterator} over the remaining objects collection stored by the generator. This
	 * means that the objects that have been returned using the method <i>getNextRandomObject()</i> will
	 * not appeared in the <i>iterator</i> until the generator is reset.
	 * @return an <i>iterator</i> over the remaining objects collection stored by the generator.
	 */
	public Iterator<T> getRemainingObjects() {
		return remainingObjects.iterator();
	}
	
	/**
	 * Add the object passed as argument to the original collection of objects stored by the generator. 
	 * <u>Note that</u>: The addition of an object won't update the remaining objects, so it will be needed to reset the generator
	 *      to get the added object as a result to a call of the method <i>getNextRandomObject()</i>.
	 * @param object to add. 
	 * @return true if the object has been added to the original collection with success.
	 * 
	 */
	public boolean add(T object) {
		return originalObjects.add(object);
	}
	
	/**
	 * Removes the first occurrence of the specified element from the original collection,if it is present.
	 * <u>Note that</u>: The elimination of an object won't update the remaining objects, so it may be needed to reset the generator
	 *      to don't get the removed object as a result to a call of the method <i>getNextRandomObject()</i>. 
	 * @param object to remove.
	 * @return true if the original collection of the generator contains the object.
	 */
	public boolean remove(T object) {
		return originalObjects.remove(object);
	}
	
	/**
	 * Returns true if there are still elements that haven't been returned as a result to a call of the method <i>getNextRandomObject()</i>.
	 * @return true if there are still elements that haven't been returned as a result to a call of the method <i>getNextRandomObject()</i>.
	 */
	public boolean hasNextRandomObject() {
		return remainingObjects.size() > 0;
	}
	
	/**
	 * Returns a reference to a random object from the objects that havent'been returned yet until the last <i>reset()</i> of the generator.
	 * After that, the state of the generator will be update and, until the generator get <i>reset()</i>, it won't be possible to obtain a
	 * reference to the same object.
	 * @return a reference to a random object from the objects that havent'been returned yet until the last <i>reset()</i> of the generator.
	 */
	public T getNextRandomObject() {
		T object = remainingObjects.get(rnd.nextInt(remainingObjects.size()));
		remainingObjects.remove(object);
		
		return object;
	}
	
	/**
	 * Returns a reference to a random object from the objects that havent'been returned yet until the last <i>reset()</i> of the generator.
	 * @return a reference to a random object from the objects that havent'been returned yet until the last <i>reset()</i> of the generator.
	 */
	public T getRandomObjectFromRemainning() {
		T object = remainingObjects.get(rnd.nextInt(remainingObjects.size()));
		return object;
	}
	
	/**
	 * Returns a reference to a random object from all the objects stored in the generator.
	 * @return a reference to a random object from all the objects stored in the generator.
	 */
	public T getRandomObject() {
		T object = originalObjects.get(rnd.nextInt(originalObjects.size()));
		return object;
	}
	
	/**
	 * Reset the state of the generator. So the remaining objects will be equals to the original list.
	 */
	public void reset() {
		remainingObjects = new ArrayList<T>();
		remainingObjects.addAll(originalObjects);
	}
	
	/**
	 * <u>Note that</u> the execution of this method will reset the state of the generator.
	 * Returns a sequence generated with all the elements stored in the generator without repeated elements.
	 * 
	 * @return a sequence generated with all the elements stored in the generator without repeated elements. 
	 * @throws RandomSequenceGeneratorException never
	 */
	public Collection<T> generateRandomSequence() throws RandomSequenceGeneratorException {
		return generateRandomSequence(originalObjects.size(),false);
	}
	
	/**
	 * <u>Note that</u> the execution of this method will reset the state of the generator.
	 * 
	 * Returns a sequence generated automatically with the specified length and the possibility of repeated elements.<br/>
	 * <b>For Example</b>:<br/>
	 * <ul>
	 * 	<li>
	 * if it's called <i>generatedRandomSequence(5,true)</i>:<br/>
	 * - Objects stored: [0,1,2] <br/>
	 * - Possible results: [0,0,2,1,0], [1,1,1,1,1], [0,1,2,2,0]...
	 * 	</li>
	 * 	<li>
	 * if it's called <i>generatedRandomSequence(5,false)</i>:<br/>
	 * - Objects stored: [0,1,2] <br/>
	 * - Possible results: [0,1,2,1,0], [1,0,2,0,1], [0,1,2,2,0]...<br/>
	 * - It's not possible to get: [0,0,1,2,1] or [1,2,0,1,1]
	 * 	</li>
	 * </ul>
	 * 
	 * @param length of the generated sequence. Must be greater or equals to 0. If the length is greater than the size of the collection
	 * stored by the generator, the generator work in different ways depends on the <i>repeatedAllowed</i> value passed.
	 * @param repeatedAllowed specified if the sequence allows repeated objects. If the length is greater than the size of the collection
	 * stored by the generator, the sequence will be generated without repeated elements in intervals with the size of the original collection.
	 * @return a sequence generated automatically with the specified length and the possibility of repeated elements.
	 * @throws RandomSequenceGeneratorException if the specified length is below 0.
	 */
	public Collection<T> generateRandomSequence(final int length, final boolean repeatedAllowed) throws RandomSequenceGeneratorException {
		if(length < 0) {
			throw new RandomSequenceGeneratorException("Error. The lenght specified as argument must be greater or equals to 0");
		}
		List<T> objects = new ArrayList<T>();
		reset();
		if(repeatedAllowed) {
			for(int i = 0; i<length; ++i) {
				objects.add(getRandomObject());
			}
		} else {
			int i = 0;
			while(i < length) {
				if(this.hasNextRandomObject()) {
					objects.add(getNextRandomObject());
					++i;
				} else {
					reset();
				}
			}
		}
		reset();
		return objects;
	}
	
}