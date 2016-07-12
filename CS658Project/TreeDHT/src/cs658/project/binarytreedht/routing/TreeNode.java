package cs658.project.binarytreedht.routing;

import cs658.project.aux.primitives.Key;
import cs658.project.aux.primitives.Node;
import cs658.project.binarytreedht.protocol.ContentThread;
import cs658.project.binarytreedht.protocol.KeepAliveThread;

import java.net.DatagramSocket;

/**
 * Created by iyro on 4/25/16.
 */
public class TreeNode {
    private DatagramSocket socket;

    private boolean isRoot;

    private Node rootNode;
    private Key rootKey;

    private Node myNode;
    private Key myKey;

    private Node parentNode;
    private Key parentKey;

    private Node leftNode;
    private Key leftKey;

    private Node rightNode;
    private Key rightKey;

    private KeepAliveThread leftKeepAlive;
    private KeepAliveThread rightKeepAlive;
    private ContentThread contentThread;

    public void setContentThread(ContentThread contentThread) {
        this.contentThread = contentThread;
    }

    public ContentThread getContentThread() {
        return contentThread;
    }

    public TreeNode(Node myNode, DatagramSocket socket) {
        this.myNode = myNode;
        this.socket = socket;
        this.myKey = new Key(myNode);
    }

    public TreeNode(Node myNode, Key myKey, DatagramSocket socket) {
        this.myNode = myNode;
        this.socket = socket;
        this.myKey = myKey;
    }

    public KeepAliveThread getLeftKeepAlive() {
        return leftKeepAlive;
    }

    public void setLeftKeepAlive(KeepAliveThread leftKeepAlive) {
        this.leftKeepAlive = leftKeepAlive;
        if (leftKeepAlive != null)
            leftKeepAlive.start();
    }

    public KeepAliveThread getRightKeepAlive() {
        return rightKeepAlive;
    }

    public void setRightKeepAlive(KeepAliveThread rightKeepAlive) {
        this.rightKeepAlive = rightKeepAlive;
        if (rightKeepAlive != null)
            rightKeepAlive.start();
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setLeft(Node leftNode, Key leftKey) {
        this.leftNode = leftNode;
        this.leftKey = leftKey;
    }

    public void setRight(Node rightNode, Key rightKey) {
        this.rightNode = rightNode;
        this.rightKey = rightKey;
    }

    public void setParent(Node parentNode, Key parentKey) {
        this.parentNode = parentNode;
        this.parentKey = parentKey;
    }

    public Node getMyNode() {
        return myNode;
    }

    public Key getMyKey() {
        return myKey;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public Key getParentKey() {
        return parentKey;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public Key getLeftKey() {
        return leftKey;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public Key getRightKey() {
        return rightKey;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(Node rootNode, Key rootKey) {
        this.rootNode = rootNode;
        this.rootKey = rootKey;

        if (rootKey.equals(myKey)) {
            isRoot = true;
            contentThread = new ContentThread();
            contentThread.start();
        }
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Key getRootKey() {
        return rootKey;
    }

    public Integer getRoute(Key key) {
        /*if (isRoot) {
            if (rootKey.belongsLeft(key)) {
                return -1;
            } else if (rootKey.belongsRight(key)) {
                return 1;
            } else {
                return 0;
            }
        }*/

        /*if (rootKey.belongsLeft(myKey)) {
            if (rootKey.belongsLeft(key)) {
                if (rootKey.clockwiseDistanceTo(key) > rootKey.clockwiseDistanceTo(myKey)) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                return 0;
            }
        } else if (rootKey.belongsRight(myKey)) {
            if (rootKey.belongsRight(key)) {
                if (rootKey.clockwiseDistanceTo(key) > rootKey.clockwiseDistanceTo(myKey)) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }*/


        if (myKey.belongsLeft(key)) {
            return -1;
        }

        if (myKey.belongsRight(key)) {
            return 1;
        }

        return 0;
    }
}
