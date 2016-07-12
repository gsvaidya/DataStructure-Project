package cs658.project.binarytreedht.protocol;

import cs658.project.aux.primitives.Key;
import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Communication;
import cs658.project.binarytreedht.messages.KeepAliveRequest;
import cs658.project.binarytreedht.routing.TreeNode;

import java.io.IOException;

/**
 * Created by iyro on 4/26/16.
 */
public class KeepAliveThread extends Thread {
    private static TreeNode myTreeNode;
    private Node node;
    private Key key;
    private boolean prevResponse;

    public KeepAliveThread(Node node, Key key) {
        this.node = node;
        this.key = key;
        this.prevResponse = true;
    }

    public static void initialize(TreeNode treeNode) {
        KeepAliveThread.myTreeNode = treeNode;
    }

    public void setPrevResponse(boolean prevResponse) {
        this.prevResponse = prevResponse;
    }

    @Override
    public void run() {
        while (node != null) {
            try {
                Thread.sleep(5000);
                if (prevResponse) {
                    setPrevResponse(false);
                    Communication.send(new KeepAliveRequest(myTreeNode.getMyKey()).getMessage(), myTreeNode.getSocket(), node);
                } else {
                    if (myTreeNode.getLeftKey() != null && myTreeNode.getLeftKey().equals(this.key)) {
                        myTreeNode.setLeft(null, null);
                        myTreeNode.setLeftKeepAlive(null);
                    }

                    if (myTreeNode.getRightKey() != null && myTreeNode.getRightKey().equals(this.key)) {
                        myTreeNode.setRight(null, null);
                        myTreeNode.setRightKeepAlive(null);
                    }
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
