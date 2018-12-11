package scenes;

import controllers.Playground;
import controllers.Tools;
import core.Scenemator;
import interfaces.ItemSelectable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tools.Log;
import views.DrawingView;
import views.SelectView;
import views.TextView;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Menu extends ViewScene {

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
        VBox vSettingsBox = new VBox();
        vSettingsBox.setAlignment(Pos.CENTER);

        VBox.setVgrow(vSettingsBox, Priority.ALWAYS);
        vBox.getChildren().add(vSettingsBox);

        List<Playground> playgrounds = new ArrayList<>();

        SelectView<TextView> selectTextureView = new SelectView<>();
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
            }
        });

        for(String mapTextureName : Tools.generateTextures()) {
            TextView textView = new TextView(mapTextureName, 16, Color.LIGHTGRAY, 16);
            textView.setOnHover(Color.WHITE);
            textView.inflate(selectTextureView);

            selectTextureView.add(textView);
        }

        SelectView<DrawingView> selectMapView = new SelectView<>();
        selectMapView.setSpacing(48);
        selectMapView.inflate(vSettingsBox);
        selectMapView.setItemSelectable(new ItemSelectable<DrawingView>() {
            @Override
            public void onItemActive(DrawingView node) {
                node.setEffect(new DropShadow(25, 0, 0, Color.rgb(255, 255, 0, 0.5)));
            }

            @Override
            public void onItemIdle(DrawingView node) {
                node.setEffect(null);
            }

            @Override
            public void onItemClicked(DrawingView node, int position) {

            }
        });

        for(File map : Tools.generateMaps()) {
            Playground playground = new Playground();
            playground.generateFields(map);
            playground.generatePlaygroundSnapshot(null,6);

            DrawingView drawingView = new DrawingView();
            drawingView.setRoot(16);

            playground.inflate(drawingView);
            playground.addObserver(drawingView);

            selectMapView.add(drawingView);

            playgrounds.add(playground);
        }

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

        // Menu animation
        setMenuAnimation(vBox);
    }

    public void setMenuAnimation(VBox vBox) {
        VBox vAnimationBox = new VBox();
        vAnimationBox.setAlignment(Pos.CENTER);

        VBox.setVgrow(vAnimationBox, Priority.ALWAYS);
        vBox.getChildren().add(vAnimationBox);

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
        int duration = 16000000;
        registerTimeOutListener(new TimeOutListener() {
            @Override
            public void timeOutListenerCallback() {
                for(Rectangle rectangle : rectangles) {
                    double pos = rectangle.getLayoutX();
                    if(pos < 0)
                        pos = distance;

                    rectangle.setLayoutX(pos - width);
                }

                registerTimeOutListener(this, duration);
            }
        }, duration);
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
