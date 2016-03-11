package com.stxpons.xmaster;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.util.*;

/**
 * <p>Filename: com.stxpons.xmaster.Context.java<p>
 * <p>Date: 2016-03-10 10:50.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
final class Context {

    private final static Collection<Server> servers = new HashSet<>();
    private static Action action;
    private static CommandLine cmd;
    private static Options options;
    private static boolean keepAlive;
    //private static String localFile;
    //private static String remoteFile;
    private static String shellFile;
    private static String file;
    private final static Collection<SftpFileHolder> sftpFiles = new HashSet<>();

    private static final Object LOCK = new Lock();

    static class Lock {
    }

    static Collection<Server> getServers() {
        return servers;
    }

    static Action getAction() {
        return action;
    }

    static void setAction(Action action) {
        Context.action = action;
    }

    static CommandLine getCmd() {
        return cmd;
    }

    static void setCmd(CommandLine cmd) {
        Context.cmd = cmd;
    }

    static Options getOptions() {
        return options;
    }

    static void setOptions(Options options) {
        Context.options = options;
    }

    static Object getLock() {
        return LOCK;
    }

    static void setKeepAlive(boolean keepAlive) {
        Context.keepAlive = keepAlive;
    }

    static boolean isKeepAlive() {
        return keepAlive;
    }

    static String getShellFile() {
        return shellFile;
    }

    static void setShellFile(String shellFile) {
        Context.shellFile = shellFile;
    }

    static String getFile() {
        return file;
    }

    static void setFile(String file) {
        Context.file = file;
    }

    static Collection<SftpFileHolder> getSftpFiles() {
        return sftpFiles;
    }
}
