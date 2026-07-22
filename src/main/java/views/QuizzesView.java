package views;

import components.Dropdown.Dropdown;
import components.Pagination.Pagination;
import components.Pagination.PaginationEvent;
import dto.response.Category;
import dto.response.PaginationMeta;
import dto.response.QuizzesOverall;
import dto.response.QuizzesResponse;
import dto.response.SubCategory;
import enums.EDifficulty;
import service.QuizService;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuizzesView extends JPanel {

    private JPanel quizListPanel;
    private QuizService quizService;
    private Pagination pagination;

    private Dropdown<String> categoryDropdown;
    private Dropdown<EDifficulty> difficultyDropdown;

    private static final int LIMIT = 10;

    public QuizzesView(Category category, SubCategory selectedCategory) {

        setLayout(new BorderLayout(10, 10));

        quizService = new QuizService();

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        categoryDropdown = new Dropdown<>();
        difficultyDropdown = new Dropdown<>();

        String[] categories = category.categories()
                .stream()
                .map(SubCategory::name)
                .toArray(String[]::new);

        categoryDropdown.setModel(new DefaultComboBoxModel<>(categories));
        categoryDropdown.setSelectedItem(selectedCategory.name());

        difficultyDropdown.setModel(
                new DefaultComboBoxModel<>(EDifficulty.values())
        );

        JButton searchButton = new JButton("Search");

        filterPanel.add(categoryDropdown);
        filterPanel.add(difficultyDropdown);
        filterPanel.add(searchButton);

        add(filterPanel, BorderLayout.NORTH);


        quizListPanel = new JPanel();

        BoxLayout boxLayout = new BoxLayout(
                quizListPanel,
                BoxLayout.Y_AXIS
        );

        quizListPanel.setLayout(boxLayout);
        quizListPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(quizListPanel);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        add(scrollPane, BorderLayout.CENTER);


        pagination = new Pagination();
        add(pagination, BorderLayout.SOUTH);

        pagination.addEventPagination(this::loadData);
        searchButton.addActionListener(e -> loadData(1));
        loadData(1);
    }

    private void loadData(int page) {

        Map<String, String> params = new HashMap<>();

        params.put(
                "category",
                Objects.requireNonNull(
                        categoryDropdown.getSelectedItem()
                ).toString()
        );

        params.put(
                "difficulty",
                Objects.requireNonNull(
                        difficultyDropdown.getSelectedItem()
                ).toString()
        );

        params.put("limit", String.valueOf(LIMIT));
        params.put("offset", String.valueOf((page - 1) * LIMIT));

        QuizzesResponse response = quizService.getQuizzes(params);

        if (response == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error"
            );
            return;
        }

        List<QuizzesOverall> quizzes = response.data();
        PaginationMeta meta = response.meta();

        updateQuizList(quizzes);

        int totalPages =
                (int) Math.ceil((double) meta.total() / meta.limit());

        int currentPage =
                (int) ((meta.offset() / meta.limit()) + 1);

        pagination.setPagegination(
                currentPage,
                totalPages
        );
    }

    private void updateQuizList(List<QuizzesOverall> quizzes) {

        quizListPanel.removeAll();

        for (QuizzesOverall quiz : quizzes) {
            quizListPanel.add(createQuizItem(quiz));
            quizListPanel.add(Box.createVerticalStrut(5));
        }

        quizListPanel.revalidate();
        quizListPanel.repaint();
    }

    private JPanel createQuizItem(QuizzesOverall quiz) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10,10,10,10)
                )
        );

        panel.setPreferredSize(new Dimension(0, 120));
        panel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 120)
        );

        JLabel title = new JLabel(quiz.title());
        title.setFont(
                title.getFont().deriveFont(Font.BOLD,16)
        );

        JLabel info = new JLabel(
                "Difficulty: "
                        + quiz.difficulty()
                        + " | Questions: "
                        + quiz.questionCount()
        );


        panel.add(title, BorderLayout.NORTH);
        panel.add(info, BorderLayout.CENTER);

        return panel;
    }
}