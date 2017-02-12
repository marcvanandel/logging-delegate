package nl.marcvanandel.logging.example;

import nl.marcvanandel.logging.Slf4jLoggingDelegate;

/**
 * Example business service implementation only as example for application of the {@link LoggingDelegate} pattern.
 * <p>
 * See blog post: <a href="http://marcvanandel.nl/logging-delegate/">http://marcvanandel.nl/logging-delegate/</a>
 *
 * @author marc-va
 * @since 1.0 on 12/02/2017
 */
public class BusinessServiceImpl {

    private LoggingDelegate logger;

    private BusinessServiceImpl otherBusinessService;

    public BusinessServiceImpl() {
        super();
        logger = new LoggingDelegate();
    }

    /**
     * Do some logic in this business service. Because this method is public this is exposed logic outside of the
     * business service, probably the reason of this business service.
     *
     * @param input
     * @return the result
     */
    public String doSomething(final String input) {
        logger.logDoSomethingStart(input);
        String result = otherBusinessService.executeLogic(input);
        logger.logDoSomethingFinish(input, result);
        return result;
    }

    /**
     * Example method to execute some logic. In the real world this would be real functional logic (of course ;-)
     *
     * @param input
     * @return the result
     */
    String executeLogic(String input) {
        return "the result";
    }

    public void setOtherBusinessService(BusinessServiceImpl otherBusinessService) {
        this.otherBusinessService = otherBusinessService;
    }

    private static class LoggingDelegate extends Slf4jLoggingDelegate {

        public void logDoSomethingStart(final String input) {
            logger.debug("doSomething START with [{}]", input);
        }

        public void logDoSomethingFinish(final String input, final String result) {
            logger.info("doSomething FINISH with [{}], result: [{}]", input, result);
        }

    }

}
