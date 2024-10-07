package org.chubby.github.nightmares.common.init;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.chubby.github.nightmares.Constants;

import java.util.function.Supplier;

public class ModAttachments
{
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT = DeferredRegister
            .create(NeoForgeRegistries.ATTACHMENT_TYPES,Constants.MODID);

    public static final Supplier<AttachmentType<Integer>> SANITY = ATTACHMENT.register("sanity",
            ()-> AttachmentType.builder(()->0).serialize(Codec.INT).build());

    public static void register(IEventBus eventBus)
    {
        ATTACHMENT.register(eventBus);
    }
}
