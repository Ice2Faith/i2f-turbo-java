package i2f.spi.test;


import i2f.spi.SpiComponent;
import i2f.spi.SpiComponentFactory;
import i2f.spi.Spis;

import java.io.IOException;
import java.util.List;

public class TestSpi {
    public static void main(String[] args) throws IOException {
        Spis.makeSpiFile(SpiComponent.class, TestSpiComponent.class, TestUser.class);
        Spis.makeSpiFile(SpiComponentFactory.class, TestSpiComponentFactory.class);

        List<SpiComponent> components = Spis.load(SpiComponent.class);
        System.out.println(components);

        List<Object> list = Spis.loadSpiComponents();
        System.out.println(list);
    }
}
