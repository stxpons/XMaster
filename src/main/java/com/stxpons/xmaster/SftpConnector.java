package com.stxpons.xmaster;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

/**
 * <p>Filename: com.stxpons.xmaster.SftpConnector.java<p>
 * <p>Date: 2016-03-10 17:59.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
class SftpConnector extends Connector {

    private String home = null;

    @Override
    String getChannelType() {
        return Action.sftp.getChannelType();
    }

    @Override
    void startExtend() throws Exception {

    }

    protected String remoteAbsolutePath(String path) throws Exception {
        return remoteAbsolutePath(getHome(), path);
    }

    protected String remoteAbsolutePath(final String workPath, final String path) throws Exception {
        if (path.charAt(0) == '/') return path;
        if (workPath.endsWith("/")) return workPath + path;
        return workPath + "/" + path;
    }

    protected String getHome() throws Exception {
        if (null == home) {
            final ChannelSftp channelSftp = (ChannelSftp) this.channel;
            home = channelSftp.getHome();
        }
        return home;
    }

    protected void checkAndMkdir(String dir) throws Exception {
        if (null != dir && !dir.equals(getHome())) {
            final ChannelSftp channelSftp = (ChannelSftp) this.channel;
            try {
                channelSftp.ls(dir);
            } catch (SftpException e) {
                if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                    try {
                        channelSftp.mkdir(dir);
                    } catch (SftpException se) {
                        if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                            checkAndMkdir(getRemoteParent(dir));
                            checkAndMkdir(dir);
                        } else {
                            throw e;
                        }
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    protected String getRemoteParent(final String remote) {
        if (remote.length() > 1 && remote.indexOf('/') >= 0) {
            return remote.substring(0, remote.lastIndexOf('/'));
        }
        return null;
    }
}
