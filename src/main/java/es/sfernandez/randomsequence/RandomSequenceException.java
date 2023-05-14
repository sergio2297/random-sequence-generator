/**
 * @author Sergio Fern�ndez Aguilar
 * @date 13/12/2020
 * @version 0.1
 */
package es.sfernandez.randomsequence;

/**
 * <p>A RandomSequenceException is special type of {@link RuntimeException} thrown by the classes inside the
 * module randomsequence.</p>
 *
 * @author SFernandez
 */
public class RandomSequenceException
			extends RuntimeException {

	/**
	 * <p>Creates a new RandomSequenceException with an error message.</p>
	 * @param msg the error message
	 */
	public RandomSequenceException(final String msg) {
		super(msg);
	}

}
