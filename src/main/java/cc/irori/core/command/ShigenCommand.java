package cc.irori.core.command;

import cc.irori.core.CorePlugin;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ShigenCommand extends AbstractPlayerCommand {

    public ShigenCommand() {
        super("shigen", "Teleport to the shigen world.");
    }

    @Override
    protected void execute(@NonNullDecl CommandContext context, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        World shigenWorld = CorePlugin.getNewestShigenWorld();
        Transform spawn = shigenWorld.getWorldConfig().getSpawnProvider().getSpawnPoint(ref, store);
        Teleport teleport = Teleport.createForPlayer(shigenWorld, spawn);
        store.addComponent(ref, Teleport.getComponentType(), teleport);
    }
}
