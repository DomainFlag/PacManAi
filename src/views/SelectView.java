package views;

import interfaces.Inflater;
import interfaces.ItemSelectable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class SelectView<T extends Node> extends HBox implements Inflater {

    private ItemSelectable<T> itemSelectable = null;

    private List<T> items = new ArrayList<>();
    private int position = -1;

    public SelectView() {
        setAlignment(Pos.TOP_CENTER);
    }

    public SelectView(List<T> items) {
        this();

        this.items = items;

        getChildren().clear();

        for(T node : items)
            add(node);

        setDefaultActiveItem();
    }

    public void setItemSelectable(ItemSelectable<T> itemSelectable) {
        this.itemSelectable = itemSelectable;
    }

    private void setDefaultActiveItem() {
        if(position == -1) {
            if(!items.isEmpty()) {
                position = 0;

                if(itemSelectable != null)
                    itemSelectable.onItemActive(items.get(position));
            }
        }
    }

    public void add(T node) {
        setDefaultActiveItem();

        items.add(node);

        setOnClickListener(node, items.size() - 1);
    }

    public void setOnClickListener(T node, int index) {
        node.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                if(itemSelectable != null) {
                    itemSelectable.onItemClicked(node, index);

                    if(position != -1) {
                        itemSelectable.onItemIdle(items.get(position));
                    }

                    itemSelectable.onItemActive(node);
                }

                position = index;
            }
        });
    }

    @Override
    public void inflate(Pane pane) {
        pane.getChildren().add(this);
    }
}
