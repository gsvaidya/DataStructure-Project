package cs658.project.binarytreedht.protocol;

import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Communication;
import cs658.project.binarytreedht.messages.JoinRequest;
import cs658.project.binarytreedht.messages.KeepAliveRequest;
import cs658.project.binarytreedht.messages.MessageObject;
import cs658.project.binarytreedht.routing.TreeNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Logger;

/**
 * Created by iyro on 4/12/16.
 */
public class MessageProcessor extends Thread {
    private static TreeNode myTreeNode;
    private Logger logger;

    public MessageProcessor(TreeNode node, Logger logger) throws IOException {
        this.logger = logger;
        MessageProcessor.myTreeNode = node;
    }

    @Override
    public void run() {
        Long start = System.currentTimeMillis();
        Integer rejoinCount = 0;
        while (myTreeNode.getSocket().isBound()) {
            try {
                DatagramPacket datagramPacket = Communication.receive(myTreeNode.getSocket());

                if (datagramPacket == null) {
                    Long timeSinceLastAlive = System.currentTimeMillis() - start;

                    if (timeSinceLastAlive >= 5000L) {
                        if (!myTreeNode.isRoot()) {
                            if (rejoinCount < 5) {
                                Communication.send(new JoinRequest(myTreeNode.getMyNode(), myTreeNode.getMyKey(), false).getMessage(), myTreeNode.getSocket(), myTreeNode.getRootNode());
                                rejoinCount++;
                            } else {
                                logger.severe("Could not rejoin network. Quitting.");
                                System.exit(1);
                            }
                        }
                    }
                } else {
                    Node incomingNode = new Node(datagramPacket);
                    MessageObject messageObject = MessageObject.get(datagramPacket);

                    if (messageObject != null) {
                        messageObject.process(incomingNode);

                        if (messageObject instanceof KeepAliveRequest) {
                            start = System.currentTimeMillis();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
