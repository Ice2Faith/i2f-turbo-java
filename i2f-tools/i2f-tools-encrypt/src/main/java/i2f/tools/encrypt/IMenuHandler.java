package i2f.tools.encrypt;

public interface IMenuHandler {
    String name();

    void execute(String[] args) throws Exception;
}
