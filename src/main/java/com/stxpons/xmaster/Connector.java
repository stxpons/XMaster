package com.stxpons.xmaster;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * <p>Filename: com.stxpons.xmaster.Connector.java<p>
 * <p>Date: 2016-03-10 10:37.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
abstract class Connector implements IConnector, Runnable {
    protected Server server;
    protected Session session;
    protected Channel channel;
    protected InputStream in;
    protected OutputStream writer;
    protected OutputStream out;
    protected final static int CONNECTION_TIMEOUT = 30 * 1000;
    protected final static byte[] LINE_SEPARATOR_BYTES = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator")).getBytes();
    protected short connectionStatus = 0;//0 初始化,1正在连接,2已连接,3监听,9关闭
    protected final Queue<String> queue = new LinkedList<>();

    Connector() {
    }

    Connector(final Server server) {
        this.server = server;
    }

    Server getServer() {
        return server;
    }

    abstract String getChannelType();

    public void setOutputStream(OutputStream out) {
        this.out = out;
    }

    public void setInputStream(InputStream in) throws IOException {
        this.in = in;
        if (null != in && in instanceof PipedInputStream) {
            writer = new PipedOutputStream((PipedInputStream) in);
        }
    }

    @Override
    public void accept(final Server server) {
        this.server = server;
    }

    public void open() throws Exception {
        connectionStatus = 1;
        Util.log("Starting connection.");
        openSession();
        openChannel();
        connectionStatus = 2;
        Util.log("Finish connection.");
    }

    void openSession() throws Exception {
        JSch jSch = new JSch();

        session = jSch.getSession(getServer().getUser(), getServer().getHost(), getServer().getPort());
        session.setPassword(getServer().getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.setServerAliveInterval(60000);

        session.connect(CONNECTION_TIMEOUT);
    }

    void openChannel() throws Exception {
        channel = session.openChannel(getChannelType());
        if (null != in) {
            channel.setInputStream(in);
        } else {
            in = channel.getInputStream();
        }
        if (null != out) {
            channel.setOutputStream(out);
        } else {
            out = channel.getOutputStream();
        }
        channel.connect(CONNECTION_TIMEOUT);
    }

    @Override
    public void close() throws Exception {
        closeChannel();
        closeSession();
    }

    void closeChannel() throws Exception {
        if (null != writer) {
            writer.close();
        }
        if (null != in) {
            in.close();
        }
        if (null != out) {
            out.flush();
            out.close();
        }
        if (null != channel && channel.isConnected()) {
            channel.disconnect();
        }
    }

    void closeSession() {
        if (null != session && session.isConnected()) {
            session.disconnect();
        }
    }

    void sendCommand(final int b) throws Exception {
        if (null != writer) {
            writer.write(b);
            writer.flush();
        }
    }

    void sendCommand(final byte[] bytes) throws Exception {
        if (null != writer) {
            writer.write(bytes);
            writer.write(LINE_SEPARATOR_BYTES);
            writer.flush();
        }
    }

    void sendCommand(final String command) throws Exception {
        sendCommand(command.getBytes());
    }

    public void execute(final String command) throws Exception {
        if (isListening()) {
            queue.add(command);
        } else {
            if (connectionStatus == 9) {
                Util.log("Connection close.");
            }
            sendCommand(command);
        }
    }

    boolean isListening() {
        return connectionStatus == 3;
    }

    public void listen() throws Exception {
        connectionStatus = 3;
        while (true) {
            if (!queue.isEmpty()) {
                while (connectionStatus < 2) ;
                String command = queue.poll();
                sendCommand(command);
            }
            try {
                Thread.sleep(50);
            } catch (Throwable ignored) {
            }
        }
    }

    protected void start() throws Exception {
        final Connector connector = this;
        if (null == in) {
            PipedInputStream in = new PipedInputStream(8 * 1024);
            connector.setInputStream(in);
        }
        if (null == out) {
            String outFile = Constants.TODAY_WORK_PATH + File.separator + server + ".log";
            final File f = new File(outFile);
            if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
            if (!f.exists()) f.createNewFile();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(f, true));
            connector.setOutputStream(out);
        }
        connector.open();
    }

    abstract void startExtend() throws Exception;

    @Override
    public void run() {
        try {
            this.start();
            startExtend();
        } catch (Exception e) {
            Util.error(e);
        }
        Util.log("Finish job.");
    }
}
