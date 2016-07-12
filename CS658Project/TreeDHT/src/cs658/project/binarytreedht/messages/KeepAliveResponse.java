package cs658.project.binarytreedht.messages;

import cs658.project.aux.primitives.Key;
import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Matching;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by iyro on 4/25/16.
 */
public class KeepAliveResponse extends MessageObject {
    //<len> ALIVEOK <Key>

    private Key key;

    public KeepAliveResponse(Key key) {
        this.key = key;
    }

    public KeepAliveResponse(String message) {
        Integer length = Integer.parseInt(message.substring(0, 4));

        List<String> tokens = Matching.getTokens(message);
        if (tokens.get(1).equals("ALIVEOK")) {
            this.key = new Key(tokens.get(2));
        }
    }


    @Override
    public void process(Node incomingNode) {
        if (super.getLeftKey() != null && super.getLeftKey().equals(key)) {
            if (super.getLeftKeepAlive() != null)
                super.getLeftKeepAlive().setPrevResponse(true);
        }
        if (super.getRightKey() != null && super.getRightKey().equals(key)) {
            if (super.getRightKeepAlive() != null)
                super.getRightKeepAlive().setPrevResponse(true);
        }
    }

    @Override
    public String getMessage() {
        DecimalFormat nf = new DecimalFormat("0000");
        StringBuilder sb = new StringBuilder();
        sb.append(" ALIVEOK ").append(this.key.toString());
        return nf.format(sb.toString().length() + 4) + sb.toString();
    }
}
