package cs658.project.binarytreedht.protocol;

import cs658.project.aux.utilities.Communication;
import cs658.project.binarytreedht.routing.TreeNode;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by iyro on 4/26/16.
 */
public class ContentThread extends Thread {
    private static TreeNode myTreeNode;
    private static String test = "CONTENT Root says hi!";

    public static void initialize(TreeNode treeNode) {
        ContentThread.myTreeNode = treeNode;
        ContentThread.test = getContent();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(getContent());
                if (myTreeNode.getLeftNode()!=null)
                    Communication.send(test, myTreeNode.getSocket(), myTreeNode.getLeftNode());
                if (myTreeNode.getRightNode()!=null)
                    Communication.send(test, myTreeNode.getSocket(), myTreeNode.getRightNode());
                Thread.sleep(3000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getContent() {
        DecimalFormat nf = new DecimalFormat("0000");

        StringBuilder sb = new StringBuilder();
        sb.append(" CONTENT ");
        sb.append("Root says Hi.");
        return nf.format(sb.toString().length() + 4) + sb.toString();
    }
}
