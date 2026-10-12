package i2f.graphics.d2;

import i2f.graphics.d2.visual.D2Frame;

/**
 * @author Ice2Faith
 * @date 2026/10/12 11:04
 * @desc
 */
public class CherryTreeTest {
    public static void main(String[] args) {
        D2Frame frame = new D2Frame(1080, 720);

        frame.setVisible(true);

        CherryTree.drawCallback(frame);

    }
}
