package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Sword extends Consumable {

    public Sword(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            // TODO: Add sword to player.
            consume();
        }
    }
}
