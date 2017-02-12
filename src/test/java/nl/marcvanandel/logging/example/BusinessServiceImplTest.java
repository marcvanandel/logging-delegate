package nl.marcvanandel.logging.example;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link BusinessServiceImpl}.
 *
 * @author marc-va
 * @since 1.0 on 12/02/2017
 */
public class BusinessServiceImplTest {

    private BusinessServiceImpl businessService;

    @Before
    public void setUp() {
        businessService = new BusinessServiceImpl();
        businessService.setOtherBusinessService(businessService);
    }

    @Test
    public void testDoSomething() {
        String input = "the input";
        String actual = businessService.doSomething(input);
        assertEquals("outcome of doSomething", "the result", actual);
    }

}
