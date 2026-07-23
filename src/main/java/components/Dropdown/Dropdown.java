package components.Dropdown;

import javax.swing.*;

public class Dropdown <E> extends JComboBox<E> {
    public Dropdown() {
        setUI(new DropdownSuggestionUI());
    }
}
