package interfaces;

import javafx.scene.Node;

public interface ItemSelectable<T> {
    void onItemActive(T node);
    void onItemIdle(T node);
    void onItemClicked(T node, int position);
}
