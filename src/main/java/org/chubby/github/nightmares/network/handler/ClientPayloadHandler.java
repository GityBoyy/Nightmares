package org.chubby.github.nightmares.network.handler;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.chubby.github.nightmares.common.init.ModAttachments;
import org.chubby.github.nightmares.network.paylod.SanityData;

public class ClientPayloadHandler
{
    public static void handleSanityData(final SanityData packet , final IPayloadContext ctx)
    {
        ctx.enqueueWork(()->{
            Player player = ctx.player();
            player.setData(ModAttachments.SANITY,packet.sanityLevel());
        });
    }
}
