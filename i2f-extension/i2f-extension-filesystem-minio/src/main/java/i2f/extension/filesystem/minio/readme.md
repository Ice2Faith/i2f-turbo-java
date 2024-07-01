# file system extension

## 简介

- 拓展了标准的文件系统接口规范
- 实现了FTP、SFTP、MinIO、HDFS四种当前流行的文件系统
- 提供了便捷的切换文件系统的能力

## MinIO

```java
public static void testMinioFs() {
    MinioMeta meta = new MinioMeta();
    meta.setUrl("http://x.x.x.x:9000");
    meta.setAccessKey("minioadmin");
    meta.setSecretKey("minioadmin");

    MinioFileSystem fs = new MinioFileSystem(meta);

    IFile file = fs.getFile("/home");

    if (!file.isExists()) {
        file.mkdir();
    }
    System.out.println(file);

    List<IFile> list = file.listFiles();
    list.forEach(System.out::println);

    IFile attachFiles = file.getFile("/home/alarm/files");
    if (!attachFiles.isExists()) {
        attachFiles.mkdirs();
    }
    System.out.println(attachFiles);


}
```
