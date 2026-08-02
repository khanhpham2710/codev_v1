package views;

import components.Button.BackButton;
import dto.response.QuizDataResponse.Answer;
import dto.response.QuizDataResponse.Question;
import dto.response.QuizDataResponse.QuizData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class QuizView extends JPanel {

    private final JPanel contentPanel;
    private final JLabel timerLabel;

    private final JButton startButton;
    private final JButton pauseButton;
    private final JButton finishButton;
    private final JButton resetButton;

    private boolean isPause = true;
    private boolean quizLocked = false;

    private final Map<Question, Answer> selectedAnswers = new HashMap<>();
    private final Map<Question, JButton> selectedButtons = new HashMap<>();
    private final Map<Question, Map<Answer, JButton>> answerButtons = new HashMap<>();

    private Timer timer;
    private int remainingSeconds;


    public QuizView(QuizData quizData) {

        setLayout(new BorderLayout());

        remainingSeconds = quizData.questionCount() * 30;


        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(10, 20, 10, 20));


        timerLabel = new JLabel("Time: " + remainingSeconds + "s");


        startButton = new JButton("Start");
        startButton.addActionListener(e -> startTimer(quizData));


        pauseButton = new JButton("Pause");
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(e -> pauseQuiz());


        finishButton = new JButton("Finish");
        finishButton.setEnabled(false);
        finishButton.addActionListener(e -> finishQuiz(quizData));


        resetButton = new JButton("Reset");
        resetButton.setEnabled(false);
        resetButton.addActionListener(e -> resetQuiz(quizData));


        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        actions.add(startButton);
        actions.add(pauseButton);
        actions.add(resetButton);
        actions.add(finishButton);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        BackButton backButton = new BackButton();

        leftPanel.add(backButton);
        leftPanel.add(timerLabel);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);


        add(header, BorderLayout.NORTH);


        contentPanel = new JPanel();

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));


        for (Question question : quizData.questions()) {

            JPanel panel = createQuestionPanel(question);

            panel.setAlignmentX(Component.LEFT_ALIGNMENT);

            contentPanel.add(panel);
            contentPanel.add(Box.createVerticalStrut(20));
        }


        JScrollPane scrollPane = new JScrollPane(contentPanel);

        scrollPane.setBorder(null);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);


        add(scrollPane, BorderLayout.CENTER);
    }


    private JPanel createQuestionPanel(Question question) {

        JPanel panel = new JPanel(new BorderLayout(0, 20));


        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), new EmptyBorder(20, 20, 20, 20)));


        JLabel label = new JLabel("<html><body style='width:600px'>" + question.text() + "</body></html>");


        panel.add(label, BorderLayout.NORTH);


        JPanel answersPanel = new JPanel(new GridLayout(2, 2, 15, 15));


        Map<Answer, JButton> buttons = new HashMap<>();

        answerButtons.put(question, buttons);


        for (Answer answer : question.answers()) {

            JButton button = new JButton(answer.text());


            button.setPreferredSize(new Dimension(0, 50));


            button.setEnabled(false);


            button.addActionListener(e -> {

                if (quizLocked) {
                    return;
                }

                selectedAnswers.put(question, answer);


                selectedButtons.put(question, button);


                for (Component component : answersPanel.getComponents()) {

                    if (component instanceof JButton btn) {

                        btn.setBorder(UIManager.getBorder("Button.border"));
                    }
                }


                button.setBorder(BorderFactory.createLineBorder(new Color(50, 120, 255), 3));
            });


            buttons.put(answer, button);


            answersPanel.add(button);
        }


        panel.add(answersPanel, BorderLayout.CENTER);


        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));


        panel.setPreferredSize(new Dimension(800, 240));


        return panel;
    }


    private void startTimer(QuizData quizData) {

        if (timer != null && timer.isRunning()) {
            return;
        }


        timer = new Timer(1000, e -> {

            remainingSeconds--;


            timerLabel.setText("Time: " + remainingSeconds + "s");


            if (remainingSeconds <= 0) {

                timer.stop();

                finishQuiz(quizData);
            }
        });


        timer.start();


        isPause = true;


        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        finishButton.setEnabled(true);
        resetButton.setEnabled(true);

        toggleEnableButtons(true);
    }


    private void pauseQuiz() {

        if (isPause) {

            if (timer != null) {
                timer.stop();
            }


            pauseButton.setText("Continue");


            isPause = false;


            toggleEnableButtons(false);


        } else {


            if (timer != null) {
                timer.start();
            }


            pauseButton.setText("Pause");


            isPause = true;


            toggleEnableButtons(true);
        }
    }


    private void finishQuiz(QuizData quizData) {

        if (timer != null) {
            timer.stop();
        }

        quizLocked = true;

        int correct = 0;


        for (Question question : quizData.questions()) {


            Answer selected = selectedAnswers.get(question);


            Map<Answer, JButton> buttons = answerButtons.get(question);


            for (Answer answer : question.answers()) {


                JButton button = buttons.get(answer);


                button.setBorder(UIManager.getBorder("Button.border"));


                if (answer.isCorrect()) {

                    button.setOpaque(true);
                    button.setContentAreaFilled(true);

                    button.setBackground(new Color(76, 175, 80));
                    button.setForeground(Color.WHITE);
                }


                if (selected != null && selected.equals(answer) && !answer.isCorrect()) {

                    button.setOpaque(true);
                    button.setContentAreaFilled(true);

                    button.setBackground(new Color(244, 67, 54));
                    button.setForeground(Color.WHITE);
                }

            }

            if (selected != null && selected.isCorrect()) {

                correct++;
            }
        }

        JOptionPane.showMessageDialog(this, "Score: " + correct + "/" + quizData.questions().size());

        finishButton.setEnabled(false);
        pauseButton.setEnabled(false);
    }


    private void resetQuiz(QuizData quizData) {

        selectedAnswers.clear();
        selectedButtons.clear();

        quizLocked = false;

        for (Map<Answer, JButton> buttons : answerButtons.values()) {

            for (JButton button : buttons.values()) {

                button.setOpaque(false);
                button.setContentAreaFilled(true);

                button.setBackground(
                        UIManager.getColor("Button.background")
                );

                button.setForeground(
                        UIManager.getColor("Button.foreground")
                );

                button.setBorder(
                        UIManager.getBorder("Button.border")
                );

                button.setEnabled(false);
            }
        }

        remainingSeconds = quizData.questionCount() * 30;

        timerLabel.setText(
                "Time: " + remainingSeconds + "s"
        );

        finishButton.setEnabled(true);
        pauseButton.setEnabled(false);
        startButton.setEnabled(true);

        pauseButton.setText("Pause");

        if (timer != null) {
            timer.stop();
        }

        isPause = true;
    }


    private void toggleEnableButtons(boolean enabled) {
        for (Map<Answer, JButton> buttons : answerButtons.values()) {
            for (JButton button : buttons.values()) {
                button.setEnabled(enabled);
            }
        }
    }
}