package com.stxpons.xmaster;

import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

/**
 * //TODO 切换channel模式,命令行上传下载sftp文件
 * <p>Filename: com.stxpons.xmaster.XMaster.java<p>
 * <p>Date: 2016-03-10 11:23.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
public final class XMaster {

    private static final Collection<IConnector> connectors = new HashSet<>();

    public static void main(String[] args) throws Exception {
        Thread.currentThread().setName(Constants.XMaster);
        Util.parserArgs(args);

        for (final Server server : Context.getServers()) {
            IConnector connector = Context.getAction().getClazz().newInstance();
            connector.accept(server);
            connectors.add(connector);
            if (connector instanceof Connector) {
                final Thread t = new Thread((Connector) connector);
                t.setName(server.toString());
                t.setDaemon(true);
                t.start();
                if (!Context.isKeepAlive()) {
                    t.join();
                }
            }
        }

        keepAlive();
    }

    private static void keepAlive() {
        if (Context.isKeepAlive()) {
            final Scanner sc = new Scanner(System.in);
            while (true) {
                try {
                    String line = sc.nextLine();
                    if (null != line) {
                        if (line.startsWith("__")) {
                            if ("__exit".equals(line) || "__quit".equals(line)) {
                                System.exit(0);
                                continue;
                            } else if ("__help".equals(line)) {
                                Util.printHelp();
                                continue;
                            } else if (line.startsWith("__ctrl+") && line.length() == ("__ctrl+".length() + 1)) {
                                char c = line.charAt("__ctrl+".length());
                                if (c >= 'a' && c <= 'z') {
                                    c = (char) (c - 32);
                                }
                                if (c >= 'A' && c <= 'Z') {
                                    for (final IConnector connector : connectors) {
                                        if (connector instanceof Connector) {
                                            ((Connector) connector).sendCommand(c & 0x1f);
                                        }
                                    }
                                    continue;
                                }
                            }
                        }
                        for (final IConnector connector : connectors) {
                            connector.execute(line);
                        }
                    }
                    Thread.sleep(50);
                } catch (Throwable ignored) {
                }
            }
        }
    }
}
