package cs658.project.binarytreedht.messages;

import cs658.project.aux.primitives.Key;
import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Communication;
import cs658.project.aux.utilities.Matching;
import cs658.project.binarytreedht.protocol.KeepAliveThread;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by iyro on 4/25/16.
 */
public class JoinRequest extends MessageObject {
    private Node node;
    private Key key;
    private boolean hasRootSeen;


    public JoinRequest(Node node, Key key, boolean hasRootSeen) {
        this.node = node;
        this.key = key;
        this.hasRootSeen = hasRootSeen;
    }

    public JoinRequest(String message) {
        Integer length = Integer.parseInt(message.substring(0, 4));
        List<String> tokens = Matching.getTokens(message);
        if (tokens.get(1).equals("JOIN")) {
            this.node = new Node(tokens.get(2), Integer.parseInt(tokens.get(3)));
            this.key = new Key(tokens.get(4));
            this.hasRootSeen = Integer.parseInt(tokens.get(5)) == 1;
        }
    }

    @Override
    public String getMessage() {
        DecimalFormat nf = new DecimalFormat("0000");
        StringBuilder sb = new StringBuilder();
        sb.append(" JOIN ").append(this.node.getIp()).append(" ").append(this.node.getPort()).append(" ").append(this.key.toString()).append(" ").append(hasRootSeen ? "1" : "0");
        return nf.format(sb.toString().length() + 4) + sb.toString();
    }

    @Override
    public void process(Node incomingNode) throws IOException {
        /*if (key.equals(super.getMyKey())) {
            System.out.println("Duplicate Key " + super.getMyKey().toString() + " " + key);
            Communication.send(new cs658.project.binarytreedht.messages.Error().getMessage(), super.getSocket(), node);
            return;
        }*/

        /*if (super.getLeftKey() != null && key.equals(super.getLeftKey())) {
            System.out.println("Duplicate Key Left");
            Communication.send(new cs658.project.binarytreedht.messages.Error().getMessage(), super.getSocket(), node);
            return;
        }

        if (super.getRightKey() != null && key.equals(super.getRightKey())) {
            System.out.println("Duplicate Key Right");
            Communication.send(new cs658.project.binarytreedht.messages.Error().getMessage(), super.getSocket(), node);
            return;
        }*/

        if (super.isRoot()) {
            this.hasRootSeen = true;
        }

        if (hasRootSeen) {
            Integer route = super.getRoute(key);
            if (route > 0) {
                if (super.getRightKey() == null) {
                    super.setRight(node, key);
                    JoinResponse joinResponse = new JoinResponse(super.getMyNode(), super.getMyKey(), super.getRootNode(), super.getRootKey());
                    Communication.send(joinResponse.getMessage(), super.getSocket(), node);
                    super.setRightKeepAlive(new KeepAliveThread(super.getRightNode(), super.getRightKey()));
                } else {
                    Communication.send(this.getMessage(), super.getSocket(), super.getRightNode());
                }
            } else if (route < 0) {
                if (super.getLeftKey() == null) {
                    super.setLeft(node, key);
                    JoinResponse joinResponse = new JoinResponse(super.getMyNode(), super.getMyKey(), super.getRootNode(), super.getRootKey());
                    Communication.send(joinResponse.getMessage(), super.getSocket(), node);
                    super.setLeftKeepAlive(new KeepAliveThread(super.getLeftNode(), super.getLeftKey()));
                } else {
                    Communication.send(this.getMessage(), super.getSocket(), super.getLeftNode());
                }
            } else {
                System.out.println("Error");
                Communication.send(new cs658.project.binarytreedht.messages.Error().getMessage(), super.getSocket(), node);
            }
        } else {
            Communication.send(this.getMessage(), super.getSocket(), super.getRootNode());
        }
    }
}
