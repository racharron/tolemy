package tolemy.rr;

public class ContextCreationException extends Exception {

	public ContextCreationException() {
	}

	public ContextCreationException(String message) {
		super(message);
	}

	public ContextCreationException(Throwable cause) {
		super(cause);
	}

	public ContextCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContextCreationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
