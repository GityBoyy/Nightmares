package org.chubby.github.nightmares.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.chubby.github.nightmares.common.events.SanityTracker;

public class GetSanityCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("get_sanity")
                        .executes(GetSanityCommand::getSanity)
        );
    }

    private static int getSanity(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (source.getEntity() instanceof ServerPlayer player) {
            SanityTracker tracker = new SanityTracker(100);

            if (tracker != null) {
                int sanityLevel = tracker.getSanityLevel();

                player.sendSystemMessage(Component.literal("Your current sanity level is: " + sanityLevel));
            } else {
                player.sendSystemMessage(Component.literal("Could not retrieve sanity level."));
            }

            return 1;
        }

        return 0;
    }
}
