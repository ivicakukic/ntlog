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

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.TTCCLayout;
import org.apache.log4j.spi.LoggingEvent;

public class NTEventLogAppender extends AppenderSkeleton {

    private org.apache.logging.ntlog.NTEventLogAppender appender;

    public NTEventLogAppender() {
        this(null, null, null);
    }

    public NTEventLogAppender(String source) {
        this(null, source, null);
    }

    public NTEventLogAppender(String server, String source) {
        this(server, source, null);
    }

    public NTEventLogAppender(Layout layout) {
        this(null, null, layout);
    }

    public NTEventLogAppender(String source, Layout layout) {
        this(null, source, layout);
    }

    public NTEventLogAppender(String server, String source, Layout layout) {
        if (source == null) {
            source = "Log4j";
        }
        if (layout == null) {
            this.layout = new TTCCLayout();
        } else {
            this.layout = layout;
        }

        this.appender = new org.apache.logging.ntlog.NTEventLogAppender(server, source);
    }

    @Override
    public void close() {
        try {
            this.appender.deregisterEventSource();
        } catch (Throwable e) {
        }
    }

    public void activateOptions() {
        try {
            this.appender.registerEventSource();
        } catch (Throwable e) {
        }
    }

    public void append(LoggingEvent event) {
        if (!appender.isRegistered()) {
            return;
        }
        
        StringBuffer sbuf = new StringBuffer();
        
        sbuf.append(layout.format(event));
        if (layout.ignoresThrowable()) {
            String[] s = event.getThrowableStrRep();
            if (s != null) {
                int len = s.length;
                for (int i = 0; i < len; i++) {
                    sbuf.append(s[i]);
                    sbuf.append("\n");
                }
            }
        }
        // Normalize the log message level into the supported categories
        int nt_category = event.getLevel().toInt();

        // Anything above FATAL or below DEBUG is labeled as INFO.
        //if (nt_category > FATAL || nt_category < DEBUG) {
        //  nt_category = INFO;
        //}
        this.appender.reportEvent(sbuf.toString(), nt_category);
    }

    /**
     * The <b>Source</b> option which names the source of the event. The current
     * value of this constant is <b>Source</b>.
     */
    public void setSource(String source) {
        this.appender.setSource(source.trim());
    }

    public String getSource() {
        return appender.getSource();
    }

    /**
     * The <code>NTEventLogAppender</code> requires a layout. Hence, this method
     * always returns <code>true</code>.
     */
    public boolean requiresLayout() {
        return true;
    }

}
