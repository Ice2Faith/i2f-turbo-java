# file system extension

## 简介

- 拓展了标准的文件系统接口规范
- 实现了FTP、SFTP、MinIO、HDFS四种当前流行的文件系统
- 提供了便捷的切换文件系统的能力

## HDFS

```java
public static void testHdfs() throws Exception {
    HdfsMeta meta = new HdfsMeta();
    meta.setDefaultFs("hdfs://x.x.x.x:9000");
    meta.setUser("root");
    meta.setUri("hdfs://x.x.x.x:9000");

    HdfsFileSystem fs = new HdfsFileSystem(meta);
    System.out.println(fs);


    IFile file = fs.getFile("/home/test/hdfs.txt");
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

    List<IFile> list = file.getFile("/").listFiles();
    list.forEach(System.out::println);

    fs.close();
}
```
