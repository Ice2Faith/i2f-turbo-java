package i2f.net.nio.udp;

import java.io.IOException;
import java.nio.channels.DatagramChannel;

/**
 * @author Ice2Faith
 * @date 2022/5/11 9:50
 * @desc
 */
public interface IUdpListener {

    void onRead(DatagramChannel sc, IUdpConnector server) throws IOException;

    void onWrite(DatagramChannel sc, IUdpConnector server) throws IOException;
}
