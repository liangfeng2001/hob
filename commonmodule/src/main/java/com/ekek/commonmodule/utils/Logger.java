package com.ekek.commonmodule.utils;

import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {

    // Constants
    private static final String DEFAULT_TAG = "EKEKTAG";
    private static final String LOG_FOLDER_NAME = "log";
    private static final String LOG_CONFIGURATION_FILE = "log.config";
    private static final String LOG_FILE_PREFIX = "log_";

    // XML NODE definitions
    private static final String XN_CONFIG = "Configuration";
    private static final String XN_VERSION = "Version";
    private static final int XV_VERSION = 1;
    private static final String XN_VALUE = "Value";
    private static final String XN_TAG = "Tag";

    private static final String XN_LOGGING_LEVEL = "LoggingLevel";
    private static final int DEFAULT_LOGGING_LEVEL = Log.INFO;

    private static final String XN_DAYS_TO_KEEP = "DaysToKeep";
    private static final int DEFAULT_DAYS_TO_KEEP = 9;
    private static final int LOG_FOLDERS_TO_KEEP = 36;

    // Fields
    private static String logFolder;
    private String configurationFileName;
    private int configFileVersion;
    private String tag;
    private int loggingLevel;
    private int loggingLevelOriginal;
    private int daysToKeep;
    private static boolean initialized = false;

    // Singleton
    private Logger() {
        this.tag = DEFAULT_TAG;
        this.loggingLevel = DEFAULT_LOGGING_LEVEL;
        this.loggingLevelOriginal = loggingLevel;
    }
    private static Logger instance;
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    } // end Singleton

    // Public functions
    public static void initializeLoggingSystem(String appDataFolder) {
        createFolders(appDataFolder);
        loadLoggingParameters();
        initialized = true;
        Logger.getInstance().i("Finished initializeLoggingSystem(" + appDataFolder + ")");
    }
    public void v(String aMessage) {
        log(aMessage, Log.VERBOSE, false);
    }
    public void v(String aMessage, boolean includeStack) {
        log(aMessage, Log.VERBOSE, includeStack);
    }
    public void d(String aMessage) {
        log(aMessage, Log.DEBUG, false);
    }
    public void d(String aMessage, boolean includeStack) {
        log(aMessage, Log.DEBUG, includeStack);
    }
    public void i(String aMessage) {
        log(aMessage, Log.INFO, false);
    }
    public void i(String aMessage, boolean includeStack) {
        log(aMessage, Log.INFO, includeStack);
    }
    public void w(String aMessage) {
        log(aMessage, Log.WARN, false);
    }
    public void w(String aMessage, boolean includeStack) {
        log(aMessage, Log.WARN, includeStack);
    }
    public void e(String aMessage) {
        log(aMessage, Log.ERROR, false);
    }
    public void e(String aMessage, boolean includeStack) {
        log(aMessage, Log.ERROR, includeStack);
    }
    public void e(Throwable ex) {
        e(ex.getClass().getName() + "->" + ex.getMessage());
        StackTraceElement[] stack = ex.getStackTrace();
        for (StackTraceElement element : stack) {
            writeLog(
                    Log.ERROR,
                    getTag(),
                    element.getFileName(),
                    element.getMethodName(),
                    element.getLineNumber(),
                    "");
        }
    }
    public void setConfigurationFileName(String configurationFileName) {
        this.configurationFileName = configurationFileName;
        if (!FileUtils.getInstance().isFileExist(configurationFileName)) {
            this.generateDefaultConfigFile();
        }
        this.loadConfiguration();
        if (this.configFileVersion < XV_VERSION) {
            this.reGenerateConfigFile();
            this.loadConfiguration();
        }
        this.clearLogDirectory(FileUtils.extractDirectoryName(this.configurationFileName));
    }

    // Private functions
    private void generateDefaultConfigFile() {
        this.configFileVersion = XV_VERSION;
        this.loggingLevel = DEFAULT_LOGGING_LEVEL;
        this.loggingLevelOriginal = loggingLevel;
        this.tag = DEFAULT_TAG;
        this.daysToKeep = DEFAULT_DAYS_TO_KEEP;
        GenConfigFileThread t = new GenConfigFileThread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e(e);
            e.printStackTrace();
        }
    }
    private void reGenerateConfigFile() {
        if (this.configFileVersion < 2) {
            this.daysToKeep = DEFAULT_DAYS_TO_KEEP;
        }
        this.configFileVersion = XV_VERSION;
        GenConfigFileThread t = new GenConfigFileThread(this);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e(e);
            e.printStackTrace();
        }
    }
    private void clearLogDirectory(String aDirName) {
        ClearDirectoryThread t = new ClearDirectoryThread(this, aDirName);
        t.start();
    }
    private void loadConfiguration() {
        try {
            File f = new File(this.configurationFileName);
            i("Start to load logging configurations from the file: " + this.configurationFileName);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new FileInputStream(f), "utf-8");
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tag = xpp.getName();
                    i("START_TAG: " + tag);
                    if (tag.equals(XN_CONFIG)) {
                        this.configFileVersion = Integer.parseInt(xpp.getAttributeValue(null, XN_VERSION));
                        i("XML_VERSION: " + this.configFileVersion);
                    } else if (tag.equals(XN_TAG)) {
                        this.tag = xpp.getAttributeValue(null, XN_VALUE);
                        i("TOMCAT_TAG: " + this.tag);
                    } else if (tag.equals(XN_LOGGING_LEVEL)) {
                        this.loggingLevel = Integer.parseInt(xpp.getAttributeValue(null, XN_VALUE));
                        this.loggingLevelOriginal = loggingLevel;
                        i("LOGGING_LEVEL: " + this.loggingLevel);
                    } else if (tag.equals(XN_DAYS_TO_KEEP)) {
                        this.daysToKeep= Integer.parseInt(xpp.getAttributeValue(null, XN_VALUE));
                        i("DAYS_TO_KEEP: " + this.daysToKeep);
                    }
                }
                eventType = xpp.next();
            }
            i("Finished loading logging configurations from the file: " + this.configurationFileName);
        } catch (XmlPullParserException e) {
            e(e);
        } catch (FileNotFoundException e) {
            e(e);
        } catch (IOException e) {
            e(e);
        }
    }
    private static void createFolders(String appDataFolder) {
        if (!FileUtils.getInstance().isFileExist(appDataFolder)) {
            FileUtils.getInstance().createSDDir(appDataFolder);
            Logger.getInstance().i("appDataFolder Created successfully: " + appDataFolder);
        }

        logFolder = appDataFolder;
        logFolder += appDataFolder.substring(appDataFolder.length() - 1).equals('/') ?
                LOG_FOLDER_NAME + '/':
                '/' + LOG_FOLDER_NAME + '/';

        if (!FileUtils.getInstance().isFileExist(logFolder)) {
            FileUtils.getInstance().createSDDir(logFolder);
            Logger.getInstance().i("logFolder Created successfully: " + logFolder);
        }

        backupExistingLog();
    }
    private static void loadLoggingParameters() {
        Logger.getInstance().setConfigurationFileName(logFolder + LOG_CONFIGURATION_FILE);
    }
    private static void backupExistingLog() {
        File logDir = new File(logFolder);
        if (!logDir.exists() || !logDir.isDirectory()) {
            return;
        }

        File[] files = logDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        // get information
        int folderCount = 0;
        int folderNameMax = 0;
        List<File> logFiles = new ArrayList<>();
        for (File f: files) {
            if (f.isDirectory()) {
                folderCount++;
                int x = Integer.parseInt(f.getName());
                if (x > folderNameMax) {
                    folderNameMax = x;
                }
            } else if (f.isFile()) {
                if (f.getName().startsWith(LOG_FILE_PREFIX)) {
                    logFiles.add(f);
                }
            }
        }

        // backup log files
        if (logFiles.size() > 0) {
            folderNameMax++;
            folderCount++;
            String newFolderFullName = logFolder + folderNameMax + "/";
            File newFolder = new File(newFolderFullName);
            if (!newFolder.mkdir()) {
                return;
            }

            for (int i = 0; i < logFiles.size(); i++) {
                logFiles.get(i).renameTo(new File(newFolderFullName + logFiles.get(i).getName()));
            }
        }

        // delete old log folders
        if (folderCount > LOG_FOLDERS_TO_KEEP) {
            for (File f: files) {
                if (f.isDirectory()) {
                    int x = Integer.parseInt(f.getName());
                    if (x <= folderNameMax - LOG_FOLDERS_TO_KEEP) {
                        for(File f2: f.listFiles()) {
                            f2.delete();
                        }
                        f.delete();
                    }
                }
            }
        }
    }
    private void log(String aMessage, int aLevel, boolean includeStack) {
        if (aLevel >= getLoggingLevel()) {
            StackTraceElement[] elements = new Throwable().getStackTrace();
            writeLog(
                    aLevel,
                    getTag(),
                    elements[2].getFileName(),
                    elements[2].getMethodName(),
                    elements[2].getLineNumber(),
                    aMessage);
            if (includeStack) {
                writeLog(aLevel, getTag(), "-- Stack -------", "---------", 1111, "----------------");
                for (int i = 3; i < elements.length; i++) {
                    String cName = elements[i].getFileName();
                    String mName = elements[i].getMethodName();
                    int lineNum = elements[i].getLineNumber();
                    writeLog(aLevel, getTag(), cName, mName, lineNum, "");
                }
                writeLog(aLevel, getTag(), "----------------", "---------", 9999, "----------------");
            }
        }
    }
    private synchronized void writeLog(
            int aLevel,
            String tag,
            String className,
            String methodName,
            int lineNumber,
            String message) {
        String line = createNewMessage(
                className,
                methodName,
                lineNumber,
                message);
        switch (aLevel) {
            case Log.VERBOSE:
                Log.v(tag, line);
                break;
            case Log.DEBUG:
                Log.d(tag, line);
                break;
            case Log.INFO:
                Log.i(tag, line);
                break;
            case Log.WARN:
                Log.w(tag, line);
                break;
            case Log.ERROR:
                Log.e(tag, line);
                break;
        }

        if (!initialized) {
            return;
        }

        Date now = new Date(SystemClock.elapsedRealtime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String filename = String.format(
                logFolder + LOG_FILE_PREFIX + "%s.txt",
                sdf.format(now));

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

        line = sdf.format(now) + " " + line;
        FileUtils.getInstance().appendText(filename, line);
    }
    private String createNewMessage(
            String className,
            String methodName,
            int lineNumber,
            String message) {
        StringBuffer sb = new StringBuffer();
        sb.append(className + "->" + methodName + " at " + lineNumber);
        sb.append(" " + message);
        return sb.toString();
    }

    // Functions for properties
    public String getTag() { return tag; }
    private int getLoggingLevel() { return loggingLevel; }
    public void setLoggingLevel(int loggingLevel) {
        this.loggingLevel = loggingLevel;
    }
    public void resetLoggingLevel() {
        this.loggingLevel = this.loggingLevelOriginal;
    }

    // Classes
    private class GenConfigFileThread extends Thread {
        // Fields
        private Logger master;
        // Constructors
        public GenConfigFileThread(Logger master) {
            super("Generate Configuration File");
            this.master = master;
        }
        // Overridden functions
        @Override
        public void run() {
            FileOutputStream fos = null;
            File f = null;
            try {
                i("Start to generate configurations to " + this.master.configurationFileName);
                if (FileUtils.getInstance().isFileExist(this.master.configurationFileName)) {
                    FileUtils.getInstance().deleteFile(this.master.configurationFileName);
                }
                f = FileUtils.getInstance().createSDFile(this.master.configurationFileName);
                fos = new FileOutputStream(f);
                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(fos, "UTF-8");
                serializer.startDocument("UTF-8", true);
                serializer.startTag(null, XN_CONFIG);
                serializer.attribute(null, XN_VERSION, "" + this.master.configFileVersion);
                i("XML_VERSION: " + this.master.configFileVersion);

                serializer.startTag(null,XN_TAG);
                serializer.attribute(null, XN_VALUE, this.master.tag);
                serializer.endTag(null,XN_TAG);
                i("TOMCAT_TAG: " + this.master.tag);

                serializer.startTag(null,XN_LOGGING_LEVEL);
                serializer.attribute(null, XN_VALUE, Integer.toString(this.master.loggingLevel));
                serializer.endTag(null,XN_LOGGING_LEVEL);
                i("LOGGING_LEVEL: " + this.master.loggingLevel);

                serializer.startTag(null,XN_DAYS_TO_KEEP);
                serializer.attribute(null, XN_VALUE, Integer.toString(this.master.daysToKeep));
                serializer.endTag(null,XN_DAYS_TO_KEEP);
                i("DAYS_TO_KEEP: " + this.master.daysToKeep);

                serializer.endTag(null,XN_CONFIG);
                serializer.endDocument();
                serializer.flush();
                i("The configuration file has been generated: " + this.master.configurationFileName);
            } catch (IOException e) {
                e(e);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private class ClearDirectoryThread extends Thread {
        // Fields
        private Logger master;
        private String directoryName;
        // Constructors
        public ClearDirectoryThread(Logger master, String directoryName) {
            super("Clear Directory");
            this.master = master;
            this.directoryName = directoryName;
        }
        // Overridden functions
        @Override
        public void run() {
            File[] files = FileUtils.getFiles(this.directoryName);
            if (files != null) {
                for (File f: files) {
                    String fName = f.getName();
                    if (fName.startsWith(LOG_FILE_PREFIX) && fName.length() > 12) {
                        String dateStr = fName.substring(4, 12);
                        dateStr = dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8) + " 00:00:00";
                        Date date = DateTimeUtil.strToDate(dateStr);
                        Date today = new Date(SystemClock.elapsedRealtime());
                        int days = DateTimeUtil.getDayInterval(date, today);
                        if (days > this.master.daysToKeep) {
                            f.delete();
                        }
                    }
                }
            }
        }
    }
}