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
public class ChannelExtraRgbaFilter implements RgbaFilter {
    protected int channelMask = Rgba.Channel.RGBA.mask();

    public ChannelExtraRgbaFilter(int channelMask) {
        this.channelMask = channelMask;
    }

    public ChannelExtraRgbaFilter(Rgba.Channel... channels) {
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
        return Rgba.rgba((channelMask & Rgba.Channel.R.mask()) != 0 ? color.r : 0,
                (channelMask & Rgba.Channel.G.mask()) != 0 ? color.g : 0,
                (channelMask & Rgba.Channel.B.mask()) != 0 ? color.b : 0,
                (channelMask & Rgba.Channel.A.mask()) != 0 ? color.a : 255
        );
    }
}
