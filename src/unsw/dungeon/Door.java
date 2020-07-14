package unsw.dungeon;

import javafx.scene.input.KeyCode;

public class Door extends Entity {
    private String id;
    private boolean isUnlocked = false;
    public Door(int x, int y, String id) {
        super(x, y);
        this.id = id;
    }

    @Override
    public boolean canEntityMoveHere(Entity entity) {
        if (entity instanceof Player) {
            return id.equals(((Player) entity).getKey());
        }
        return false;
    }

    @Override
    public void interact(Entity actor, KeyCode keyCode) {
        if (actor instanceof Player) {
            if (id.equals(((Player) actor).getKey())) {
                isUnlocked = true;
                ((Player) actor).dropKey();
            }
        }
    }
}