package me.ele.talaris.bean;

public class BeanAccessException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public BeanAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanAccessException(String message) {
        super(message);
    }

    public BeanAccessException(Throwable cause) {
        super(cause);
    }

}
