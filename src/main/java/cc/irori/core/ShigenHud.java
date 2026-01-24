package cc.irori.core;

import cc.irori.shodo.TextBox;
import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ShigenHud extends CustomUIHud {

    private final Player player;
    private final TextBox textBox = TextBox.builder()
            .setWidth(400)
            .setHeight(400)
            .setCleanupPeriodSeconds(0)
            .build();

    private boolean isVisible = false;
    private int shigenId = 0;

    public ShigenHud(Player player, PlayerRef playerRef) {
        super(playerRef);
        this.player = player;
        updateMessage();
    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        if (!isVisible) {
            return;
        }
        uiCommandBuilder.append("Irori/Shigen.ui");
        textBox.render(uiCommandBuilder, "#ShigenContainer");
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void setShigenId(int shigenId) {
        this.shigenId = shigenId;
        updateMessage();
    }

    public void update() {
        MultipleHUD.getInstance().setCustomHud(player, getPlayerRef(), "IroriCore_ShigenHUD", this);
    }

    private void updateMessage() {
        textBox.typesetter().clear();
        textBox.typesetter().addMessage("/spawn → 資源ワールドを出る", Colors.SKY_LIGHT);
        textBox.typesetter().addMessage("資源ワールド" + shigenId + " (リセットあり)", Colors.SCARLET_LIGHT);
    }
}
