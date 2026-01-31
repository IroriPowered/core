package cc.irori.core.command;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class CoreDebugCommand extends CommandBase {

    public CoreDebugCommand() {
        super("coredebug", "A debug command.");

        setPermissionGroups("irori.core.debug");
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext context) {
        World defaultWorld = Universe.get().getDefaultWorld();

        context.sendMessage(Message.raw("Freezing the default world thread"));
        defaultWorld.execute(() -> {
            while (true) { }
        });
    }
}
