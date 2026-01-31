package cc.irori.core;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.ShutdownReason;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.concurrent.atomic.AtomicLong;

public class ServerWatchdog {

    private static final HytaleLogger LOGGER = Logs.logger();

    private static final long FREEZE_TIMEOUT_MS = 30_000;
    private static final long SHUTDOWN_TIMEOUT_MS = 60_000;

    private final AtomicLong lastResponseTime = new AtomicLong(System.currentTimeMillis());

    private final World world;
    private volatile boolean stopping = false;

    public ServerWatchdog(World world) {
        this.world = world;
    }

    public void start() {
        LOGGER.atInfo().log("Starting server watchdog for world: " + world.getName());
        Thread watchdog = new Thread(this::runWatchdog, "Irori-Server-Watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
    }

    private void runWatchdog() {
        while (true) {
            boolean shutdown = false;
            String shutdownReason = "Server is not responding with an unknown reason.";

            try {
                try {
                    world.execute(() -> {
                        lastResponseTime.set(System.currentTimeMillis());
                    });
                } catch (Exception e) {
                    shutdownReason = "The world did not accept tasks. The world may have been crashed.";
                    shutdown = true;
                }

                if (!shutdown) {
                    Thread.sleep(5_000);
                    long elapsed = System.currentTimeMillis() - lastResponseTime.get();

                    if (!stopping && elapsed > FREEZE_TIMEOUT_MS) {
                        shutdownReason = "The world has not responded for " + (elapsed / 1000) + " seconds.";
                        shutdown = true;
                    }
                }

                if (shutdown) {
                    stopping = true;
                    LOGGER.atSevere().log("========== WORLD " + world.getName() + " IS STUCK ==========");
                    LOGGER.atSevere().log("# " + shutdownReason);
                    LOGGER.atSevere().log("# Dumping all world threads and initiating shutdown...");

                    Thread.getAllStackTraces().forEach((thread, stackTrace) -> {
                        if (thread.getName().startsWith("WorldThread")) {
                            LOGGER.atSevere().log("Thread: " + thread.getName() + " (ID: " + thread.getId() + "):");
                            for (StackTraceElement element : stackTrace) {
                                LOGGER.atSevere().log("    at " + element.toString());
                            }
                        }
                    });

                    Thread.sleep(5_000);
                    HytaleServer.get().shutdownServer(ShutdownReason.CRASH.withMessage("Watchdog detected a frozen world: " + world.getName()));

                    Thread.sleep(SHUTDOWN_TIMEOUT_MS);
                    LOGGER.atSevere().log("Shutdown cannot proceed. Forcing exit.");
                    Runtime.getRuntime().halt(1);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}
