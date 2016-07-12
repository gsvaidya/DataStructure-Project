package cs658.project.binarytreedht.messages;

import cs658.project.aux.primitives.Key;
import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Communication;
import cs658.project.aux.utilities.Matching;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by iyro on 4/25/16.
 */
public class SearchRequest extends MessageObject {
    private Key key;

    public SearchRequest(String message) {
        Integer length = Integer.parseInt(message.substring(0, 4));
        List<String> tokens = Matching.getTokens(message);
        if (tokens.get(1).equals("SEARCH")) {
            this.key = new Key(tokens.get(2));
        }
    }

    public SearchRequest(Key key) {
        this.key = key;
    }

    @Override
    public String getMessage() {
        DecimalFormat nf = new DecimalFormat("0000");
        StringBuilder sb = new StringBuilder();
        sb.append(" SEARCH ").append(this.key.toString());
        return nf.format(sb.toString().length() + 4) + sb.toString();
    }

    @Override
    public void process(Node incomingNode) throws IOException {
        if (super.getMyKey().belongsLeft(key)) {
            if (super.getLeftKey() == null) {
                Communication.send(new SearchResponse(super.getMyKey()).getMessage(), super.getSocket(), incomingNode);
            } else {
                Communication.send(this.getMessage(), super.getSocket(), super.getLeftNode());
            }
        } else {
            if (super.getRightKey() == null) {
                Communication.send(new SearchResponse(super.getMyKey()).getMessage(), super.getSocket(), incomingNode);
            } else {
                Communication.send(this.getMessage(), super.getSocket(), super.getRightNode());
            }
        }
    }
}
