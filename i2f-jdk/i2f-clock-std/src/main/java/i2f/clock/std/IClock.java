package i2f.clock.std;

/**
 * @author Ice2Faith
 * @date 2024/12/27 23:05
 * @desc
 */
public interface IClock {
    long currentMillis();

    default long currentSeconds() {
        return currentMillis() / 1000;
    }

    default long currentMinutes() {
        return currentMillis() / 1000 / 60;
    }
}
