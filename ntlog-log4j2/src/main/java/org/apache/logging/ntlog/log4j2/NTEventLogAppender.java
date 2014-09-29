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

import java.io.Serializable;
import static org.apache.logging.ntlog.Priority.ALL_INT;
import static org.apache.logging.ntlog.Priority.DEBUG_INT;
import static org.apache.logging.ntlog.Priority.ERROR_INT;
import static org.apache.logging.ntlog.Priority.FATAL_INT;
import static org.apache.logging.ntlog.Priority.INFO_INT;
import static org.apache.logging.ntlog.Priority.OFF_INT;
import static org.apache.logging.ntlog.Priority.WARN_INT;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.spi.StandardLevel;

@Plugin(name = "NTEventLog", category = "Core", elementType = "appender", printObject = true)
public final class NTEventLogAppender extends AbstractAppender {

    private org.apache.logging.ntlog.NTEventLogAppender appender;

    private NTEventLogAppender(String name, String server, String source, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);

        this.appender = new org.apache.logging.ntlog.NTEventLogAppender(server, source);
    }

    @PluginFactory
    public static NTEventLogAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginAttribute("server") String server,
            @PluginAttribute("source") String source,
            @PluginElement("Layout") Layout layout,
            @PluginElement("Filters") Filter filter,
            @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {

        if (name == null) {
            LOGGER.error("No name provided for NtEventProvider");
            return null;
        }

        if (source == null) {
            source = "Log4j2";
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        return new NTEventLogAppender(name, server, source, layout, filter, ignoreExceptions);
    }

    @Override
    public void append(LogEvent event) {
        if (!appender.isRegistered()) {
            return;
        }

        StringBuilder sbuf = new StringBuilder();
        sbuf.append(getLayout().toSerializable(event));

        int level = convertLevel(event.getLevel().getStandardLevel());

        appender.reportEvent(sbuf.toString(), level);
    }
    
    @Override
    protected void setStarting() {
        //System.out.println("log4j2.NTEventLogAppender.setStarting");
        super.setStarting();
        registerEventSource();
    }    

    @Override
    protected void setStopping() {
        //System.out.println("log4j2.NTEventLogAppender.setStopping");
        super.setStopping();
        deregisterEventSource();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            //System.out.println("log4j2.NTEventLogAppender.finalize");
            deregisterEventSource();
        } catch (Throwable t) {
            throw t;
        } finally {
            super.finalize();
        }
    }

    private void registerEventSource() {
        appender.registerEventSource();
    }

    private void deregisterEventSource() {
        appender.deregisterEventSource();
    }

    private int convertLevel(StandardLevel sl) {
        if (StandardLevel.OFF.equals(sl)) {
            return OFF_INT;
        } else if (StandardLevel.FATAL.equals(sl)) {
            return FATAL_INT;
        } else if (StandardLevel.ERROR.equals(sl)) {
            return ERROR_INT;
        } else if (StandardLevel.WARN.equals(sl)) {
            return WARN_INT;
        } else if (StandardLevel.INFO.equals(sl)) {
            return INFO_INT;
        } else if (StandardLevel.DEBUG.equals(sl)) {
            return DEBUG_INT;
        } else if (StandardLevel.ALL.equals(sl)) {
            return ALL_INT;
        } else {
            return ALL_INT;
        }
    }

}
