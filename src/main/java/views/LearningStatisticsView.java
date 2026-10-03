package views;

import components.Button.BackButton;
import config.CurrentUser;
import dto.LearningStatistics;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.LocalDateModel;
import service.LearningStatisticsService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LearningStatisticsView extends JPanel {
    private final UUID userId;
    private final LearningStatisticsService service;
    LocalDateModel formModel = new LocalDateModel(), toModel = new LocalDateModel();
    private final JDatePicker from = new JDatePicker(formModel), to = new JDatePicker(toModel);
    private final JLabel status = new JLabel(" ");
    private final JButton refreshButton = new JButton("Refresh");
    private final DefaultTableModel rows = new DefaultTableModel(
            new Object[]{"Date", "Attempts", "Distinct quizzes", "Correct answers"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int col) {
            return col == 0 ? String.class : Long.class;
        }
    };

    public LearningStatisticsView() {
        this(CurrentUser.getInstance().getCurrentUserId(), new LearningStatisticsService());
    }

    public LearningStatisticsView(UUID userId, LearningStatisticsService service) {
        this.userId = java.util.Objects.requireNonNull(userId);
        this.service = java.util.Objects.requireNonNull(service);
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(16, 16, 16, 16));
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        JPanel heading = new JPanel(new FlowLayout(FlowLayout.LEFT));
        heading.add(new BackButton());
        JLabel title = new JLabel("Learning statistics");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        heading.add(title);
        top.add(heading);
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.add(new JLabel("From (yyyy-MM-dd):"));
        filter.add(from);
        filter.add(new JLabel("To:"));
        filter.add(to);
        filter.add(refreshButton);
        top.add(filter);
        add(top, BorderLayout.NORTH);
        JTable table = new JTable(rows);
        table.setRowHeight(28);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(rows);
        sorter.setSortsOnUpdates(true);
        sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.DESCENDING)));
        table.setRowSorter(sorter);
        table.getTableHeader().setToolTipText("Click a column header to sort ascending or descending");
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        add(scrollPane, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        refreshButton.addActionListener(e -> load());
        from.addActionListener(e -> load());
        to.addActionListener(e -> load());
        load();
    }

    private LocalDate parse(LocalDateModel model) {
        return model.getValue();
    }

    private void load() {
        LocalDate first, last;
        try {
            first = parse(formModel);
            last = parse(toModel);
            if (first != null && last != null && first.isAfter(last)) {
                throw new IllegalArgumentException("From date must be on or before To date.");
            }
        } catch (DateTimeParseException e) {
            status.setText("Use valid dates in yyyy-MM-dd format, or leave blank for no limit.");
            return;
        } catch (IllegalArgumentException e) {
            status.setText(e.getMessage());
            return;
        }
        refreshButton.setEnabled(false);
        from.setEnabled(false);
        to.setEnabled(false);
        status.setText("Loading statistics...");
        rows.setRowCount(0);
        new SwingWorker<LearningStatistics, Void>() {
            @Override
            protected LearningStatistics doInBackground() throws Exception {
                return service.load(userId, first, last);
            }

            @Override
            protected void done() {
                try {
                    LearningStatistics data = get();
                    data.days().forEach(day -> rows.addRow(new Object[]{day.date().toString(),
                            day.attempts(), day.quizzes(), day.correctAnswers()}));
                    status.setText(data.attempts() == 0 ? "No saved results in this period."
                            : "Loaded " + data.days().size() + " day(s). Repeated attempts are included.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    status.setText("Loading interrupted.");
                } catch (ExecutionException e) {
                    status.setText("Could not load statistics. Check your database connection and try Refresh.");
                } finally {
                    refreshButton.setEnabled(true);
                    from.setEnabled(true);
                    to.setEnabled(true);
                }
            }
        }.execute();
    }
}
