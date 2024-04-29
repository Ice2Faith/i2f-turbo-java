package i2f.spi.test;


import i2f.spi.SpiComponent;

public class TestUser implements SpiComponent {
    public String username;
    public String password;

    public TestUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
