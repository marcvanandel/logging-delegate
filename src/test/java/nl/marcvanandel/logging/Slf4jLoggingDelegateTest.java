package nl.marcvanandel.logging;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * TODO JavaDoc
 *
 * @author marc
 * @since 0.1 on 11/02/2017
 */
public class Slf4jLoggingDelegateTest {

    @Test
    public void testDefaultLoggingDelegate() {
        Slf4jLoggingDelegate logger = new Slf4jLoggingDelegate() {
        };

        String actualName = logger.getLoggerInstance().getName();
        logger.getLoggerInstance().info("testing it with [{}] ;-)", this);
        assertEquals("logger name",
                "nl.marcvanandel.logging.Slf4jLoggingDelegateTest",
                actualName);
    }

}
