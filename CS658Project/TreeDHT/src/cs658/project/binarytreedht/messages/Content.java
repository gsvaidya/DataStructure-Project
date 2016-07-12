package cs658.project.binarytreedht.messages;

import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Communication;

import java.io.IOException;

/**
 * Created by iyro on 4/26/16.
 */
public class Content extends MessageObject {
    private String message;

    public Content(String message) {
        this.message = message.trim();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void process(Node incomingNode) throws IOException {
        System.out.println(getContent());
        if (super.getLeftNode()!=null)
            Communication.send(message, super.getSocket(), super.getLeftNode());
        if (super.getRightNode()!=null)
            Communication.send(message, super.getSocket(), super.getRightNode());
    }

    private String getContent() {
        return this.message.substring(5 + "CONTENT ".length());
    }
}
