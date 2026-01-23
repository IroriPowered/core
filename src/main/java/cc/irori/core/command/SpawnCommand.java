package cc.irori.core.command;

import cc.irori.shodo.ShodoAPI;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class SpawnCommand extends AbstractPlayerCommand {

    public SpawnCommand() {
        super("spawn", "Teleports to the server spawn.");
    }

    @Override
    protected void execute(@NonNullDecl CommandContext context, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        World defaultWorld = Universe.get().getDefaultWorld();
        Transform spawn = defaultWorld.getWorldConfig().getSpawnProvider().getSpawnPoint(ref, store);
        Teleport teleport = Teleport.createForPlayer(defaultWorld, spawn);
        store.addComponent(ref, Teleport.getComponentType(), teleport);

        context.sendMessage(Message.raw("Teleported to the server spawn."));
        ShodoAPI.getInstance().sendMessage(playerRef, "サーバーのスポーン地点に移動しました。");
    }
}
