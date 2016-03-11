package com.stxpons.xmaster;

import java.io.Serializable;

/**
 * <p>Filename: com.stxpons.xmaster.Server.java<p>
 * <p>Date: 2016-03-10 10:39.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
class Server implements Serializable {

    private static final long serialVersionUID = -3232794790003022334L;

    private String user;
    private String password;
    private String host = "127.0.0.1";
    private int port = 22;

    public Server(final String host) {
        this(System.getProperty("user.name"), null, host);
    }

    public Server(final String user, final String password, final String host) {
        this(user, password, host, 22);
    }

    public Server(final String user, final String password, final String host, final int port) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    void setUser(String user) {
        this.user = user;
    }

    void setPassword(String password) {
        this.password = password;
    }

    void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return user + "@" + host + "[" + port + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;

        if (port != server.port) return false;
        if (user != null ? !user.equals(server.user) : server.user != null) return false;
        return host != null ? host.equals(server.host) : server.host == null;

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }
}
