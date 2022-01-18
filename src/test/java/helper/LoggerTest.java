package helper;

import junit.framework.TestCase;
import org.junit.Before;

public class LoggerTest extends TestCase {

    Logger logger = new Logger();

    public void testAddLog() {
        logger.addLog("test");
        assertEquals(1,logger.getLogs().size());
    }


}