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
import java.io.FileWriter;
import java.io.IOException;
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

    JPopupMenu popupMenu;

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
        createPopupMenu();
    }

    public void openProject() {
        JFileChooser chooser = new JFileChooser(projectPath);
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
                    if (node.getIsDirectory()) {
                        app.getSaveFileButton().setEnabled(false);
                    } else {
                        setEditorContent(app.getEditorView(), node);
                        app.setEnabled(true);
                        FileNode parentNode = (FileNode) node.getParent();
                        app.setCurrentFileParentPath(parentNode.getFilePath());
                    }
                } catch (NullPointerException pointerException) {
                    System.out.println("No File Selected");
                } catch (ClassCastException classCastException) {
                    System.out.println("Exception");
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = projectTree.getClosestRowForLocation(e.getX(), e.getY());
                    projectTree.setSelectionRow(row);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });

    }


    private void createPopupMenu() {
        popupMenu = new JPopupMenu();

        JMenuItem addFileItem = new JMenuItem("New File");
        JMenuItem addFolderItem = new JMenuItem("New Folder");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem renameItem = new JMenuItem("Rename");

        addFileItem.addActionListener(e -> createFile(false));
        addFolderItem.addActionListener(e -> createFile(true));
        deleteItem.addActionListener(e -> deleteFile());
        renameItem.addActionListener(e -> renameFile());

        popupMenu.add(addFileItem);
        popupMenu.add(addFolderItem);
        popupMenu.add(deleteItem);
        popupMenu.add(renameItem);
    }

    private void createFile(boolean isDirectory) {
        FileNode selectedNode = (FileNode) projectTree.getLastSelectedPathComponent();

        FileNode parentNode;
        if (selectedNode == null) {
            parentNode = root;
        } else if (selectedNode.getIsDirectory()) {
            parentNode = selectedNode;
        } else {
            parentNode = (FileNode) selectedNode.getParent();
            if (parentNode == null) parentNode = root;
        }

        File parentFile = (parentNode == root && projectPath != null)
                ? new File(projectPath)
                : new File(parentNode.getFilePath());

        if (isDirectory) {
            String folderName = JOptionPane.showInputDialog(null, "Enter folder name");
            if (folderName == null || folderName.trim().isEmpty()) return;

            File newFolder = new File(parentFile, folderName);
            if (!newFolder.mkdir()) {
                JOptionPane.showMessageDialog(null, "Unable to create folder",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            FileNode newNode = new FileNode(folderName, null, newFolder.getAbsolutePath(), true);
            insertAndRefresh(parentNode, newNode);

        } else {
            String fileName = JOptionPane.showInputDialog(null, "Enter file name");
            if (fileName == null || fileName.trim().isEmpty()) return;

            File newFile = new File(parentFile, fileName);
            try {
                if (newFile.createNewFile()) {
                    FileNode newNode = new FileNode(fileName, "", newFile.getAbsolutePath(), false);
                    projectFiles.add(newNode);
                    insertAndRefresh(parentNode, newNode);
                } else {
                    JOptionPane.showMessageDialog(null, "Unable to create file",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void insertAndRefresh(FileNode parentNode, FileNode newNode) {
        DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();
        int index = parentNode.getChildCount();
        model.insertNodeInto(newNode, parentNode, index);
        projectTree.expandPath(new javax.swing.tree.TreePath(parentNode.getPath()));
    }

    private void deleteFile() {
        FileNode selectedNode = (FileNode) projectTree.getLastSelectedPathComponent();
        try {
            File selectedFile = new File(selectedNode.getFilePath());
            if (selectedFile.exists()) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to delete this file?",
                        "Delete file",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    Desktop desktop = Desktop.getDesktop();
                    boolean deleted = false;

                    if (desktop.isSupported(Desktop.Action.MOVE_TO_TRASH)) {
                        deleted = desktop.moveToTrash(selectedFile);
                    } else {
                        int confirmPermanent = JOptionPane.showConfirmDialog(
                                null,
                                "Your computer does not support moving files to the Recycle Bin.\n"
                                        + "Do you want to delete this file permanently?",
                                "Delete File",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (confirmPermanent == JOptionPane.YES_OPTION) {
                            if (selectedFile.isDirectory()) {
                                deleted = deleteDirectory(selectedFile);
                            } else {
                                deleted = selectedFile.delete();
                            }
                        }
                    }

                    if (deleted) {
                        DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();
                        model.removeNodeFromParent(selectedNode);
                        model.reload();
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Could not delete the file/folder.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Cannot delete opened project", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private boolean deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }

    private void renameFile() {
        FileNode selectedNode = (FileNode) projectTree.getLastSelectedPathComponent();

        if (selectedNode.getIsDirectory()) JOptionPane.showMessageDialog(null, "Cannot rename folders", "Error", JOptionPane.ERROR_MESSAGE);
        else {

            String newName = JOptionPane.showInputDialog(null, "Enter new name", selectedNode.getNodeName());
            if (newName == null || newName.trim().isEmpty()) {
                return;
            }

            File currentFile = new File(selectedNode.getFilePath());
            File parentFile = currentFile.getParentFile();
            File newFile = new File(parentFile, newName);

            if (currentFile.renameTo(newFile)) {

                selectedNode.setNodeName(newName);
                selectedNode.setFilePath(newFile.getAbsolutePath());


                DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();
                model.nodeChanged(selectedNode);

                refreshTree();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Could not rename the file",
                        "Rename Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveFile() {
        FileNode node = (FileNode) projectTree.getLastSelectedPathComponent();
        if (node == null) {
            JOptionPane.showMessageDialog(null, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        node.setContent(app.getEditorView().getText());

        File savedFile = new File(node.getFilePath());
        try (FileWriter writer = new FileWriter(savedFile)) {
            writer.write(node.getContent());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving file: " + savedFile.getName() + "\n" + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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