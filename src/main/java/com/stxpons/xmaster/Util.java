package com.stxpons.xmaster;

import org.apache.commons.cli.*;

import java.io.*;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.Arrays;

/**
 * <p>Filename: com.stxpons.xmaster.Util.java<p>
 * <p>Date: 2016-03-10 01:15.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
public class Util {

    static final String AVAILABLE_ACTION_NAME;

    static {
        StringBuilder sb = new StringBuilder();
        int len = Action.values().length;
        for (int i = 0; ; i++) {
            Action a = Action.values()[i];
            sb.append(a.name());
            if (i == len - 1) break;
            sb.append(", ");
        }
        AVAILABLE_ACTION_NAME = sb.toString();
    }

    static void parserArgs(final String[] args) throws Exception {
        initOptions();
        parserOptions(args);
        checkArgs();
        parserServers();
        parserOther();
        keepAlive();
    }

    private static void initOptions() {
        //路径有空格 FIXME
        final Options options = new Options();
        for (Opt opt : Opt.values()) {
            Option option = new Option(opt.opt, opt.longOpt, opt.hasArg, opt.description);
            option.setArgName(opt.argName);
            option.setRequired(opt.required);
            options.addOption(option);
        }
        Context.setOptions(options);
    }

    private static void parserOptions(final String[] args) throws Exception {
        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(Context.getOptions(), args);
        Context.setCmd(cmd);
    }

    private static void checkArgs() {
        final CommandLine cmd = Context.getCmd();
        if (cmd.hasOption(Constants.O_HELP)) {
            printHelp();
            System.exit(0);
        }
        Action action = null;
        if (cmd.hasOption(Constants.O_ACTION)) {
            try {
                action = Action.valueOf(cmd.getOptionValue(Constants.O_ACTION));
            } catch (Throwable e) {
                Util.error("Please input the right action: " + AVAILABLE_ACTION_NAME);
                System.exit(0);
            }
        } else {
            Util.error("Please input action");
            System.exit(0);
        }
        Context.setAction(action);

        if (!(cmd.hasOption(Constants.O_LOCAL_FILE) && !cmd.hasOption(Constants.O_REMOTE_FILE))
                && !(!cmd.hasOption(Constants.O_LOCAL_FILE) || cmd.hasOption(Constants.O_REMOTE_FILE))) {
            Util.error("Please input both localfile and remotefile");
            System.exit(0);
        }

        checkFileExist();
    }

    private static void checkFileExist() {
        checkFileExist(Context.getCmd().getOptionValue(Constants.O_SHELL_FILE), true, true);
        //checkFileExist(Context.getCmd().getOptionValue(Constants.O_LOCAL_FILE), true, true);
        //checkFileExist(Context.getCmd().getOptionValue(Constants.O_REMOTE_FILE), true, true);
        checkFileExist(Context.getCmd().getOptionValue(Constants.O_UPLOAD_DOWNLOAD_FILES), true, true);
    }

    static boolean checkFileExist(String fileName) {
        return checkFileExist(fileName, false, false);
    }

    private static boolean checkFileExist(String fileName, boolean haveVariable, boolean exit) {
        if (null != fileName) {
            if (haveVariable && fileName.indexOf('$') > 0) {
                fileName = fileName.substring(0, fileName.indexOf('$'));
                if (fileName.indexOf(File.separator) > 0) {
                    fileName = fileName.substring(0, fileName.lastIndexOf(File.separator));
                }
            }
            File file = new File(fileName);
            if (!file.exists()) {
                Util.error("File not exist: " + file);
                if (exit) {
                    System.exit(0);
                }
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    static void printHelp() {
        PrintWriter pw = new PrintWriter(System.out);
        HelpFormatter help = new HelpFormatter();

        int width = 128;
        int leftPadding = 1;
        int descPadding = 3;

        char[] headerFooterPadding = new char[width];
        Arrays.fill(headerFooterPadding, '-');
        Arrays.fill(headerFooterPadding, 0, leftPadding, ' ');
        final String headerFooter = new String(headerFooterPadding);

        final String title = Constants.XMaster;
        char[] titlePadding = new char[width];
        Arrays.fill(titlePadding, '#');
        int index = (width - title.length() - 2) / 2;
        titlePadding[index++] = ' ';
        for (int j = 0; j < title.length(); index++, j++) {
            titlePadding[index] = title.charAt(j);
        }
        titlePadding[index/*++*/] = ' ';
        //Arrays.fill(titlePadding,index,width,'<');

        final String titleLine = new String(titlePadding);

        pw.println(titleLine);

        pw.println("/** Author  : stxpons@gmail.com **/");
        pw.println("/** Date    : 2016-03-10        **/");
        pw.println("/** Version : 1.0               **/");
        pw.println();

        help.printHelp(pw, width, Constants.XMaster, headerFooter, Context.getOptions(), leftPadding, descPadding, headerFooter, true);

        pw.println(" console command:");
        pw.println(" __help                          print help");
        pw.println(" __quit                          exit");
        pw.println(" __exit                          exit");
        pw.println(" __ctrl[a-z|A-Z]                 send CTRL+[A-Z]");

        pw.println(titleLine);

        pw.flush();
    }

    private static void parserServers() throws Exception {
        final CommandLine cmd = Context.getCmd();
        if (cmd.hasOption(Constants.O_SERVERS)) {
            String serversFile = cmd.getOptionValue(Constants.O_SERVERS);
            checkFileExist(serversFile, true, true);
            parserServersFromFile(serversFile);
        } else if (cmd.hasOption(Constants.O_HOST)) {
            final String host = cmd.getOptionValue(Constants.O_HOST);
            final Server server = new Server(host);
            if (cmd.hasOption(Constants.O_USER)) {
                String user = cmd.getOptionValue(Constants.O_USER);
                server.setUser(user);
            }
            if (cmd.hasOption(Constants.O_PASSWORD)) {
                String password = cmd.getOptionValue(Constants.O_PASSWORD);
                server.setPassword(password);
            }
            if (cmd.hasOption(Constants.O_PORT)) {
                String tP = cmd.getOptionValue(Constants.O_PORT);
                int port = Integer.valueOf(tP);
                server.setPort(port);
            }
            Context.getServers().add(server);
        } else {
            //获取默认文件
            Util.warn("Get default servers file");
            boolean haveServers = false;
            File file = new File(Constants.DEFAULT_SERVERS_FILE);
            if (file.exists()) {
                String serversFile = file.getAbsolutePath();
                Util.warn("Using default servers file : " + serversFile);
                parserServersFromFile(serversFile);
                haveServers = true;
            } else {
                Util.error("Can't find default servers file : " + file.getAbsolutePath());
            }
            if (!haveServers) {
                Util.error("Please input servers by " + Constants.O_SERVERS + " or " + Constants.O_HOST);
                System.exit(0);
            }
        }
    }

    private static void parserServersFromFile(final String serversFile) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(serversFile)));
            String line;
            while (null != (line = reader.readLine())) {
                if (line.length() == 0) continue;
                if (line.startsWith("#")) continue;
                if (line.indexOf(' ') < 0) continue;
                String[] hs = line.split(" ");
                if (hs.length < 3) continue;
                String user = hs[0];
                String password = hs[1];
                String host = hs[2];
                final Server server = new Server(user, password, host);
                if (hs.length >= 4) {
                    String sP = hs[3];
                    int port = Integer.valueOf(sP);
                    server.setPort(port);
                }
                Context.getServers().add(server);
            }
        } finally {
            if (null != reader) reader.close();
        }
    }

    private static void parserOther() throws Exception {
        final CommandLine cmd = Context.getCmd();
        if (cmd.hasOption(Constants.O_SHELL_FILE)) {
            Context.setShellFile(parserLocalFile(cmd.getOptionValue(Constants.O_SHELL_FILE)));
        }
        if (cmd.hasOption(Constants.O_UPLOAD_DOWNLOAD_FILES)) {
            Context.setFile(parserLocalFile(cmd.getOptionValue(Constants.O_UPLOAD_DOWNLOAD_FILES)));
        }
        if (cmd.hasOption(Constants.O_LOCAL_FILE) && cmd.hasOption(Constants.O_REMOTE_FILE)) {
            String local = parserLocalFile(cmd.getOptionValue(Constants.O_LOCAL_FILE));
            String remote = cmd.getOptionValue(Constants.O_REMOTE_FILE);
            Context.getSftpFiles().add(new SftpFileHolder(local, remote));
        } else if (Context.getAction().getChannelType().equals("sftp")) {
            String file = parserLocalFile(cmd.getOptionValue(Constants.O_UPLOAD_DOWNLOAD_FILES));
            if (null == file) {
                Util.warn("Get default sftp files list");
                File f = new File(Constants.DEFAULT_FILES);
                switch (Context.getAction()) {
                    case download:
                        File t = new File(Constants.DEFAULT_DOWNLOAD_FILES);
                        if (t.exists()) f = t;
                        else Util.error("Can't find default sftp files : " + t.getAbsolutePath());
                        break;
                    case upload:
                        t = new File(Constants.DEFAULT_UPLOAD_FILES);
                        if (t.exists()) f = t;
                        else Util.error("Can't find default sftp files : " + t.getAbsolutePath());
                        break;
                    case sftp:
                        break;
                    default:
                        Util.error("Nonsupport sftp action type");
                }
                if (f.exists()) {
                    file = f.getAbsolutePath();
                    Util.warn("Using default sftp files : " + f.getAbsolutePath());
                } else {
                    Util.error("Can't find default sftp files : " + f.getAbsolutePath());
                }
            }
            if (null != file) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(new File(file)));
                    String line;
                    int li = Context.getAction() == Action.download ? 1 : 0;
                    int ri = Math.abs(li - 1);
                    while (null != (line = reader.readLine())) {
                        if (line.length() == 0) continue;
                        if (line.startsWith("#")) continue;
                        if (line.indexOf('|') < 0) continue;
                        String[] hs = line.split("\\|");
                        if (hs.length < 2) continue;
                        String local = hs[li].trim();
                        String remote = hs[ri].trim();
                        Context.getSftpFiles().add(new SftpFileHolder(local, remote));
                    }
                } finally {
                    if (null != reader) reader.close();
                }
            }
        }
    }

    /**
     * 解析本地文件路径.如文件无前缀设置为当前路径.路径可能包含变量
     */
    private static String parserLocalFile(String fileName) {
        return fileName;
    }

    private static void keepAlive() throws Exception {
        final CommandLine cmd = Context.getCmd();
        if (cmd.hasOption(Constants.O_KEEP_ALIVE)) {
            Context.setKeepAlive(true);
        }
    }

    static String getString(final String string, final Connector connector) {
        if (null != string && string.indexOf('$') >= 0) {
            final Server server = connector.getServer();
            String rtn = string;
            rtn = rtn.replaceAll("\\$HOST", server.getHost());
            rtn = rtn.replaceAll("\\$USER", server.getUser());
            rtn = rtn.replaceAll("\\$PASSWORD", server.getUser());
            return rtn;
        }
        return string;
    }

    private static URL getResourceURL(String resourceFileName) {
        ClassLoader classLoader = getClassClassLoader();
        URL url;
        if (classLoader == null)
            url = ClassLoader.getSystemResource(resourceFileName);
        else
            url = classLoader.getResource(resourceFileName);
        return url;
    }

    private static ClassLoader getClassClassLoader() {
        if (System.getSecurityManager() == null) {
            return Util.class.getClassLoader();
        } else {
            return (ClassLoader) java.security.AccessController.doPrivileged(new PrivilegedAction() {
                public java.lang.Object run() {
                    return Util.class.getClassLoader();
                }
            });
        }
    }

    static void log(final String msg) {
        System.out.println(Thread.currentThread().getName() + " - " + msg);
        System.out.flush();
    }

    static void error(final String msg) {
        System.err.println(Thread.currentThread().getName() + " - [ERROR] " + msg);
        System.err.flush();
    }

    static void error(final Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        e.printStackTrace(new PrintStream(baos));
        String err = baos.toString();
        Util.error(err);
    }

    static void warn(final String msg) {
        System.err.println(Thread.currentThread().getName() + " - [WARN] " + msg);
        System.err.flush();
    }
}