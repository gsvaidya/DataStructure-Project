package cs658.project.binarytreedht.messages;

import cs658.project.aux.primitives.Key;
import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Matching;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by iyro on 4/25/16.
 */
public class JoinResponse extends MessageObject {
    private Node node;
    private Key key;

    private Node root;
    private Key rootKey;

    public JoinResponse(String message) {
        Integer length = Integer.parseInt(message.substring(0, 4));
        List<String> tokens = Matching.getTokens(message);
        if (tokens.get(1).equals("JOINOK")) {
            this.node = new Node(tokens.get(2), Integer.parseInt(tokens.get(3)));
            this.key = new Key(tokens.get(4));
            this.root = new Node(tokens.get(5), Integer.parseInt(tokens.get(6)));
            this.rootKey = new Key(tokens.get(7));
        }
    }

    public JoinResponse(Node node, Key key, Node root, Key rootKey) {
        this.node = node;
        this.key = key;
        this.root = root;
        this.rootKey = rootKey;
    }

    @Override
    public String getMessage() {
        DecimalFormat nf = new DecimalFormat("0000");
        StringBuilder sb = new StringBuilder();
        sb.append(" JOINOK ").append(this.node.getIp()).append(" ").append(this.node.getPort()).append(" ").append(this.key.toString());
        sb.append(" ").append(root.getIp()).append(" ").append(root.getPort()).append(" ").append(rootKey.toString());
        return nf.format(sb.toString().length() + 4) + sb.toString();
    }

    @Override
    public void process(Node incomingNode) throws IOException {
        super.setParent(node, key);
        super.setRoot(root, rootKey);
    }
}
