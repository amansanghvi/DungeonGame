package unsw.dungeon;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Entity consumed only by the player. Makes the player invincible.
 * It is deleted once it is consumed.
 */
public class InvinciblePlayerState implements PlayerState {
    private final Player player;
    private Timer taskTimer;

    public static String STATE_NAME = "Invincible";
    public static int TIME_LEFT = 5;

    public InvinciblePlayerState(Player player) {
        this.player = player;
    }

    public void startInvincibility() {
        player.setState(this);
        int delay = 1000;
        int period = 1000;

        taskTimer = new Timer();
        InvinciblePlayerState thisState = this;
        TimerTask task = new TimerTask() {
            public void run(){
                thisState.expireState();
            }
        };
        taskTimer.schedule(task, 5*1000);                   //Time in milliseconds
        
        taskTimer.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                TIME_LEFT--;
                System.out.println(TIME_LEFT);
            }
        }, delay, period);
    
    }

    @Override
    public String getStateName() {
        return STATE_NAME;
    }

    @Override
    public void interactWithEnemy(Enemy enemy) {
        enemy.delete();
    }

    @Override
    public void expireState() {
        if (this.taskTimer != null) {
            this.taskTimer.cancel();
            this.taskTimer = null;
            this.player.setState(new DefaultPlayerState(player));
        }
    }

    @Override
    public boolean attractsEnemies() {
        return false;
    }
}