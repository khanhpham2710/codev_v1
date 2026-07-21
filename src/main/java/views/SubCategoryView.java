package views;

import dto.response.Category;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SubCategoryView extends JPanel {
    private Category category;
    private JLabel title;

    public SubCategoryView(Category category) {
        this.category = category;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        title = new JLabel(category.name());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 24));

        add(title, BorderLayout.NORTH);

        JPanel categoryPanel = new JPanel(new GridLayout(0, 3, 10, 10));

        category.categories().forEach(c -> {
            JButton button = new JButton("<html><center>" + c.name() + "<br>Total quiz: " + c.quizCount() + "</center></html>");

            button.setPreferredSize(new Dimension(200, 80));

            categoryPanel.add(button);
        });

        JScrollPane scrollPane = new JScrollPane(categoryPanel);

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        add(scrollPane, BorderLayout.CENTER);
    }
}
