package com.ekek.commonmodule.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    // Constants
    private final int FILESIZE = 40 * 1024;

    // Singleton
    public static FileUtils instance;
    private FileUtils() {
    }
    public static FileUtils getInstance() {

        if (instance == null) {

            instance = new FileUtils();
        }

        return instance;
    }

    // Public functions
    public static String extractFileExt(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
    public static String extractFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    public static String getNewFileName(String folderName, String prefix, String ext) {
        folderName = folderName.replaceAll("\\\\", "/");
        if (!"/".equals(folderName.subSequence(folderName.length() - 1, folderName.length()))) {
            folderName += "/";
        }
        File file = new File(folderName);
        if (!file.exists()) {
            return folderName;
        }
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = prefix + sdf.format(now);
        String fullName = folderName + fileName + ext;
        while (true) {
            file = new File(fullName);
            if (!file.exists()) {
                break;
            }
            fileName = fileName + "_1";
            fullName = folderName + fileName + ext;
        }
        return fullName;
    }
    public static String extractDirectoryName(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int splash = fileName.lastIndexOf('/');
            if (splash > -1 && splash < fileName.length()) {
                return fileName.substring(0, splash);
            }
        }
        return fileName;
    }
    public File createSDFile(String fileName) throws IOException {
        File file = new File( fileName);
        file.createNewFile();
        return file;
    }
    public File createSDDir(String dirName) {
        File dir =  new File( dirName);
        dir.mkdir();
        return dir;
    }
    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
    public static File[] getFiles(String aDirName) {
        File file = new File(aDirName);
        if (file.exists() && file.isDirectory()) {
            return file.listFiles();
        }
        return null;
    }
    public boolean deleteDirectory(String path) {
        boolean flag = false;
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        return dirFile.delete();
    }
    public boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(filePath);
            } else {
                return deleteDirectory(filePath);
            }
        }
    }
    public void appendText(String fileName, String line) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return;
                }
            }
            FileWriter writer = new FileWriter(file, true);
            if (!line.endsWith("\r\n")) {
                line += "\r\n";
            }
            writer.write(line);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
