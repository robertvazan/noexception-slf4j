// Part of SLF4J extension for NoException: https://noexception.machinezoo.com/slf4j
package com.machinezoo.noexception.slf4j;

import java.util.*;
import java.util.function.*;
import org.slf4j.*;
import com.machinezoo.noexception.*;

/**
 * Static methods for creating exception handlers that log exceptions to SLF4J loggers.
 * These handlers complement core handlers defined in {@link Exceptions} class.
 * <p>
 * Typical usage: {@code ExceptionLogging.log().get(() -> my_throwing_lambda).orElse(fallback)}
 * <p>
 * 
 * @see <a href="https://noexception.machinezoo.com/slf4j">Homepage of SLF4J extension for NoException</a>
 */
public class ExceptionLogging {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionLogging.class);
	private static final LoggingHandler DEFAULT = new LoggingHandler(logger, () -> "Caught exception.");
	/**
	 * Returns {@code ExceptionHandler} that writes all exceptions to common logger.
	 * Logs are written to SLF4J logger named after this class.
	 * This handler is convenient and a suitable default choice,
	 * but the single shared logger can make logs harder to filter.
	 * Use {@link #log(Logger)} to specify custom logger where filtering is important.
	 * <p>
	 * Typical usage: {@code Exceptions.log().run(() -> my_throwing_lambda)}
	 * <p>
	 * No exceptions are allowed through, not even {@code Error}s.
	 * If {@link InterruptedException} is caught, {@link Thread#interrupt()} is called.
	 * 
	 * @return logging exception handler
	 * @see #log(Logger)
	 */
	public static ExceptionHandler log() {
		return DEFAULT;
	}
	/**
	 * Creates {@code ExceptionHandler} that writes all exceptions to the specified {@code logger}.
	 * Most application code can use the more convenient {@link #log()} method.
	 * Use {@link #log(Logger, String)} overload to specify unique message where necessary.
	 * <p>
	 * Typical usage: {@code Exceptions.log(logger).run(() -> my_throwing_lambda)}
	 * <p>
	 * No exceptions are allowed through, not even {@code Error}s.
	 * If {@link InterruptedException} is caught, {@link Thread#interrupt()} is called.
	 * 
	 * @param logger
	 *            where all exceptions are logged
	 * @return exception handler with custom logger
	 * @throws NullPointerException
	 *             if {@code logger} is {@code null}
	 * @see #log()
	 * @see #log(Logger, String)
	 */
	public static ExceptionHandler log(Logger logger) {
		return new LoggingHandler(logger, () -> "Caught exception.");
	}
	/**
	 * Creates {@code ExceptionHandler} that writes all exceptions to the specified {@code logger} with the specified {@code message}.
	 * If you just need to specify custom logger, use {@link #log(Logger)}.
	 * This overload allows for differentiating or explanatory message.
	 * If the message is expensive to construct, use {@link #log(Logger, Supplier)} method.
	 * <p>
	 * Typical usage: {@code Exceptions.log(logger, "Failed to do X.").run(() -> my_throwing_lambda)}
	 * <p>
	 * No exceptions are allowed through, not even {@code Error}s.
	 * If {@link InterruptedException} is caught, {@link Thread#interrupt()} is called.
	 * 
	 * @param logger
	 *            where all exceptions are logged
	 * @param message
	 *            introduces every exception in the log
	 * @return exception handler with custom logger and message
	 * @throws NullPointerException
	 *             if {@code logger} or {@code message} is {@code null}
	 * @see #log(Logger)
	 * @see #log(Logger, Supplier)
	 */
	public static ExceptionHandler log(Logger logger, String message) {
		Objects.requireNonNull(message);
		return new LoggingHandler(logger, () -> message);
	}
	/**
	 * Creates {@code ExceptionHandler} that writes all exceptions to the specified {@code logger} with lazily evaluated {@code message}.
	 * If the message does not need lazy evaluation, use the {@link #log(Logger, String)} method.
	 * This overload constructs expensive messages lazily only when exception is caught.
	 * If {@code message} throws, the exception is logged and fallback message is used to log the original exception.
	 * <p>
	 * Typical usage: {@code Exceptions.log(logger, () -> "Exception in " + this).run(() -> my_throwing_lambda)}
	 * <p>
	 * No exceptions are allowed through, not even {@code Error}s.
	 * If {@link InterruptedException} is caught, {@link Thread#interrupt()} is called.
	 * 
	 * @param logger
	 *            where all exceptions are logged
	 * @param message
	 *            a {@link Supplier} that is lazily evaluated to generate log message
	 * @return exception handler with custom logger and lazily evaluated message
	 * @throws NullPointerException
	 *             if {@code logger} or {@code message} is {@code null}
	 * @see #log(Logger, String)
	 */
	public static ExceptionHandler log(Logger logger, Supplier<String> message) {
		return new LoggingHandler(logger, message);
	}
}
