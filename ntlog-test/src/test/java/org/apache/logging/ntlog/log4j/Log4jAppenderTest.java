/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.logging.ntlog.log4j;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

public class Log4jAppenderTest {

    private static final Logger logger = Logger.getLogger(Log4jAppenderTest.class);

    @Test
    public void testLogging () {
        logger.trace("trace message from log4j.NTEventLogAppenderTest");
        logger.debug("debug message from log4j.NTEventLogAppenderTest");
        logger.info("info message from log4j.NTEventLogAppenderTest");
        logger.warn("warn message from log4j.NTEventLogAppenderTest");
        logger.fatal("warn message from log4j.NTEventLogAppenderTest");
        try {
            String a = null;
            int index = a.indexOf("as");
        } catch (Exception ex) {
            logger.error("Sample error message", ex);
        }
    }
    
}
