package cs658.project.binarytreedht.messages;

import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Communication;
import cs658.project.aux.utilities.Matching;
import cs658.project.binarytreedht.protocol.ContentThread;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by iyro on 4/25/16.
 */
public class BootstrapResponse extends MessageObject {
    private boolean isNotFirst;
    private Node node;

    public BootstrapResponse(Node node) {
        this.node = node;
        this.isNotFirst = true;
    }

    public BootstrapResponse() {
        this.isNotFirst = false;
    }

    public BootstrapResponse(String message) {
        Integer length = Integer.parseInt(message.substring(0, 4));
        List<String> tokens = Matching.getTokens(message);
        if (tokens.get(1).equals("BOOTSTRAPOK")) {
            this.isNotFirst = (Integer.parseInt(tokens.get(2)) == 1);

            if (isNotFirst) {
                this.node = new Node(tokens.get(3), Integer.parseInt(tokens.get(4)));
            }
        }
    }

    public boolean isNotFirst() {
        return isNotFirst;
    }

    public Node getNode() {
        return node;
    }

    public String getMessage() {
        DecimalFormat nf = new DecimalFormat("0000");
        StringBuilder sb = new StringBuilder();
        sb.append(" BOOTSTRAPOK ").append(isNotFirst ? "1 " : "0 ");
        if (isNotFirst) {
            sb.append(this.node.getIp()).append(" ").append(this.node.getPort());
        }
        return nf.format(sb.toString().length() + 4) + sb.toString();
    }

    @Override
    public void process(Node incomingNode) throws IOException {
        if (isNotFirst) {
            Communication.send(new JoinRequest(super.getMyNode(), super.getMyKey(), false).getMessage(), super.getSocket(), node);
        } else {
            super.setRoot(super.getMyNode(), super.getMyKey());
        }

        super.getLogger().info("I am Root : " + super.isRoot());
    }
}
