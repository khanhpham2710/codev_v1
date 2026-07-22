package views;

import app.AppManager;
import dto.response.Category;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SubCategoryView extends JPanel {
    private Category category;
    private JLabel title;
    private JButton backButton;

    private JPanel headerPanel;

    public SubCategoryView(Category category) {
        this.category = category;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        headerPanel = new JPanel(new BorderLayout());

        backButton = new JButton("Back");

        backButton.addActionListener(e -> {
            AppManager.getInstance().back();
        });

        title = new JLabel(category.name(), SwingConstants.CENTER);
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 24));

        JPanel spacer = new JPanel();
        spacer.setPreferredSize(backButton.getPreferredSize());

        headerPanel.add(spacer, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(backButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JPanel categoryPanel = new JPanel(new GridLayout(0, 3, 10, 10));

        category.categories().forEach(c -> {
            JButton button = new JButton("<html><center>" + c.name() + "<br>Total quiz: " + c.quizCount() + "</center></html>");

            button.setPreferredSize(new Dimension(200, 80));

            button.addActionListener(e -> {
                AppManager.getInstance().changeView(new QuizzesView(category, c));
            });

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
