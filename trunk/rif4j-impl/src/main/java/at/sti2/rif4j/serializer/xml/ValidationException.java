package at.sti2.rif4j.serializer.xml;

public class ValidationException extends RuntimeException {

	/**
	 * 
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
