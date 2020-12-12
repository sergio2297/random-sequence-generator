package randomSequenceGenerator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import java.lang.Cloneable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RandomSequenceGenerator<T extends Cloneable> {
	
	//---- Atributos ----
	private Random rnd;
	private List<T> originalObjects;
	private List<T> remainingObjects;
	
	private Method clone;
	
	//---- Constructor ----
	@SuppressWarnings("unchecked")
	public RandomSequenceGenerator(final Collection<T> objects) throws RandomSequenceGeneratorException {
		this.rnd = new Random();
		this.originalObjects = new ArrayList<T>();
		this.remainingObjects = new ArrayList<T>();
		
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
	
	//---- Metodos ----
	public Iterator<T> getOriginalObjects() {
		return originalObjects.iterator();
	}
	
	public Iterator<T> getRemainingObjects() {
		return remainingObjects.iterator();
	}
	
	public boolean hasNextRandomObject() {
		return remainingObjects.size() > 0;
	}
	
	public T getNextRandomObject() {
		T object = remainingObjects.get(rnd.nextInt(remainingObjects.size()));
		remainingObjects.remove(object);
		
		return object;
	}
	
	public T getRandomObject() {
		T object = remainingObjects.get(rnd.nextInt(remainingObjects.size()));
		return object;
	}
	
	public void reset() {
		remainingObjects = new ArrayList<T>();
		remainingObjects.addAll(originalObjects);
	}
	
}
