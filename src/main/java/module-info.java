// Part of SLF4J extension for NoException: https://noexception.machinezoo.com/slf4j
/**
 * SLF4J extension for NoException. See {@link com.machinezoo.noexception.slf4j.ExceptionLogging} class.
 * 
 * @see <a href="https://noexception.machinezoo.com/slf4j">Homepage of SLF4J extension for NoException</a>
 */
module com.machinezoo.noexception.slf4j {
	exports com.machinezoo.noexception.slf4j;
	requires transitive com.machinezoo.noexception;
	requires transitive org.slf4j;
}
