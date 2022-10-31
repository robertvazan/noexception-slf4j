// Part of SLF4J extension for NoException: https://noexception.machinezoo.com/slf4j
package com.machinezoo.noexception.slf4j;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.IsInstanceOf.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.security.*;
import java.util.function.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import com.github.valfirst.slf4jtest.*;
import com.machinezoo.noexception.*;

@ExtendWith(TestLoggerFactoryExtension.class)
public class ExceptionLoggingTest {
	TestLogger sharedLogger = TestLoggerFactory.getTestLogger(ExceptionLogging.class);
	TestLogger customLogger = TestLoggerFactory.getTestLogger(ExceptionLoggingTest.class);
	@Test
	public void log_runtime() {
		ExceptionLogging.log().run(() -> {
			throw new NumberFormatException();
		});
		assertEquals(1, sharedLogger.getLoggingEvents().size());
		LoggingEvent event = sharedLogger.getLoggingEvents().get(0);
		assertThat(event.getThrowable().orElse(null), instanceOf(NumberFormatException.class));
		assertEquals("Caught exception.", event.getMessage());
	}
	@Test
	public void log_error() {
		ExceptionLogging.log().run(() -> {
			throw new IOError(new IOException());
		});
		assertEquals(1, sharedLogger.getLoggingEvents().size());
		assertThat(sharedLogger.getLoggingEvents().get(0).getThrowable().orElse(null), instanceOf(IOError.class));
	}
	@Test
	public void log_interrupt() {
		ExceptionLogging.log().run(Exceptions.sneak().runnable(() -> {
			throw new InterruptedException();
		}));
		assertTrue(Thread.interrupted());
		assertEquals(1, sharedLogger.getLoggingEvents().size());
		assertThat(sharedLogger.getLoggingEvents().get(0).getThrowable().orElse(null), instanceOf(InterruptedException.class));
	}
	@Test
	public void logTo() {
		ExceptionLogging.log(customLogger).run(() -> {
			throw new NumberFormatException();
		});
		assertEquals(0, sharedLogger.getLoggingEvents().size());
		assertEquals(1, customLogger.getLoggingEvents().size());
		assertThrows(NullPointerException.class, () -> ExceptionLogging.log(null));
	}
	@Test
	public void logWithMessage() {
		ExceptionLogging.log(customLogger, "Commented exception.").run(() -> {
			throw new NumberFormatException();
		});
		assertEquals(1, customLogger.getLoggingEvents().size());
		assertEquals("Commented exception.", customLogger.getLoggingEvents().get(0).getMessage());
		assertThrows(NullPointerException.class, () -> ExceptionLogging.log(null, "Message."));
		assertThrows(NullPointerException.class, () -> ExceptionLogging.log(customLogger, (String)null));
	}
	@Test
	public void logWithLazyMessage() {
		ExceptionLogging.log(customLogger, () -> "Lazy message.").run(() -> {
			throw new NumberFormatException();
		});
		assertEquals(1, customLogger.getLoggingEvents().size());
		assertEquals("Lazy message.", customLogger.getLoggingEvents().get(0).getMessage());
		assertThrows(NullPointerException.class, () -> ExceptionLogging.log(null, () -> "Message."));
		assertThrows(NullPointerException.class, () -> ExceptionLogging.log(customLogger, (Supplier<String>)null));
	}
	@Test
	public void logWithLazyMessage_null() {
		ExceptionLogging.log(customLogger, () -> null).run(() -> {
			throw new NumberFormatException();
		});
		assertEquals(1, customLogger.getLoggingEvents().size());
		LoggingEvent event = customLogger.getLoggingEvents().get(0);
		assertThat(event.getThrowable().orElse(null), instanceOf(NumberFormatException.class));
	}
	@Test
	public void logWithLazyMessage_throwing() {
		ExceptionLogging.log(customLogger, () -> {
			throw new InvalidParameterException();
		}).run(() -> {
			throw new NumberFormatException();
		});
		assertEquals(2, customLogger.getLoggingEvents().size());
		LoggingEvent event1 = customLogger.getLoggingEvents().get(0);
		assertThat(event1.getThrowable().orElse(null), instanceOf(InvalidParameterException.class));
		LoggingEvent event2 = customLogger.getLoggingEvents().get(1);
		assertThat(event2.getThrowable().orElse(null), instanceOf(NumberFormatException.class));
	}
}
