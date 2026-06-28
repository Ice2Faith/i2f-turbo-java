package i2f.mixin.impl;


import i2f.mixin.consts.MixinConsts;

/**
 * @author Ice2Faith
 * @date 2026/5/14 8:56
 * @desc
 */
public interface RandomMixins {

    default int rand() {
        return MixinConsts.RANDOM.nextInt();
    }

    default int rand(int bound) {
        return MixinConsts.RANDOM.nextInt(bound);
    }

    default int rand(int min, int max) {
        return MixinConsts.RANDOM.nextInt(max - min) + min;
    }

    default double random() {
        return MixinConsts.RANDOM.nextDouble();
    }
}
