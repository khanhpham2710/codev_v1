package views;

import app.AppManager;
import components.Button.BackButton;
import components.Dropdown.Dropdown;
import components.Pagination.Pagination;
import config.CurrentUser;
import dto.ScoreDTO;
import dto.response.*;
import dto.response.CategoriesResponse.*;
import enums.EDifficulty;
import service.QuizService;
import service.ScoreService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class QuizzesView extends JPanel {

    private JPanel quizListPanel;
    private QuizService quizService;
    private Pagination pagination;

    private Dropdown<String> categoryDropdown;
    private Dropdown<EDifficulty> difficultyDropdown;

    private static final int LIMIT = 10;

    private final ScoreService scoreService;
    private List<ScoreDTO> scoreDTOS = new ArrayList<>();

    public QuizzesView(Category category, SubCategory selectedCategory) {

        setLayout(new BorderLayout(10, 10));

        scoreService = new ScoreService();
        quizService = new QuizService();

        UUID currentUserId = CurrentUser.getInstance().getCurrentUserId();

        if (currentUserId != null) {
            try {
                scoreDTOS = scoreService.getScoresByUserId(currentUserId);
            } catch (IllegalArgumentException e) {
                scoreDTOS = Collections.emptyList();
            }
        }

        JPanel filterPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

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

        leftPanel.add(categoryDropdown);
        leftPanel.add(difficultyDropdown);
        leftPanel.add(searchButton);

        BackButton backButton = new BackButton();

        JPanel rightPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        rightPanel.add(backButton);


        filterPanel.add(leftPanel, BorderLayout.WEST);
        filterPanel.add(rightPanel, BorderLayout.EAST);

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

        JPanel paginationPanel = new JPanel();

        pagination = new Pagination();
        paginationPanel.add(pagination, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);

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

        List<ScoreDTO> scores = scoreDTOS.stream()
                .filter(q -> quiz.id() != null && quiz.id().equals(q.getQuizId())).toList();

        ScoreDTO highestScore = scores.stream()
                .max(Comparator.comparing(ScoreDTO::getScore))
                .orElse(null);

        String infoText = "Difficulty: "
                + quiz.difficulty()
                + " | Questions: "
                + quiz.questionCount();

        if (highestScore != null){
            infoText = infoText + " Highest score: " + highestScore.getScore()
                    + " | Total try: " + scores.size();
        }

        JLabel info = new JLabel(infoText);

        panel.add(title, BorderLayout.NORTH);
        panel.add(info, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                QuizDataResponse.QuizData quizData = quizService.getQuizById(quiz.id());

                if (quizData != null){
                    AppManager.getInstance().changeView(new QuizView(quizData));
                }
            }
        });

        return panel;
    }
}