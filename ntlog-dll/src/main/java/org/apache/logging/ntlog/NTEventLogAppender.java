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

package org.apache.logging.ntlog;

public class NTEventLogAppender {

    private int _handle = 0;
    private String source = null;
    private String server = null;

    public NTEventLogAppender(String server, String source) {
        //System.out.println("Construction of NTEventLogAppender");
        this.server = server;
        this.source = source;
    }

    native private int nativeRegisterEventSource(String server, String source);

    native private void nativeDeregisterEventSource(int handle);

    native private void nativeReportEvent(int handle, String message, int level);

    public void registerEventSource() {
        if (_handle == 0) {
            try {
                //System.out.println(String.format("registerEventSource source = %s", source));
                _handle = nativeRegisterEventSource(server, source);
                //System.out.println(String.format("registerEventSource result handle = %d source = %s", _handle, source));
            } catch (Throwable t) {
                t.printStackTrace();
                _handle = 0;
            }
        }
    }

    public void deregisterEventSource() {
        if (_handle != 0) {
            try {
                nativeDeregisterEventSource(_handle);
                //System.out.println(String.format("deregisterEventSource handle = %d", _handle));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        _handle = 0;
    }

    public boolean isRegistered() {
        return _handle != 0;
    }

    public void reportEvent(String message, int level) {
        if (_handle != 0) {
            nativeReportEvent(_handle, message, level);
        }
    }

    /**
     * The <b>Source</b> option which names the source of the event. The current
     * value of this constant is <b>Source</b>.
     */
    public void setSource(String source) {
        this.source = source.trim();
    }

    public String getSource() {
        return source;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            //System.out.println("finalize");
            if (_handle != 0) {
                deregisterEventSource();
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            super.finalize();
        }
    }

    static {
        String[] archs;
        try {
            archs = new String[]{System.getProperty("os.arch")};
        } catch (SecurityException e) {
            archs = new String[]{"amd64", "ia64", "x86"};
        }
        boolean loaded = false;
        for (int i = 0; i < archs.length; i++) {
            try {
                System.loadLibrary("NTEventLogAppender." + archs[i]);
                loaded = true;
                break;
            } catch (java.lang.UnsatisfiedLinkError e) {
                loaded = false;
            }
        }
        if (!loaded) {
            try {
                System.loadLibrary("NTEventLogAppender");
            } catch (UnsatisfiedLinkError e) {
                e.printStackTrace();
            }
        }
    }
}
