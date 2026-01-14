package cc.irori.manager;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ShutdownReason;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.Universe;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ManagerPlugin extends JavaPlugin {

    private static final String DISCORD_INVITE = "ajFSfjbpcX";

    private static final HytaleLogger LOGGER = Logs.logger();

    private final ScheduledExecutorService restartExecutor = Executors.newScheduledThreadPool(1);

    public ManagerPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("Welcome to Irori-Manager :)");

        // Periodically restart the server if no players are online
        restartExecutor.scheduleAtFixedRate(() -> {
            if (Universe.get().getPlayerCount() == 0) {
                HytaleServer.get().shutdownServer(new ShutdownReason(0, "Scheduled restart - No players online"));
            }
        }, 30L, 30L, TimeUnit.MINUTES);

        // Send join and leave messages
        getEventRegistry().register(PlayerConnectEvent.class, event -> {
            broadcastMessage(event.getPlayerRef().getUsername() + " joined the game");

            event.getPlayerRef().sendMessage(Message.join(
                    Message.raw("> Welcome to ").color(Colors.GOLD_LIGHT),
                    Message.raw("IRORI Server").color(Colors.MUSTARD).bold(true),
                    Message.raw("!").color(Colors.GOLD_LIGHT)
            ));
            event.getPlayerRef().sendMessage(Message.join(
                    Message.raw("> Please join our ").color(Colors.SKY_LIGHT),
                    Message.raw("DISCORD").color(Colors.BLUE_LIGHT).bold(true),
                    Message.raw(": ").color(Colors.GOLD_LIGHT),
                    Message.raw("discord.gg/" + DISCORD_INVITE).color(Colors.TEAL).link("https://discord.gg/" + DISCORD_INVITE)
            ));
        });
        getEventRegistry().register(PlayerDisconnectEvent.class, event ->
                broadcastMessage(event.getPlayerRef().getUsername() + " left the game"));
    }

    @Override
    protected void shutdown() {
        restartExecutor.shutdownNow();
        LOGGER.atInfo().log("Irori-Manager shutting down. Goodbye!");
    }

    private static void broadcastMessage(String message) {
        Universe.get().sendMessage(Message.raw(message).color(Colors.YELLOW));
    }
}
