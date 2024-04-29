package i2f.spi.test;

import i2f.spi.SpiComponentFactory;

public class TestSpiComponentFactory implements SpiComponentFactory<TestUser> {

    @Override
    public Class<?> type() {
        return TestUser.class;
    }

    @Override
    public TestUser build() {
        return new TestUser("admin", "123456");
    }
}
