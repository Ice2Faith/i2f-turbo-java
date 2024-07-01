# file system extension

## 简介

- 拓展了标准的文件系统接口规范
- 实现了FTP、SFTP、MinIO、HDFS四种当前流行的文件系统
- 提供了便捷的切换文件系统的能力

## SFTP

```java
public static void testSftpFs() throws Exception {
    SftpMeta meta = new SftpMeta();
    meta.setHost("x.x.x.x");
    meta.setPort(22);
    meta.setUsername("root");
    meta.setPassword("xxx");

    SftpFileSystem fs = new SftpFileSystem(meta);
    System.out.println(fs);

    IFile file = fs.getFile("/root/home/test/ftp.txt");
    if (!file.getDirectory().isExists()) {
        file.getDirectory().mkdirs();
    }
    if (!file.isExists()) {
        file.writeText("hello", "UTF-8");
    }

    if (file.isExists()) {
        String str = file.readText("UTF-8");
        System.out.println("read:" + str);
        file.delete();
    }

    if (file.getDirectory().isExists()) {
        file.getDirectory().delete();
    }

    List<IFile> list = file.getDirectory().getDirectory().listFiles();
    list.forEach(System.out::println);

    fs.close();

}
```

## SFTP(proxy)

```java
public static void testProxySftpFs() throws Exception {
    ProxySftpMeta meta = new ProxySftpMeta();
    meta.setHost("x.x.x.x");
    meta.setPort(22);
    meta.setUsername("root");
    meta.setPassword("xxx");

    meta.setLocalPost(7700);
    meta.setRemoteHost("192.168.1.100");
    meta.setRemotePort(22);
    meta.setRemoteUsername("root");
    meta.setRemotePassword("xxx");

    ProxySftpFileSystem fs = new ProxySftpFileSystem(meta);
    System.out.println(fs);

    IFile file = fs.getFile("/root/home/test/ftp.txt");
    if (!file.getDirectory().isExists()) {
        file.getDirectory().mkdirs();
    }
    if (!file.isExists()) {
        file.writeText("hello", "UTF-8");
    }

    if (file.isExists()) {
        String str = file.readText("UTF-8");
        System.out.println("read:" + str);
        file.delete();
    }

    if (file.getDirectory().isExists()) {
        file.getDirectory().delete();
    }

    List<IFile> list = file.getDirectory().getDirectory().listFiles();
    list.forEach(System.out::println);

    fs.close();

}
```
