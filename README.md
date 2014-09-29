ntlog
=====

About the project
------------------------------
Project contains windows nt log appenders for common loggins frameworks, including log4j, log4j2 and logback. 

It is an adaptation NTEventLogAppender provided with log4j, allowing the following:
- usage of same native DLL with different logging frameworks
- implementation of own appender for any logging framework without the need to reference and use log4j NTEventLogAppender
- usage within application servers

Project will build 4 jars: 
- ntlog-dll-1.0.jar
- ntlog-log4j-1.0.jar
- ntlog-log4j2-1.0.jar
- ntlog-logback-1.0.jar

To use with one of the frameworks include ntlog-dll-1.0.jar along with the jar apropriate for your logging framework. 

Configuration example for all supported frameworks can be seen in test project. Tests assume that NTEventLogAppender.dll 
is included in windows\system32 folder or your classpath. 


Usage with application servers
------------------------------
To use appenders in application server environments, place the ntlog-dll-1.0.jar in a folder visible to server class loader. Applications can use include separate logging frameworks or use a shared logger, as long as ntlog-dll-1.0.jar is shared. Application restarts are also supported, since the DLL is loaded from a class created by server class loader. 

For tomcat 7 use lib folder. 
