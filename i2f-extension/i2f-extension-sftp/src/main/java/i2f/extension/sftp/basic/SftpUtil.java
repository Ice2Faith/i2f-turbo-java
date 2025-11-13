package i2f.extension.sftp.basic;

import com.jcraft.jsch.*;
import i2f.extension.sftp.basic.data.ISftpMeta;
import i2f.io.file.FileUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2021/11/1
 */
public class SftpUtil implements Closeable {
    private ISftpMeta meta;
    private Session session;
    private ChannelSftp channelSftp;

    public SftpUtil(ISftpMeta meta) {
        setMeta(meta);
    }

    public SftpUtil setMeta(ISftpMeta meta) {
        this.meta = meta;
        return this;
    }

    public SftpUtil login() throws JSchException {
        JSch jsch = new JSch();
        if (meta.getPrivateKey() != null) {
            jsch.addIdentity(meta.getPrivateKey());
        }

        session = jsch.getSession(meta.getUsername(), meta.getHost(), meta.getPort());

        if (meta.getPassword() != null) {
            session.setPassword(meta.getPassword());
        }

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        Properties cusConfig = meta.getConfig();
        for (Map.Entry<Object, Object> item : cusConfig.entrySet()) {
            config.put(item.getKey(), item.getValue());
        }

        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();

        channelSftp = (ChannelSftp) channel;

        return this;
    }

    public SftpUtil logout() {
        if (channelSftp != null) {
            if (channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
        return this;
    }

    public ChannelSftp getChannelSftp() {
        return channelSftp;
    }

    public ChannelShell getChannelShell() throws JSchException {
        Channel shell = session.openChannel("shell");
        shell.connect();
        return (ChannelShell) shell;
    }

    public String pwd() throws SftpException {
        return channelSftp.pwd();
    }

    public void cd(String path) throws SftpException {
        channelSftp.cd(path);
    }

    public String realpath(String path) throws SftpException {
        return channelSftp.realpath(path);
    }

    public SftpUtil mkdirs(String serverPath) throws SftpException {
        serverPath = serverPath.replaceAll("\\\\", "/");
        String[] dirs = serverPath.split("\\/");
        StringBuilder builder = new StringBuilder();
        for (String item : dirs) {
            if ("".equals(item)) {
                continue;
            }
            builder.append("/");
            builder.append(item);
            String ppath = builder.toString();
            if (!existDir(ppath)) {
                channelSftp.mkdir(ppath);
            }
        }
        return this;
    }

    public boolean existDir(String path) {
        try {
            String bak = channelSftp.pwd();
            channelSftp.cd(path);
            channelSftp.cd(bak);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public SftpUtil upload(String serverPath, String serverFileName, InputStream is) throws SftpException {
        mkdirs(serverPath);
        channelSftp.cd(serverPath);
        channelSftp.put(is, serverFileName);
        return this;
    }

    public SftpUtil upload(String serverPath, File localFile) throws SftpException, IOException {
        String fileName = localFile.getName();
        InputStream is = new FileInputStream(localFile);
        upload(serverPath, fileName, is);
        is.close();
        return this;
    }

    public InputStream download(String serverPath, String serverFileName) throws SftpException {
        if (serverPath != null && !"".equals(serverPath)) {
            channelSftp.cd(serverPath);
        }
        return channelSftp.get(serverFileName);
    }

    public OutputStream download(String serverPath, String serverFileName, OutputStream os) throws SftpException {
        if (serverPath != null && !"".equals(serverPath)) {
            channelSftp.cd(serverPath);
        }
        channelSftp.get(serverFileName, os);
        return os;
    }

    public File download(String serverPath, String serverFileName, File localFile) throws IOException, SftpException {
        FileUtil.useParentDir(localFile);
        OutputStream os = new FileOutputStream(localFile);
        download(serverPath, serverFileName, os);
        os.close();
        return localFile;
    }

    public File download2Dir(String serverPath, String serverFileName, File localDir) throws SftpException {
        FileUtil.useDir(localDir);
        File localFile = new File(localDir, serverFileName);
        try {
            OutputStream os = new FileOutputStream(localFile);
            download(serverPath, serverFileName, os);
            os.close();
        } catch (IOException e) {
            throw new SftpException(0, e.getMessage(), e);
        }
        return localFile;
    }

    public SftpUtil delete(String serverPath, String serverFileName) throws SftpException {
        if (serverPath != null && !"".equals(serverPath)) {
            channelSftp.cd(serverPath);
        }
        channelSftp.rm(serverFileName);
        return this;
    }

    public SftpUtil deleteDir(String serverPath) throws SftpException {
        channelSftp.rmdir(serverPath);
        return this;
    }

    /**
     * 递归删除目录/文件
     *
     * @param path 要删除的路径
     * @throws SftpException 如果删除失败
     */
    public void recursiveDelete(String path) throws SftpException {
        // 检查路径是否存在
        try {
            boolean isDir = false;
            Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(path);
            if (entries != null) {
                for (ChannelSftp.LsEntry entry : entries) {
                    isDir = true;
                    String filename = entry.getFilename();

                    if (".".equals(filename) || "..".equals(filename)) {
                        continue;
                    }

                    String fullPath = path.endsWith("/") ? path + filename : path + "/" + filename;

                    if (entry.getAttrs().isDir()) {
                        recursiveDelete(fullPath);
                    } else {
                        channelSftp.rm(fullPath);
                    }
                }
            }

            if (isDir) {
                channelSftp.rmdir(path);
            } else {
                channelSftp.rm(path);
            }

        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                System.out.println("path not exists: " + path);
            } else {
                throw e;
            }
        }

    }

    public Vector<ChannelSftp.LsEntry> listFiles(String serverPath) throws SftpException {
        return channelSftp.ls(serverPath);
    }

    public String exec(String command) throws IOException {
        return exec(true, -1, command, null, null);
    }

    public String exec(boolean requireOutput, long waitForMillsSeconds, String cmd, String dir, String charset) throws IOException {
        StringBuffer builder = new StringBuffer();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> exHolder = new AtomicReference<>();
        Runnable task = () -> {

            ChannelShell channel = null;
            PrintWriter printWriter = null;
            BufferedReader input = null;


            try {
                channel = getChannelShell();

                InputStreamReader inputStreamReader = new InputStreamReader(channel.getInputStream(),
                        ((charset == null || charset.isEmpty()) ? StandardCharsets.UTF_8.name() : charset));
                input = new BufferedReader(inputStreamReader);

                printWriter = new PrintWriter(channel.getOutputStream());
                if (dir != null && !dir.isEmpty()) {
                    printWriter.println("cd " + dir);
                }
                printWriter.println(cmd);
                printWriter.println("exit");
                printWriter.flush();
                if (requireOutput) {
                    String line = null;
                    while ((line = input.readLine()) != null) {
                        builder.append(line);
                        builder.append("\n");
                    }
                }

            } catch (Exception e) {
                exHolder.set(e);
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {

                    }
                }
                if (channel != null) {
                    channel.disconnect();
                }
                latch.countDown();
            }
        };
        new Thread(task).start();
        try {
            if (waitForMillsSeconds >= 0) {
                latch.await(waitForMillsSeconds, TimeUnit.MILLISECONDS);
            } else {
                latch.await();
            }
        } catch (InterruptedException e) {

        }
        Throwable ex = exHolder.get();
        if (ex != null) {
            if (ex instanceof IOException) {
                throw (IOException) ex;
            }
            throw new IOException(ex.getMessage(), ex);
        }
        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        logout();
    }
}
