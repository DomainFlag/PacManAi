package scenes;

import core.Scenemator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import views.TextView;

import java.io.File;

public class Menu extends ViewScene {

    private TextView textStateView;

    public Menu(Scenemator scenemator) {
        super(scenemator, "Menu");
    }

    @Override
    public void onCreateScene(Scene scene, BorderPane pane) {
        Media media = new Media(new File("res/raw/pac_man_intro.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

        VBox vBox = new VBox();
        vBox.setFillWidth(true);
        vBox.setSpacing(16);
        vBox.setAlignment(Pos.CENTER);

        pane.setCenter(vBox);

        textStateView = new TextView("None", 36, Color.WHITE, 16);
        textStateView.setVisible(false);
        textStateView.inflate(vBox);

        TextView textStartView = new TextView("START", 36, Color.LIGHTGRAY, 16);
        textStartView.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                startGame();
            }
        });
        textStartView.setOnHover(Color.WHITE);
        textStartView.inflate(vBox);

        TextView textBuilderView = new TextView("Builder",16, Color.LIGHTGRAY, 4);
        textBuilderView.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                Creator creator = new Creator(getScenemator());
                addScene(creator);
            }
        });
        textBuilderView.setOnHover(Color.WHITE);
        textBuilderView.inflate(vBox);

        TextView textCreditsView = new TextView("Credits",16, Color.LIGHTGRAY, 4);
        textCreditsView.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                Creator creator = new Creator(getScenemator());
                addScene(creator);
            }
        });
        textCreditsView.setOnHover(Color.WHITE);
        textCreditsView.inflate(vBox);
    }

    public void setState(String message) {
        textStateView.setText(message);
        textStateView.setVisible(true);

        registerTimeOutListener(() -> {
            textStateView.setVisible(false);
        }, 3);
    }

    private void startGame() {
        Game game = new Game(getScenemator());
        addScene(game);
    }

    @Override
    public void onKeySceneListener(KeyCode keyCode) {
        if(keyCode == KeyCode.ENTER)
            startGame();
    }

    @Override
    public void onAnimatorCallback() {

    }
}
