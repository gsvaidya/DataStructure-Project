package cs658.project.binarytreedht.messages;

import cs658.project.aux.primitives.Key;
import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Matching;
import cs658.project.binarytreedht.protocol.KeepAliveThread;
import cs658.project.binarytreedht.routing.TreeNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by iyro on 4/25/16.
 */
public abstract class MessageObject {
    private static Logger logger;
    private static TreeNode myTreeNode;

    public static void initialize(TreeNode myTreeNode) {
        MessageObject.myTreeNode = myTreeNode;
    }

    public static MessageObject get(DatagramPacket datagramPacket) {
        String message = new String(datagramPacket.getData()).trim();
        Integer length = Integer.parseInt(message.substring(0, 4));

        List<String> tokens = Matching.getTokens(message);

        if (tokens.get(1).equals("ALIVEOK")) {
            return new KeepAliveResponse(message);
        }

        if (tokens.get(1).equals("ALIVE")) {
            return new KeepAliveRequest(message);
        }

        if (tokens.get(1).equals("JOINOK")) {
            return new JoinResponse(message);
        }

        if (tokens.get(1).equals("JOIN")) {
            return new JoinRequest(message);
        }

        if (tokens.get(1).equals("SEARCHOK")) {
            return new SearchResponse(message);
        }

        if (tokens.get(1).equals("SEARCH")) {
            return new SearchRequest(message);
        }

        if (tokens.get(1).equals("BOOTSTRAPOK")) {
            return new BootstrapResponse(message);
        }

        if (tokens.get(1).equals("CONFLICT")) {
            return new Error();
        }

        if (tokens.get(1).equals("ERROR")) {
            return new Error();
        }

        if (tokens.get(1).equals("CONTENT")) {
            return new Content(message);
        }

        return null;
    }

    public TreeNode getMyTreeNode() {
        return myTreeNode;
    }

    public DatagramSocket getSocket() {
        return myTreeNode.getSocket();
    }

    public Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        MessageObject.logger = logger;
    }

    public KeepAliveThread getLeftKeepAlive() {
        return myTreeNode.getLeftKeepAlive();
    }

    public void setLeftKeepAlive(KeepAliveThread leftKeepAlive) {
        myTreeNode.setLeftKeepAlive(leftKeepAlive);
    }

    public KeepAliveThread getRightKeepAlive() {
        return myTreeNode.getRightKeepAlive();
    }

    public void setRightKeepAlive(KeepAliveThread rightKeepAlive) {
        myTreeNode.setRightKeepAlive(rightKeepAlive);
    }

    public Integer getRoute(Key key) {
        return myTreeNode.getRoute(key);
    }

    public Key getParentKey() {
        return myTreeNode.getParentKey();
    }

    public Key getLeftKey() {
        return myTreeNode.getLeftKey();
    }

    public Key getRightKey() {
        return myTreeNode.getRightKey();
    }

    public Node getLeftNode() {
        return myTreeNode.getLeftNode();
    }

    public Node getRightNode() {
        return myTreeNode.getRightNode();
    }

    public Node getParentNode() {
        return myTreeNode.getParentNode();
    }

    public Node getMyNode() {
        return myTreeNode.getMyNode();
    }

    public Key getMyKey() {
        return myTreeNode.getMyKey();
    }

    public boolean isRoot() {
        return myTreeNode.isRoot();
    }

    public void setRoot(Node rootNode, Key rootKey) {
        myTreeNode.setRoot(rootNode, rootKey);
    }

    public Node getRootNode() {
        return myTreeNode.getRootNode();
    }

    public Key getRootKey() {
        return myTreeNode.getRootKey();
    }

    public void setLeft(Node leftNode, Key leftKey) {
        myTreeNode.setLeft(leftNode, leftKey);
    }

    public void setRight(Node rightNode, Key rightKey) {
        myTreeNode.setRight(rightNode, rightKey);
    }

    public void setParent(Node parentNode, Key parentKey) {
        myTreeNode.setParent(parentNode, parentKey);
    }

    public abstract String getMessage();

    public abstract void process(Node incomingNode) throws IOException;
}
