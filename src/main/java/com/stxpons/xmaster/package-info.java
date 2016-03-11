/**
 * @author baixl5@asiainfo.com
 * @version V1.0.0
 * @date 2016-03-10 10:46.
 */
package com.stxpons.xmaster;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

class Constants {
    public final static String O_HELP = "h";
    public final static String OL_HELP = "help";

    public final static String O_ACTION = "a";
    public final static String OL_ACTION = "action";

    public final static String O_USER = "u";
    public final static String OL_USER = "user";

    public final static String O_PASSWORD = "p";
    public final static String OL_PASSWORD = "password";

    public final static String O_HOST = "H";
    public final static String OL_HOST = "host";

    public final static String O_PORT = "P";
    public final static String OL_PORT = "port";

    public final static String O_SERVERS = "s";
    public final static String OL_SERVERS = "servers";

    public final static String O_SHELL_FILE = "sf";
    public final static String OL_SHELL_FILE = "shellfile";

    public final static String O_LOCAL_FILE = "lf";
    public final static String OL_LOCAL_FILE = "localfile";

    public final static String O_REMOTE_FILE = "rf";
    public final static String OL_REMOTE_FILE = "remotefile";

    public final static String O_UPLOAD_DOWNLOAD_FILES = "f";
    public final static String OL_UPLOAD_DOWNLOAD_FILES = "files";

    public final static String O_KEEP_ALIVE = "k";
    public final static String OL_KEEP_ALIVE = "keepalive";


    public static final String XMaster = "XMaster";
    public static final String TODAY = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    public static final String TODAY_WORK_PATH = System.getProperty("user.home") + File.separator + ".XMaster" + File.separator + TODAY;

    public static final String DEFAULT_SERVERS_FILE = "servers.txt";
    public static final String DEFAULT_FILES = "files.txt";
    public static final String DEFAULT_UPLOAD_FILES = "uploadfiles.txt";
    public static final String DEFAULT_DOWNLOAD_FILES = "downloadfiles.txt";
}