package com.stxpons.xmaster;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.util.Vector;

/**
 * <p>Filename: com.stxpons.xmaster.DownloadConnector.java<p>
 * <p>Date: 2016-03-10 18:01.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
class DownloadConnector extends SftpConnector {

    @Override
    String getChannelType() {
        return Action.download.getChannelType();
    }

    @Override
    void startExtend() throws Exception {
        if (Context.getSftpFiles().isEmpty()) {
            Util.log("Not File to download");
        }
        for (final SftpFileHolder fileHolder : Context.getSftpFiles()) {
            Util.log("Start downloading file : " + fileHolder);

            String local = Util.getString(fileHolder.getLocalFile(), this);
            String remote = Util.getString(fileHolder.getRemoteFile(), this);

            remote = remoteAbsolutePath(remote);
            downloadFiles(remote, local);

            Util.log("Finish downloading file : " + SftpFileHolder.toString(local, remote));
        }
    }

    void downloadFiles(final String remote, final String local) throws Exception {
        final ChannelSftp channelSftp = (ChannelSftp) this.channel;
        SftpATTRS attrs;
        try {
            attrs = channelSftp.lstat(remote);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                Util.error("Download error, remote file not exists : " + remote);
            }
            throw e;
        }
        try {
            downloadFiles(attrs, remote, local, true);
        } catch (SftpException e) {
            Util.error("Download error, remote file is : " + SftpFileHolder.toString(local, remote));
            throw e;
        } finally {
            try {
                channelSftp.cd(getHome());
            } catch (SftpException e) {
                Util.error("Change home error.");
                Util.error(e);
            }
        }
    }

    void downloadFiles(final SftpATTRS attrs, final String remote, final String local, final boolean mkdir4Path) throws SftpException {
        final ChannelSftp channelSftp = (ChannelSftp) this.channel;
        if (attrs.isDir()) {
            final File dir = new File(local);
            if (!dir.exists()) dir.mkdirs();
            Vector remoteFiles = channelSftp.ls(remote);
            for (Object o : remoteFiles) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) o;
                String fileName = lsEntry.getFilename();
                if (".".equals(fileName) || "..".equals(fileName)) continue;
                SftpATTRS sftpATTRS = lsEntry.getAttrs();
                String tRemote = remote;
                if (!tRemote.endsWith("/")) tRemote += "/";
                tRemote += fileName;
                String tLocal = dir.getAbsolutePath() + File.separator + fileName;
                downloadFiles(sftpATTRS, tRemote, tLocal, false);
            }
        } else if (attrs.isLink()) {
            try {
                channelSftp.cd(remote);//目录
                String realRemote = channelSftp.pwd();
                SftpATTRS sftpATTRS = channelSftp.lstat(realRemote);
                downloadFiles(sftpATTRS, realRemote, local, true);
            } catch (SftpException e) {
                if (e.id == ChannelSftp.SSH_FX_FAILURE) {//文件
                    if (remote.lastIndexOf('/') > 1) {
                        String path = remote.substring(0, remote.lastIndexOf('/'));
                        channelSftp.cd(path);
                        downloadFile(remote, local);
                    }
                } else {
                    throw e;
                }
            }
        } else {
            if (mkdir4Path) {
                final File parent = new File(local).getParentFile();
                if (!parent.exists()) parent.mkdirs();
            }
            downloadFile(remote, local);
        }
    }

    void downloadFile(final String remote, final String local) throws SftpException {
        final ChannelSftp channelSftp = (ChannelSftp) this.channel;
        channelSftp.get(remote, local);
    }
}
