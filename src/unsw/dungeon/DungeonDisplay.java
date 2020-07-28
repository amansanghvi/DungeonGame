package unsw.dungeon;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonDisplay {
    private final Map<Class<? extends Entity>, Map<String, Image>> imageMap;
    private int height, width;
    private GridPane mainMenu;

    private GridPane gridpane;

    public DungeonDisplay(Map<Class<? extends Entity>, Map<String, Image>> imageMap)  {
        this.imageMap = imageMap;
    }

    public void initializeDungeon(int width, int height, List<ImageView> entities, Map<Class<? extends Entity>, Label> goals) {
        this.height = height;
        this.width = width;
        gridpane.getChildren().clear();

        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());
        Image inv_1sec = new Image((new File("images/Invincibility_Time_Icons/invincibility_1sec.png")).toURI().toString());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                gridpane.add(new ImageView(ground), x, y);
            }
        }

        for (ImageView entity : entities) {
            gridpane.getChildren().add(entity);
        }

        int[] xPosition = { 0 };
        int yPosition = height+1;
        goals.forEach((entity, label) -> {
            if (entity == Exit.class) return;
            gridpane.add(new ImageView(imageMap.get(entity).get(DungeonView.DEFAULT_IMG)), xPosition[0]++, yPosition);
            gridpane.add(label, xPosition[0], yPosition, 2, 1);
            xPosition[0] += 2;
        });
        if (goals.get(Exit.class) != null) {
            gridpane.add(new ImageView(imageMap.get(Exit.class).get(DungeonView.DEFAULT_IMG)), xPosition[0]++, yPosition);
            gridpane.add(goals.get(Exit.class), xPosition[0], yPosition, 5, 1);
        }

        gridpane.add(new ImageView(inv_1sec), 4, 0);
    }

    public void showMainMenu(GridPane mainMenu, List<Pair<String, String>> levels,
                             Function<String, Boolean> clickHandler) {
        if (this.mainMenu == null) {
            this.mainMenu = mainMenu;
        }
        mainMenu.setVisible(true);
        TextFlow levelList = (TextFlow)mainMenu.lookup("#level-list");
        levelList.getChildren().clear();
        levelList.getChildren().add(new LineBreak());

        levels.forEach((level) -> {
            Text newLevel = new Text(level.getKey());
            newLevel.setOnMouseClicked((event) -> {
                if (clickHandler.apply(level.getValue())) {
                    mainMenu.setVisible(false);
                }
            });
            levelList.getChildren().addAll(newLevel, new LineBreak());
        });
    }

    public void setGridpane(GridPane gridpane) {
        this.gridpane = gridpane;
    }

    public void addGoalIcons(FlowPane goalIcons, Map<Class<? extends Entity>, Label> goals) {
        goalIcons.getChildren().clear();
        for (Class<? extends Entity> entity : goals.keySet()) {
            goalIcons.getChildren().add(new ImageView(imageMap.get(entity).get(DungeonView.DEFAULT_IMG)));
        }
    }

    public void setGoalString(TextFlow goalList, Goal goal) {
        goalList.getChildren().clear();
        String result = createGoalString(goal);
        String[] resultGoals = result.split("!");
        int indent = 0;
        for (int i = 0; i < resultGoals.length; i++) {
            if (resultGoals[i].contains("(")) {
                indent += 1;
            }
            String textToAdd = resultGoals[i].replaceAll("[()]", "");
            if (textToAdd.length() > 1) {
                Text newText = new Text("- " + textToAdd);
                newText.setTranslateX(16*indent);
                goalList.getChildren().add(newText);
                goalList.getChildren().add(new LineBreak());
            }
            if (resultGoals[i].contains(")")) {
                indent -= 1;
            }
        }
    }

    public String createGoalString(Goal goal) {
        StringBuilder result = new StringBuilder("(");
        if (goal.getGoalEngine() instanceof ComplexGoalEngine) {
            int i = 0;
            for (Goal subgoal : ((ComplexGoalEngine) goal.getGoalEngine()).getChildGoals()) {
                result.append(createGoalString(subgoal));
                if (i < ((ComplexGoalEngine) goal.getGoalEngine()).getChildGoals().size()-1) {
                    if (goal.getGoalEngine() instanceof ANDGoalEngine) {
                        result.append(" AND! ");
                    } else {
                        result.append(" OR! ");
                    }
                }
                i++;
            }
        } else if (goal.getGoalEngine() instanceof TreasureGoalEngine) {
            return "Collect ALL the treasure!";
        } else if (goal.getGoalEngine() instanceof EnemyGoalEngine) {
            return "Slay ALL the enemies!";
        } else if (goal.getGoalEngine() instanceof SwitchesGoalEngine) {
            return "Place a boulder on ALL the switches!";
        } else if (goal.getGoalEngine() instanceof ExitGoalEngine) {
            return "Get to the exit!";
        }

        result.append(")");
        return result.toString();
    }

    public void addFinalText(Text completeText, String text, String color) {
        completeText.setText(text);
        completeText.setFont(new Font("VT323", 40));
        completeText.setFill(Color.web(color));
    }

    public void updateSquares(ImageView newEntity){
        gridpane.getChildren().add(newEntity);
    }

}
