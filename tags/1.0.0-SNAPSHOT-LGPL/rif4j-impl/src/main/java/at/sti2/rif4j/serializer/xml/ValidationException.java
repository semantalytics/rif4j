package at.sti2.rif4j.serializer.xml;

/**
 * @author Iker Larizgoitia Abad
 */
public class ValidationException extends RuntimeException {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 5373494608322588006L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
