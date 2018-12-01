package views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;

public class LinearLayoutView {

    public static final class Orientation {
        public static final int HORIZONTAL = 0;
        public static final int VERTICAL = 1;
    }

    public static final class Gravity {
        public static final int START = 0;
        public static final int CENTER = 1;
        public static final int RIGHT = 2;
    }

    public static final class LayoutWidth {
        public static final int WRAP_CONTENT = 0;
        public static final int MATCH_PARENT = 1;
    }

    public static final class LayoutHeight {
        public static final int WRAP_CONTENT = 0;
        public static final int MATCH_PARENT = 1;
    }

    private Pane pane;

    private int orientation;
    private int gravity;

    public LinearLayoutView(int layoutWidth, int layoutHeight, int orientation, int gravity) {
        this.orientation = orientation;
        this.gravity = gravity;

        setOrientation(layoutWidth, layoutHeight, orientation);
        setGravity(gravity);
    }

    public void setOrientation(int layoutWidth, int layoutHeight, int orientation) {
        switch(orientation) {
            case Orientation.VERTICAL : {
                pane = new VBox();

                if(layoutWidth == LayoutWidth.MATCH_PARENT)
                    ((VBox) pane).setFillWidth(true);
                break;
            }
            default : {
                pane = new HBox();

                if(layoutHeight == LayoutHeight.MATCH_PARENT)
                    ((HBox) pane).setFillHeight(true);
            }
        }
    }

    public void setLayoutGravity(Pos pos) {
        switch(orientation) {
            case Orientation.VERTICAL : {
                ((VBox) pane).setAlignment(pos);
                break;
            }
            default : {
                ((HBox) pane).setAlignment(pos);
            }
        }
    }

    public void setGravity(int gravity) {
        switch(gravity) {
            case Gravity.CENTER : {
                pane.getChildren().add(createRegion());
                pane.getChildren().add(createRegion());
                break;
            }
            default : pane.getChildren().add(createRegion());
        }
    }

    private Region createRegion() {
        Region region = new Region();
        switch(orientation) {
            case Orientation.VERTICAL : {
                VBox.setVgrow(region, Priority.ALWAYS);
                break;
            }
            default : HBox.setHgrow(region, Priority.ALWAYS);
        }

        return region;
    }

    public void add(Node node) {
        switch(gravity) {
            case Gravity.CENTER : {
                pane.getChildren().add(1, node);
                break;
            }
            case Gravity.RIGHT : {
                pane.getChildren().add(0, node);
                break;
            }
            default : {
                pane.getChildren().add(1, node);
            }
        }
    }

    public Node getNode() {
        return pane;
    }
}
