package com.stxpons.xmaster;

/**
 * <p>Filename: com.stxpons.xmaster.SftpFileHolder.java<p>
 * <p>Date: 2016-03-11 21:30.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
public class SftpFileHolder {

    private String localFile;
    private String remoteFile;

    SftpFileHolder(String localFile, String remoteFile) {
        this.localFile = localFile;
        this.remoteFile = remoteFile;
    }

    String getLocalFile() {
        return localFile;
    }

    void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    String getRemoteFile() {
        return remoteFile;
    }

    void setRemoteFile(String remoteFile) {
        this.remoteFile = remoteFile;
    }

    @Override
    public String toString() {
        return toString(this.localFile, this.remoteFile);
    }

    public static String toString(final String localFile, final String remoteFile) {
        return "{localFile='" + localFile + "\'" + ", remoteFile='" + remoteFile + "\'}";
    }
}
