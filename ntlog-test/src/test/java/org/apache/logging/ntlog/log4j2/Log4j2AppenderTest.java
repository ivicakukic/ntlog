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

package org.apache.logging.ntlog.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;


public class Log4j2AppenderTest {

    private static final Logger logger = LogManager.getLogger(Log4j2AppenderTest.class);

    @Test
    public void testLogging () {
        logger.trace("trace message from log4j2.NTEventLogAppenderTest");
        logger.debug("debug message from log4j2.NTEventLogAppenderTest");
        logger.info("info message from log4j2.NTEventLogAppenderTest");
        logger.warn("warn message from log4j2.NTEventLogAppenderTest");
        logger.fatal("warn message from log4j2.NTEventLogAppenderTest");
        
        try {
            String a = null;
            int index = a.indexOf("as");
        } catch (Exception ex) {
            logger.error("Sample error message", ex);
        }
    }

}
