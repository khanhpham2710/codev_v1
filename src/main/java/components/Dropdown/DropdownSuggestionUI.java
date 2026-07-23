package components.Dropdown;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.util.function.Function;

public class DropdownSuggestionUI extends BasicComboBoxUI {
    private Function<Object, String> getText;

    @Override
    public void installUI(JComponent jc) {
        super.installUI(jc);
        JTextField txt = (JTextField) comboBox.getEditor().getEditorComponent();
        comboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                arrowButton.setBackground(new Color(180, 180, 180));
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                arrowButton.setBackground(new Color(150, 150, 150));
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent pme) {
                arrowButton.setBackground(new Color(150, 150, 150));
            }
        });
        AutoCompleteDecorator.decorate(comboBox);
    }

    @Override
    protected JButton createArrowButton() {
        return new ArrowButton();
    }

    @Override
    protected ComboPopup createPopup() {
        return new DropdownSuggestionPopup(comboBox);
    }

    @Override
    protected ListCellRenderer createRenderer() {
        return new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList jlist, Object e, int i, boolean bln, boolean bln1) {
                String text = "";

                if (DropdownSuggestionUI.this.getText != null) {
                    text = DropdownSuggestionUI.this.getText.apply(e);
                } else {
                    text = e == null ? "" : e.toString();
                }
                JLabel label = new JLabel(text);
                label.setFont(comboBox.getFont());
                if (i >= 0) {
                    label.setBorder(new EmptyBorder(5, 8, 5, 8));
                } else {
                    label.setBorder(null);
                }
                if (bln) {
                    label.setOpaque(true);
                }
                return label;
            }
        };
    }

    @Override
    public void paintCurrentValueBackground(Graphics grphcs, Rectangle rctngl, boolean bln) {
    }

    private static class DropdownSuggestionPopup extends BasicComboPopup {

        public DropdownSuggestionPopup(JComboBox combo) {
            super(combo);
        }

        @Override
        protected JScrollPane createScroller() {
            JScrollPane scroll = super.createScroller();
            ScrollBarCustom sb = new ScrollBarCustom();
            sb.setPreferredSize(new Dimension(12, 70));
            scroll.setVerticalScrollBar(sb);
            ScrollBarCustom sbH = new ScrollBarCustom();
            sbH.setOrientation(JScrollBar.HORIZONTAL);
            scroll.setHorizontalScrollBar(sbH);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            return scroll;
        }
    }

    private static class ArrowButton extends JButton {

        public ArrowButton() {
            setContentAreaFilled(false);
        }

        @Override
        public void paint(Graphics grphcs) {
            super.paint(grphcs);
            Graphics2D g2 = (Graphics2D) grphcs.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();
            int sizeX = 12;
            int sizeY = 8;
            int x = (width - sizeX) / 2;
            int y = (height - sizeY) / 2;
            int[] px = {x, x + sizeX, x + sizeX / 2};
            int[] py = {y, y, y + sizeY};
            g2.setColor(getBackground());
            g2.fillPolygon(px, py, px.length);
            g2.dispose();
        }
    }

    private static class Border extends EmptyBorder {

        public Color getFocusColor() {
            return focusColor;
        }

        public void setFocusColor(Color focusColor) {
            this.focusColor = focusColor;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        private Color focusColor = new Color(128, 189, 255);
        private Color color = new Color(206, 212, 218);

        public Border(int border) {
            super(border, border, border, border);
        }

        public Border() {
            this(5);
        }

        @Override
        public void paintBorder(Component cmpnt, Graphics grphcs, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) grphcs.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (cmpnt.isFocusOwner()) {
                g2.setColor(focusColor);
            } else {
                g2.setColor(color);
            }
            g2.drawRect(x, y, width - 1, height - 1);
            g2.dispose();
        }
    }

    public void setGetText(Function<Object, String> getText) {
        this.getText = getText;
    }
}