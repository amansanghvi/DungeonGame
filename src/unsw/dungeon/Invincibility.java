package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Invincibility extends Consumable {

    public Invincibility(int x, int y, Dungeon dungeon) {
        super(x, y, dungeon);
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            // TODO: Add invincibility to player.
            ((Player) actor).addToInventory(this);
            System.out.println(((Player) actor).getInventory());    // TODO BACKEND TESTING, REMOVE LATER
            consume();
        }
    }
}