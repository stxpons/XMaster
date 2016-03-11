package com.stxpons.xmaster;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>Filename: com.stxpons.xmaster.IConnector.java<p>
 * <p>Date: 2016-03-10 14:51.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
public interface IConnector extends AutoCloseable {

    void accept(final Server server) throws Exception;

    void setInputStream(InputStream in) throws Exception;

    void setOutputStream(OutputStream out) throws Exception;

    void open() throws Exception;

    void listen() throws Exception;

    void execute(final String command) throws Exception;
}
