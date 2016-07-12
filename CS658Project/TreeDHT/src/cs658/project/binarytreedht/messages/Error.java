package cs658.project.binarytreedht.messages;

import cs658.project.aux.primitives.Node;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by iyro on 4/26/16.
 */
public class Error extends MessageObject {
    @Override
    public String getMessage() {
        DecimalFormat nf = new DecimalFormat("0000");

        StringBuilder sb = new StringBuilder();
        sb.append(" ERROR");
        return nf.format(sb.toString().length() + 4) + sb.toString();
    }

    @Override
    public void process(Node incomingNode) throws IOException {
        super.getLogger().severe("Error. Quitting.");
        //TODO Send Delete to Bootstrap & MAKE new KeyConflict Class
        System.exit(1);
    }
}
