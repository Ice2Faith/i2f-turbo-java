package i2f.image.filter.impl.rgba;

import i2f.color.Rgba;
import i2f.image.filter.std.RgbaFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/12/28 13:02
 * @desc
 */
@Data
@NoArgsConstructor
public class ChannelKeepRgbaFilter implements RgbaFilter {
    protected int channelMask = Rgba.Channel.RGBA.mask();
    protected int min = 0;
    protected int max = 155;

    public ChannelKeepRgbaFilter(int channelMask) {
        this.channelMask = channelMask;
    }

    public ChannelKeepRgbaFilter(Rgba.Channel... channels) {
        int mask = 0;
        for (Rgba.Channel channel : channels) {
            if (channel == null) {
                continue;
            }
            mask |= channel.mask();
        }
        this.channelMask = mask;
    }

    @Override
    public Rgba pixel(Rgba color) {
        if (channelMask == Rgba.Channel.R.mask()) {
            int val = color.r;
            if (val >= min && val <= max) {
                return color;
            }
        } else if (channelMask == Rgba.Channel.G.mask()) {
            int val = color.g;
            if (val >= min && val <= max) {
                return color;
            }
        } else if (channelMask == Rgba.Channel.B.mask()) {
            int val = color.b;
            if (val >= min && val <= max) {
                return color;
            }
        } else if (channelMask == Rgba.Channel.A.mask()) {
            int val = color.a;
            if (val >= min && val <= max) {
                return color;
            }
        } else {
            Rgba ret = Rgba.rgba((channelMask & Rgba.Channel.R.mask()) != 0 ? color.r : 0,
                    (channelMask & Rgba.Channel.G.mask()) != 0 ? color.g : 0,
                    (channelMask & Rgba.Channel.B.mask()) != 0 ? color.b : 0,
                    (channelMask & Rgba.Channel.A.mask()) != 0 ? color.a : 255
            );
            int val = ret.gray();
            if (val >= min && val <= max) {
                return color;
            }
        }

        return Rgba.transparent();
    }
}
