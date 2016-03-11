package com.stxpons.xmaster;

/**
 * <p>Filename: com.stxpons.xmaster.Action.java<p>
 * <p>Date: 2016-03-10 10:36.</p>
 *
 * @author <a href="mailto:stxpons@gmail.com">stxpons@gmail.com</a>
 * @version V1.0.0
 */
enum Action {

    shell("shell", ShellConnector.class),
    sftp("sftp", SftpConnector.class),
    upload("sftp", UploadConnector.class),
    download("sftp", DownloadConnector.class);

    private String channelType;
    private Class<? extends IConnector> clazz;

    Action(final String channelType, final Class<? extends IConnector> clazz) {
        this.channelType = channelType;
        this.clazz = clazz;
    }

    public String getChannelType() {
        return channelType;
    }

    public Class<? extends IConnector> getClazz() {
        return clazz;
    }
}
