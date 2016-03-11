package com.stxpons.xmaster;

import com.jcraft.jsch.ChannelSftp;

import java.io.File;

/**
 * <p>Filename: com.stxpons.xmaster.UploadConnector.java<p>
 * <p>Date: 2016-03-10 18:02.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
class UploadConnector extends SftpConnector {

    @Override
    String getChannelType() {
        return Action.upload.getChannelType();
    }

    @Override
    void startExtend() throws Exception {
        if (Context.getSftpFiles().isEmpty()) {
            Util.log("Not File to upload");
        }
        for (final SftpFileHolder fileHolder : Context.getSftpFiles()) {
            Util.log("Start uploading file : " + fileHolder);

            String local = Util.getString(fileHolder.getLocalFile(), this);
            String remote = Util.getString(fileHolder.getRemoteFile(), this);
            if (!Util.checkFileExist(local)) {
                Util.error("Local file not exist and it will not be uploaded : " + local);
            } else {
                remote = remoteAbsolutePath(remote);
                uploadFiles(new File(local), remote);
            }

            Util.log("Finish uploading file : " + SftpFileHolder.toString(local, remote));
        }
    }

    void uploadFiles(final File local, final String remote) throws Exception {
        uploadFiles(local, remote, true);
    }

    void uploadFiles(final File local, final String remote, final boolean mkdir4Path) throws Exception {
        if (local.isDirectory()) {
            checkAndMkdir(remote);
            File[] files = local.listFiles();
            if (null != files) {
                for (final File file : files) {
                    uploadFiles(file, remote + "/" + file.getName(), false);
                }
            }
        } else {
            if (mkdir4Path) {
                checkAndMkdir(getRemoteParent(remote));
            }
            uploadFile(local, remote);
        }
    }

    void uploadFile(final File local, final String remote) throws Exception {
        final ChannelSftp channelSftp = (ChannelSftp) this.channel;
        channelSftp.put(local.getAbsolutePath(), remote);
    }
}
