/**
 * @author Martin Baláž
 */
package com.javaprojekt;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Třída pro přepínání mezi pohledy
 */
public class ViewModel {

    /**
     * Enum se dvěma pohledy
     */
    public enum View {A, B}

    // Atribut
    private final ObjectProperty<View> currentView = new SimpleObjectProperty<>(View.A);

    /**
     * Získá současný pohled
     * @return Současný pohled
     */
    public ObjectProperty<View> currentViewProperty() {
        return currentView ;
    }

    /**
     * Získá současný pohled
     * @return Současný pohled
     */
    public final View getCurrentView() {
        return currentViewProperty().get();
    }

    /**
     * Nastaví pohled
     * @param view Pohled, který se nastaví
     */
    public final void setCurrentView(View view) {
        currentViewProperty().set(view);
    }

}