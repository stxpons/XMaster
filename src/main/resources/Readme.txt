################################### XMaster ####################################
/** Author  : stxpons@gmail.com **/
/** Date    : 2016-03-10        **/
/** Version : 1.0               **/

usage: XMaster [-a <action>] [-f <file>] [-h] [-H <host>] [-k] [-lf <localFile>]
       [-p <password>] [-P <port>] [-rf <remoteFile>] [-s <serversFile>] [-sf
       <shellsFile>] [-u <user>]
 -------------------------------------------------------------------------------
 -a,--action <action>            The action to be execute: shell, sftp, upload,
                                 download
 -f,--files <file>               The upload or download files, line
                                 format:local_or_remote_file |
                                 local_or_remote_file.upload using default
                                 uploadfiles.txt and files.txt; download using
                                 default downloadfiles.txt and files.txt; other
                                 using default files.txt.
 -h,--help                       Help
 -H,--host <host>                Host
 -k,--keepalive                  Keep connection alive
 -lf,--localfile <localFile>     Local file or directory
 -p,--password <password>        Password
 -P,--port <port>                Port
 -rf,--remotefile <remoteFile>   Remote file or directory
 -s,--servers <serversFile>      The servers list file, one line one server:
                                 user password host [port], using default file
                                 servers.txt
 -sf,--shellfile <shellsFile>    The shell file to be executed.
 -u,--user <user>                User name
 -------------------------------------------------------------------------------
 console command:
 __help                          print help
 __quit                          exit
 __exit                          exit
 __ctrl[a-z|A-Z]                 send CTRL+[A-Z]
################################### XMaster ####################################
