package nl.marcvanandel.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SLF4J implementation.
 *
 * @author marc-va
 * @since 1.0 on 11/02/2017
 */
public abstract class Slf4jLoggingDelegate extends AbstractLoggingDelegate {

    protected Logger logger;

    public Slf4jLoggingDelegate() {
        super();
    }

    public Slf4jLoggingDelegate(Class<?> clazz) {
        super(clazz);
    }

    /**
     * Hook to get the real {@link Logger} instance of the delegate. This should
     * be used but could be convenient in some situations.
     *
     * @return instance of the real logger of the delegate
     */
    public Logger getLoggerInstance() {
        return logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    /**
     * Creates the actual {@link Logger} instance.
     *
     * @param category
     */
    @Override
    protected void createLogger(String category) {
        logger = LoggerFactory.getLogger(category);
    }

}
