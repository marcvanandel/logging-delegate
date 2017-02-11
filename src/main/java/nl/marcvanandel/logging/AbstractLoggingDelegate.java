package nl.marcvanandel.logging;

/**
 * Abstract class for applying the <i>'logging delegate pattern'</i> more
 * convenient than implementing it yourself every time.
 * <p/>
 * <p>
 * The <b>'logging delegate pattern'</b> is based on the clean code idea of
 * having all lines at the same level of abstraction inside your code. For
 * example a service calling methods inside the same service and on other
 * services. These methods and service names are self explanatory by naming. And
 * they indicate what the method will be doing. Logging statements are not of
 * the same level of abstraction as these other methods.
 * </p>
 * <p>
 * One solution is to extract the logging statements into separate methods
 * inside the same object / service. That is the idea behind the logging
 * delegate pattern as well. But because these logging methods can be scattered
 * over the class it would be nicer if they were bundled somehow. Besides this
 * logging is probably not the core business of your object. Should your object
 * actually be logging at all?
 * </p>
 * <p>
 * Extracting all logging methods into a separate class outside you actual
 * object will double the amount of class. And the pattern will become a service
 * and a service logger again and again. So this is where the logging delegate
 * pattern is getting into full view: <b>an inner class as a separate yet
 * enclosed class bundling all logging methods</b>
 * </p>
 * <p>
 * This abstract class provides an easy way to apply the logging delegate
 * pattern. It creates (and holds) the actual SLF4J {@link Logger}. It can be
 * created in several ways and has some ways of extension in mind.
 * </p>
 * <p>
 * The most easy and most convenient way of using this pattern and class is
 * simply create a subclass inside your actual object and construct it directly:
 * <p/>
 * <pre>
 * <code>
 *   private LoggingDelegate logger = new LoggingDelegate();
 *
 *   public void yourMethod() {
 *   	// use the logging methods
 *   	logger.loggingMethod();
 *   	// other logic
 *   }
 *
 *   // it is common usage to put the inner class at the bottom of your class
 *   private static class LoggingDelegate extends AbstractLoggingDelegate {
 *
 *   	// your logging methods here
 *
 *   }
 * </code>
 * </pre>
 * <p/>
 * Static usage is possible as well:
 * <p/>
 * <pre>
 * <code>
 *   private static LoggingDelegate logger = new LoggingDelegate(YourClass.class);
 *
 *   private static class LoggingDelegate extends AbstractLoggingDelegate {
 *
 *   	// your logging methods here
 *
 *   }
 * </code>
 * </pre>
 * <p/>
 * This will use the default constructor {@link #AbstractLoggingDelegate()}. For
 * extension and other usage of the logging delegate pattern see the other
 * constructors and protected methods which provides hooks to extend the default
 * behaviour.
 * </p>
 * <p>
 * This abstract class does have some extension options for adding a prefix and/or
 * a postfix to the logger category.
 * </p>
 *
 * @author marc-va
 * @since 1.0 on 11/02/2017
 */
public abstract class AbstractLoggingDelegate {

    /**
     * Default constructor.
     * <p>
     * This will instantiate an actual {@link Logger} based on the enclosing
     * class assuming the concrete subclass of this abstract class is an inner
     * class. This is the general and common usage of the 'logging delegate
     * pattern'.
     * </p>
     * <p>
     * A custom <code>Logger</code> can be instantiated by overriding the
     * {@link #buildLoggerCategory()} method. By doing so it'll be possible to
     * implement a prefix or postfix to the category and therefore the
     * <code>Logger</code>.
     * </p>
     *
     * @see also #buildLoggerCategory()
     */
    public AbstractLoggingDelegate() {
        super();
        String category = buildLoggerCategory();
        createLogger(category);
    }

    /**
     * Second default constructor extending the behaviour of the default
     * constructor {@link #AbstractLoggingDelegate()}. This constructor takes a
     * class as a parameter to derive the logger from.
     * <p>
     * This constructor should be used for static instances of concrete
     * subclasses.
     * </p>
     *
     * @param clazz the class to derive the logger from
     */
    public AbstractLoggingDelegate(Class<?> clazz) {
        super();
        String category = buildLoggerCategory(clazz);
        createLogger(category);
    }

    /**
     * Abstract method which all concrete extension need to implement for specific implementations. This method is
     * called in the constructor and intends to instantiate the actual logger instance in the logging delegate.
     *
     * @param category
     */
    abstract protected void createLogger(String category);

    abstract public boolean isDebugEnabled();

    abstract public boolean isInfoEnabled();

    /**
     * Callback method to override when there's the need for a prefix. The default implementation returns <code>null</code>.
     *
     * @return the prefix for the Logger Category; null by default
     */
    protected String getPrefix() {
        return null;
    }

    /**
     * Callback method to override when there's the need for a postfix. The default implementation returns <code>null</code>.
     *
     * @return the postfix for the Logger Category; null by default
     */
    protected String getPostfix() {
        return null;
    }

    /**
     * Returns the general category for the logger. The default implementation
     * is supporting the common usage of any logger so will return the full
     * class name: {@link #getClass()}. Following the 'logging delegate pattern'
     * the default implementation is expecting to be in a concrete class as
     * inner class of the actual object which likes to log something.
     * <p/>
     * <p>
     * Any subclass can override this method to implement any other general
     * category pattern. For example to appendPrefix a prefix or postfix to the common
     * category:
     * </p>
     * <p/>
     * <pre>
     * <code>
     * {@literal @}Override
     * protected String buildLoggerCategory() {
     * 	String category = "prefix.";
     * 	category += super.buildLoggerCategory();
     * }
     * </code>
     * </pre>
     *
     * @return general logger category
     */
    protected String buildLoggerCategory() {
        return buildLoggerCategory(getClass().getEnclosingClass());
    }

    /**
     * Returns the general category for the logger. This method supports the
     * common usage of any logger so will just return the name of the given
     * class. It is null safe so when a null class is provided the category will
     * be derived from this class itself.
     *
     * @param clazz the class the derive the category from
     * @return general logger category
     */
    protected String buildLoggerCategory(Class<?> clazz) {
        String category = getCategoryFromClass(clazz);
        if (assertCategory(category)) {
            category = buildFallbackLoggerCategory();
        }
        return category;
    }

    /**
     * Returns the category for this abstract class as a fall back scenario when
     * other scenarios didn't derive to a proper category.
     *
     * @return logger category
     */
    protected String buildFallbackLoggerCategory() {
        return getCategoryFromClass(getClass());
    }

    /**
     * Derive the category of the given class.
     *
     * @param clazz
     * @return category derived of the given class
     */
    protected String getCategoryFromClass(Class<?> clazz) {
        StringBuilder category = new StringBuilder();
        if (clazz != null) {
            appendPrefix(category);
            category.append(clazz.getName());
            appendPostfix(category);
        }
        return category.toString();
    }

    private void appendPrefix(StringBuilder category) {
        String prefix = getPrefix();
        if (prefix != null) {
            category.append(prefix);
            if (!prefix.endsWith(".")) {
                category.append(".");
            }
        }
    }

    private void appendPostfix(StringBuilder category) {
        String postfix = getPostfix();
        if (postfix != null) {
            category.append(postfix);
        }
    }

    private boolean assertCategory(String category) {
        return category == null;
    }

}
