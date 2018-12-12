package scenes;

import controllers.Playground;
import controllers.Tools;
import core.Scenemator;
import interfaces.ItemSelectable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.GameSettings;
import models.PacMan;
import models.Phantom;
import models.Vector;
import tools.Log;
import views.DrawingView;
import views.SelectView;
import views.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Menu extends ViewScene {

    private GameSettings gameSettings = new GameSettings();
    private TextView textStateView;

    public Menu(Scenemator scenemator) {
        super(scenemator, "Menu");
    }

    @Override
    public void onCreateScene(Scene scene, BorderPane pane) {
//        Media media = new Media(new File("res/raw/pac_man_intro.mp3").toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();

        VBox vBox = new VBox();
        vBox.setFillWidth(true);
        vBox.setSpacing(16);
        vBox.setAlignment(Pos.CENTER);

        pane.setCenter(vBox);

        // Game settings
        setGameSettings(vBox);

        // Menu actions
        setMenuActions(vBox);

        // Menu animation
        setMenuAnimation(vBox);
    }

    /**
     * Set menu actions: starting game based on game settings adjusted in @setGameSettings, creating custom board or credits
     * @param vBox root layout
     */
    private void setMenuActions(VBox vBox) {
        VBox vActionsBox = new VBox();
        vActionsBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(vActionsBox);

        textStateView = new TextView("None", 36, Color.WHITE, 16);
        textStateView.setVisible(false);
        textStateView.inflate(vActionsBox);

        TextView textStartView = new TextView("START", 36, Color.LIGHTGRAY, 16);
        textStartView.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                startGame();
            }
        });
        textStartView.setOnHover(Color.WHITE);
        textStartView.inflate(vActionsBox);

        TextView textBuilderView = new TextView("Builder",16, Color.LIGHTGRAY, 4);
        textBuilderView.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                Creator creator = new Creator(getScenemator());
                addScene(creator);
            }
        });
        textBuilderView.setOnHover(Color.WHITE);
        textBuilderView.inflate(vActionsBox);

        TextView textCreditsView = new TextView("Credits",16, Color.LIGHTGRAY, 4);
        textCreditsView.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                Creator creator = new Creator(getScenemator());
                addScene(creator);
            }
        });
        textCreditsView.setOnHover(Color.WHITE);
        textCreditsView.inflate(vActionsBox);
    }

    /**
     * Adjusting game settings: map style and map itself
     * @param vBox root layout
     */
    private void setGameSettings(VBox vBox) {
        VBox vSettingsBox = new VBox();
        vSettingsBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(vSettingsBox);

        List<Playground> playgrounds = new ArrayList<>();

        SelectView<TextView> selectTextureView = new SelectView<>();
        VBox.setMargin(selectTextureView, new Insets(8));
        selectTextureView.inflate(vSettingsBox);
        selectTextureView.setItemSelectable(new ItemSelectable<TextView>() {
            @Override
            public void onItemActive(TextView node) {
                node.setDefaultPaint(Color.WHITE);
            }

            @Override
            public void onItemIdle(TextView node) {
                node.setDefaultPaint(Color.LIGHTGRAY);
            }

            @Override
            public void onItemClicked(TextView node, int position) {
                for(Playground playground : playgrounds) {
                    playground.setStyle(position);
                    playground.generatePlaygroundSnapshot(null, 6);
                }

                gameSettings.setStyle(position);
            }
        });

        for(String mapTextureName : Tools.generateTextures()) {
            TextView textView = new TextView(mapTextureName, 16, Color.LIGHTGRAY, 16);
            textView.setOnHover(Color.WHITE);
            textView.inflate(selectTextureView);

            selectTextureView.add(textView);
        }

        SelectView<DrawingView> selectMapView = new SelectView<>();
        VBox.setMargin(selectMapView, new Insets(8));
        selectMapView.setSpacing(48);
        selectMapView.inflate(vSettingsBox);
        selectMapView.setItemSelectable(new ItemSelectable<DrawingView>() {
            @Override
            public void onItemActive(DrawingView node) {
                node.setEffect(new DropShadow(50, 0, 0, Color.rgb(255, 255, 0, 0.4)));
            }

            @Override
            public void onItemIdle(DrawingView node) {
                node.setEffect(null);
            }

            @Override
            public void onItemClicked(DrawingView node, int position) {
                gameSettings.setFile(playgrounds.get(position).getSource());
            }
        });

        List<File> fileMaps = Tools.generateMaps();
        if(fileMaps.size() > 0)
            gameSettings.setFile(fileMaps.get(0));

        for(File map : fileMaps) {
            Playground playground = new Playground();
            playground.generateFields(map);
            playground.generatePlaygroundSnapshot(null,6);

            DrawingView drawingView = new DrawingView();
            drawingView.setRoot(16);

            playground.inflate(drawingView);
            playground.addObserver(drawingView);

            drawingView.inflate(selectMapView);
            selectMapView.add(drawingView);

            playgrounds.add(playground);
        }
    }

    /**
     * Create menu animation as menu decoration
     * @param vBox root layout
     */
    private void setMenuAnimation(VBox vBox) {
        VBox vAnimationBox = new VBox();
        vAnimationBox.setAlignment(Pos.CENTER);

        VBox.setVgrow(vAnimationBox, Priority.ALWAYS);
        vBox.getChildren().add(vAnimationBox);

        // Characters for animation
        HBox hCharactersBox = new HBox();
        hCharactersBox.setAlignment(Pos.CENTER);
        vAnimationBox.getChildren().add(hCharactersBox);

        Pane paneCharacters = new Pane();
        paneCharacters.setPadding(new Insets(8));
        hCharactersBox.getChildren().add(paneCharacters);

        PacMan pacMan = new PacMan(new Vector(0, 0));
        pacMan.render(paneCharacters);

        Phantom phantom = new Phantom(new Vector(5, 0));
        phantom.render(paneCharacters);

        // 10 fps
        int durationPacManAnimation = 96000000;
        registerTimeOutListener(new TimeOutListener() {
            @Override
            public void timeOutListenerCallback() {
                pacMan.wobble();

                registerTimeOutListener(this, durationPacManAnimation);
            }
        }, durationPacManAnimation);

        // 60 fps
        int durationPhantom = 96000000;
        registerTimeOutListener(new TimeOutListener() {
            @Override
            public void timeOutListenerCallback() {
                phantom.wobble();

                registerTimeOutListener(this, durationPhantom);
            }
        }, durationPhantom);

        // Moving blocks for animation
        HBox hBlocksBox = new HBox();
        hBlocksBox.setAlignment(Pos.CENTER);
        vAnimationBox.getChildren().add(hBlocksBox);

        Pane pane = new Pane();

        int width = 10;
        int distance = 450;
        int offset = 16;

        int count = (int) Math.floor((distance - offset) / (offset + width));

        List<Rectangle> rectangles = new ArrayList<>();
        for(int g = 0; g < count; g++) {
            Rectangle rectangle = new Rectangle(width,  4);
            rectangle.setFill(Color.WHITE);
            rectangle.setLayoutX(g * (offset + width));

            pane.getChildren().add(rectangle);

            rectangles.add(rectangle);
        }

        hBlocksBox.getChildren().add(pane);

        // 60 fps
        int durationMovingBlocks = 16000000;
        registerTimeOutListener(new TimeOutListener() {
            @Override
            public void timeOutListenerCallback() {
                for(Rectangle rectangle : rectangles) {
                    double pos = rectangle.getLayoutX();
                    if(pos < 0)
                        pos = count * (offset + width);

                    rectangle.setLayoutX(pos - width);
                }

                registerTimeOutListener(this, durationMovingBlocks);
            }
        }, durationMovingBlocks);
    }

    public void setState(String message) {
        textStateView.setText(message);
        textStateView.setVisible(true);

        registerTimeOutListener(() -> {
            textStateView.setVisible(false);
        }, 3000000000L);
    }

    private void startGame() {
        Game game = new Game(getScenemator());
        game.onAttachGameSettings(gameSettings);

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
