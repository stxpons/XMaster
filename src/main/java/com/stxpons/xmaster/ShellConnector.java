package com.stxpons.xmaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * <p>Filename: com.stxpons.xmaster.ShellConnector.java<p>
 * <p>Date: 2016-03-10 14:56.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
class ShellConnector extends Connector {

    @Override
    String getChannelType() {
        return Action.shell.getChannelType();
    }

    @Override
    void startExtend() throws Exception {
        final Connector connector = this;
        if (null != Context.getShellFile()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(new File(Context.getShellFile())));
                String line;
                while (null != (line = reader.readLine())) {
                    connector.execute(line);
                }
            } finally {
                if (null != reader) reader.close();
            }
        }
        if (Context.isKeepAlive()) connector.listen();
    }
}
