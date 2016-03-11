package com.stxpons.xmaster;

/**
 * <p>Filename: com.stxpons.xmaster.Opt.java<p>
 * <p>Date: 2016-03-11 15:49.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
enum Opt {
    HELP(Constants.O_HELP, "Help", Constants.OL_HELP),
    KEEP_ALIVE(Constants.O_KEEP_ALIVE, "Keep connection alive", Constants.OL_KEEP_ALIVE),
    ACTION(Constants.O_ACTION, "The action to be execute: " + Util.AVAILABLE_ACTION_NAME, true, "action", Constants.OL_ACTION, false),
    USER(Constants.O_USER, "User name", true, "user", Constants.OL_USER),
    PASSWORD(Constants.O_PASSWORD, "Password", true, "password", Constants.OL_PASSWORD),
    HOST(Constants.O_HOST, "Host", true, "host", Constants.OL_HOST),
    PORT(Constants.O_PORT, "Port", true, "port", Constants.OL_PORT),
    SHELL_FILE(Constants.O_SHELL_FILE, "The shell file to be executed.", true, "shellsFile", Constants.OL_SHELL_FILE),
    SERVERS(Constants.O_SERVERS, "The servers list file, one line one server: user password host [port], using default file " + Constants.DEFAULT_SERVERS_FILE, true, "serversFile", Constants.OL_SERVERS),
    LOCAL_FILE(Constants.O_LOCAL_FILE, "Local file or directory", true, "localFile", Constants.OL_LOCAL_FILE),
    REMOTE_FILE(Constants.O_REMOTE_FILE, "Remote file or directory", true, "remoteFile", Constants.OL_REMOTE_FILE),
    UPLOAD_DOWNLOAD_FILES(Constants.O_UPLOAD_DOWNLOAD_FILES, "The upload or download files, line format:local_or_remote_file | local_or_remote_file.upload using default " + Constants.DEFAULT_UPLOAD_FILES + " and " + Constants.DEFAULT_FILES + "; download using default " + Constants.DEFAULT_DOWNLOAD_FILES + " and " + Constants.DEFAULT_FILES + "; other using default " + Constants.DEFAULT_FILES + ".", true, "file", Constants.OL_UPLOAD_DOWNLOAD_FILES);

    protected String opt;
    protected boolean hasArg;
    protected String description;
    protected boolean required;
    protected String longOpt;
    protected String argName;

//    Opt(String opt, String description) {
//        this(opt, description, false, null);
//    }

//    Opt(String opt, String description, boolean hasArg, String argName) {
//        this(opt, description, hasArg, argName, null);
//    }

    Opt(String opt, String description, String longOpt) {
        this(opt, description, false, null, longOpt);
    }

    Opt(String opt, String description, boolean hasArg, String argName, String longOpt) {
        this(opt, description, hasArg, argName, longOpt, false);
    }

    Opt(String opt, String description, boolean hasArg, String argName, String longOpt, boolean required) {
        this.opt = opt;
        this.hasArg = hasArg;
        this.description = description;
        this.argName = argName;
        this.longOpt = longOpt;
        this.required = required;
    }
}
