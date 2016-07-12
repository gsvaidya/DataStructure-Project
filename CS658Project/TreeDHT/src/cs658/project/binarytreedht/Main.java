package cs658.project.binarytreedht;

import cs658.project.aux.messages.BootstrapRequest;
import cs658.project.aux.primitives.Key;
import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Communication;
import cs658.project.aux.utilities.LocalIPAddress;
import cs658.project.aux.utilities.LoggingUtility;
import cs658.project.binarytreedht.messages.MessageObject;
import cs658.project.binarytreedht.protocol.ContentThread;
import cs658.project.binarytreedht.protocol.KeepAliveThread;
import cs658.project.binarytreedht.protocol.MessageProcessor;
import cs658.project.binarytreedht.routing.TreeNode;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.logging.Logger;

public class Main {
    private static Logger logger;

    private static Integer protocolPort = 0;
    private static DatagramSocket protocolSocket;
    private static Node bootstrapNode;
    private static TreeNode myNode;
    private static Long IDENTIFIER_SPACE_SIZE = 32L;

    private static MessageProcessor messageProcessor;

    public static void main(String[] args) {
        initializeLogging();
        Key.setIdentifierSpaceSize(IDENTIFIER_SPACE_SIZE);
        bootstrapNode = new Node(args[0], Integer.parseInt(args[1]));
        try {
            protocolSocket = new DatagramSocket(protocolPort);
            protocolSocket.setSoTimeout(5000);
            protocolPort = protocolSocket.getLocalPort();
            myNode = new TreeNode(new Node(LocalIPAddress.get(), protocolPort), protocolSocket);
            initialize();

            messageProcessor = new MessageProcessor(myNode, logger);
            new Thread(messageProcessor).start();

            Communication.send(new BootstrapRequest(myNode.getMyNode(), myNode.getMyKey()).getMessage(), protocolSocket, bootstrapNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initialize() {
        MessageObject.initialize(myNode);
        KeepAliveThread.initialize(myNode);
        ContentThread.initialize(myNode);
    }

    private static void initializeLogging() {
        logger = LoggingUtility.getLogger("test");
        Communication.setLogger(logger);
        MessageObject.setLogger(logger);
    }
}
