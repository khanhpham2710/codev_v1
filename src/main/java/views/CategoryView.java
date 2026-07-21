package views;

import app.AppManager;
import dto.response.Category;
import service.CategoriesService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CategoryView extends JPanel {
    private final List<Category> categories;
    private final CategoriesService categoriesService;
    private final JLabel title;

    public CategoryView() {
        this.categoriesService = new CategoriesService();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        title = new JLabel("Categories");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 24));

        add(title, BorderLayout.NORTH);

        JPanel categoryPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        add(categoryPanel, BorderLayout.CENTER);

        this.categories = categoriesService.getCategories();

        categories.forEach(category -> {
            JButton button = new JButton(category.name());

            categoryPanel.add(button);
        });

    }
}
