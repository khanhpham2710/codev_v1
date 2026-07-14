package entites;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileNode extends DefaultMutableTreeNode {

    private String nodeName, content, path;
    private boolean isDirectory;

    public FileNode(String name, String content, String path, boolean isDirectory) {
        super(name);
        this.nodeName = name;
        this.content = content;
        this.path = path;
        this.isDirectory = isDirectory;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getContent() {
        return content;
    }

    public String getFilePath() {
        return path;
    }

    public boolean getIsDirectory(){
        return isDirectory;
    }

    public void setFilePath(String newPath) {
        this.path = newPath;
    }

    public void setContent(String newContent){
        content = newContent;
    }

    public void setNodeName(String newName) {
        this.nodeName = newName;
        this.setUserObject(newName);
    }

    public void setDirectory(boolean isDirectory){
        this.isDirectory = isDirectory;
    }
}