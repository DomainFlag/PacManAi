package core;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import scenes.Menu;
import scenes.ViewScene;
import tools.Log;

import java.util.ArrayList;
import java.util.List;

public class Scenemator {

    private Stage primaryStage;

    private List<ViewScene> scenes = new ArrayList<>();

    public Scenemator(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(Constants.TITLE);
        this.primaryStage.initStyle(StageStyle.UNDECORATED);
    }

    public void add(ViewScene viewScene) {
        ViewScene currentScene = scenes.get(scenes.size() - 1);
        currentScene.pauseScene();

        viewScene.showScene(primaryStage);
        scenes.add(viewScene);
    }

    public void onBack() {
        if(scenes.size() > 1) {
            ViewScene currentScene = scenes.get(scenes.size() - 1);
            currentScene.pauseScene();

            ViewScene previousScene = scenes.get(scenes.size() - 2);
            previousScene.showScene(primaryStage);

            scenes.remove(currentScene);
        } else {
            primaryStage.close();
        }
    }

    public void start() {
        Menu menu = new Menu(this);
        menu.showScene(primaryStage);

        scenes.add(menu);
    }
}
