package org.chubby.github.nightmares.network.paylod;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.chubby.github.nightmares.utils.Utils;

public record SanityData(int sanityLevel) implements CustomPacketPayload
{

    public static final CustomPacketPayload.Type<SanityData> TYPE =
            new Type<>(Utils.resource("sanity_data"));

    public SanityData(int sanityLevel) {
        this.sanityLevel = sanityLevel;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SanityData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SanityData::sanityLevel,
            SanityData::new
    );

}
