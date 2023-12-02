package by.belyahovich.demo.utils;

public class ReservationException extends RuntimeException {
    /**
     * Create custom exception for clear own application stacktrace
     *
     * @param message exception description
     */
    public ReservationException(String message) {
        super(message);
        this.setStackTrace(new StackTraceElement[0]);
    }
}
