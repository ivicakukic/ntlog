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
package org.apache.logging.ntlog.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import static org.apache.logging.ntlog.Priority.ALL_INT;
import static org.apache.logging.ntlog.Priority.DEBUG_INT;
import static org.apache.logging.ntlog.Priority.ERROR_INT;
import static org.apache.logging.ntlog.Priority.INFO_INT;
import static org.apache.logging.ntlog.Priority.OFF_INT;
import static org.apache.logging.ntlog.Priority.WARN_INT;

public class NTEventLogAppender extends AppenderBase<ILoggingEvent> {

    private Layout layout;

    private org.apache.logging.ntlog.NTEventLogAppender appender;

    private String source;
    private String server;

    public NTEventLogAppender() {
        this.appender = new org.apache.logging.ntlog.NTEventLogAppender(server, source);
    }

    public String getSource() {
        if (this.appender != null) {
            return this.appender.getSource();
        } else {
            return this.source;
        }
    }

    public void setSource(String source) {
        if (this.appender != null) {
            this.appender.setSource(source);
        } else {
            this.source = source;
        }
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    private boolean checkConditions() {
        return appender != null && appender.isRegistered();
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (!isStarted() || !checkConditions()) {
            return;
        }

        String msg = layout.doLayout(event);
        if (msg == null) {
            return;
        }

        StringBuilder sbuf = new StringBuilder();
        sbuf.append(msg);
        int level = convertLevel(event.getLevel());

        appender.reportEvent(sbuf.toString(), level);
    }

    @Override
    public void start() {
        //System.out.println("logback.NTEventLogAppender.start");
        super.start();

        String source = this.source != null ? this.source.trim() : "logback";

        if (appender == null) {
            appender = new org.apache.logging.ntlog.NTEventLogAppender(server, source);
        }

        registerEventSource();
    }

    @Override
    public void stop() {
        //System.out.println("logback.NTEventLogAppender.stop");
        super.stop();

        if (checkConditions()) {
            deregisterEventSource();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            //System.out.println("logback.NTEventLogAppender.finalize");
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

    private int convertLevel(Level l) {
        if (Level.OFF.equals(l)) {
            return OFF_INT;
        } else if (Level.ERROR.equals(l)) {
            return ERROR_INT;
        } else if (Level.WARN.equals(l)) {
            return WARN_INT;
        } else if (Level.INFO.equals(l)) {
            return INFO_INT;
        } else if (Level.DEBUG.equals(l)) {
            return DEBUG_INT;
        } else if (Level.ALL.equals(l)) {
            return ALL_INT;
        } else {
            return ALL_INT;
        }
    }

}
