package views;

import app.App;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import config.Setting;
import entites.FileNode;
import enums.ETheme;
import org.fife.ui.rsyntaxtextarea.Theme;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class ProjectView extends JPanel {

    private final App app;

    FileNode root;
    JTree projectTree;
    JScrollPane projectScrollPane;
    String directoryPath;

    ArrayList<FileNode> projectFiles = new ArrayList<>();
    String projectPath = null;

    Icon folderIcon;

    public ProjectView(App app) {
        this.app = app;
        this.setPreferredSize(new Dimension(300, 1200));

        setLayout(new BorderLayout());
    }

    public void init() {
        root = new FileNode("Project", null, projectPath, true);
        projectTree = new JTree(root);
        projectTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
        projectScrollPane = new JScrollPane(projectTree);

        folderIcon = new ImageIcon(Objects.requireNonNull
                (ProjectView.class.getResource("/icons/folder_icon_24.png")));
        refreshTree();
    }

    public void openProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        directoryPath = file.getAbsolutePath();

        if (result == JFileChooser.APPROVE_OPTION) {
            projectPath = file.getAbsolutePath();
            openDirectory(file);
        }
    }

    public void openDirectory(File inputFile) {
        openDirectoryRecursive(inputFile, root);
        DefaultTreeModel treeModel = (DefaultTreeModel) projectTree.getModel();
        treeModel.reload();
    }

    private void openDirectoryRecursive(File inputFile, DefaultMutableTreeNode parentNode) {
        File[] files = inputFile.listFiles();
        if (files != null) {
            for (File file : files) {
                FileNode node;
                if (file.isDirectory()) {
                    node = new FileNode(file.getName(), "", file.getAbsolutePath(), true);
                    openDirectoryRecursive(file, node);
                } else {
                    try {
                        Scanner scanner = new Scanner(file);
                        StringBuilder data = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            data.append(scanner.nextLine()).append("\n");
                        }
                        scanner.close();
                        node = new FileNode(file.getName(), data.toString(), file.getAbsolutePath(), false);
                        projectFiles.add(node);

                    } catch (FileNotFoundException e) {
                        System.err.println("File not found: " + file.getAbsolutePath());
                        continue;
                    }
                }
                parentNode.add(node);
            }
        }

    }

    public void initActionListeners() {
        projectTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    FileNode node = (FileNode) projectTree.getLastSelectedPathComponent();
                    if (!node.getIsDirectory()) {
                        setEditorContent(app.getEditorView(), node);
                        FileNode parentNode = (FileNode) node.getParent();
                    }
                } catch (NullPointerException pointerException) {
                    System.out.println("No File Selected");
                } catch (ClassCastException classCastException) {
                    System.out.println("Exception");
                }
            }
        });

    }

    public void setEditorContent(EditorView editorView, FileNode node) {
        editorView.setText(node.getContent());
        app.setTitle(Setting.APP_NAME +  " - " + node.getNodeName());
    }

    public void refreshTree() {
        DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) projectTree.getCellRenderer();
        renderer.setClosedIcon(folderIcon);
        renderer.setOpenIcon(folderIcon);
        model.reload();
    }

    public void addComponent() {
        this.add(projectScrollPane, BorderLayout.CENTER);
    }

    public void setColorTheme(ETheme theme) {
        var t = Setting.getInstance().getTheme(theme);
        Color bg = t.bgColor;

        setBackground(bg);

        projectTree.setBackground(bg);

        projectScrollPane.getViewport().setBackground(bg);
        projectScrollPane.setBackground(bg);

        DefaultTreeCellRenderer renderer =
                (DefaultTreeCellRenderer) projectTree.getCellRenderer();

        renderer.setBackgroundNonSelectionColor(bg);

        renderer.setBackgroundSelectionColor(t.selectionBG != null ? t.selectionBG : bg.darker());
        renderer.setTextSelectionColor(t.selectionFG);
        renderer.setTextNonSelectionColor(t.caretColor);

        repaint();
    }

    public JTree getProjectTree() {
        return projectTree;
    }

    public FileNode getRoot(){
        return root;
    }

    public ArrayList<FileNode> getProjectFiles(){
        return projectFiles;
    }

    public FileNode getSelectedFileNode() {
        return (FileNode) projectTree.getLastSelectedPathComponent();
    }

}