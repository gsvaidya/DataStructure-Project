package cs658.project.bootstrap;

import cs658.project.aux.messages.BootstrapAnswer;
import cs658.project.aux.messages.BootstrapRequest;
import cs658.project.aux.messages.KeyConflict;
import cs658.project.aux.primitives.Key;
import cs658.project.aux.primitives.Node;
import cs658.project.aux.utilities.Communication;
import cs658.project.aux.utilities.LocalIPAddress;
import cs658.project.aux.utilities.LoggingUtility;
import cs658.project.aux.utilities.Matching;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;

/**
 * Created by iyro on 4/25/16.
 */
public class BootstrapServer {
    private static ConcurrentSkipListMap<Key, Node> nodesInTheSystem;
    private static Integer bootstrapPort;
    private static DatagramSocket socket;
    private static Logger logger;

    public static void main(String[] args) {
        initializeLogging();
        bootstrapPort = 10000;

        try {
            nodesInTheSystem = new ConcurrentSkipListMap<Key, Node>();

            socket = new DatagramSocket(bootstrapPort);
            bootstrapPort = socket.getLocalPort();

            Node localNode = new Node(LocalIPAddress.get(), bootstrapPort);
            System.out.println("Bootstrapper Listening at " + localNode.toString());

            while (socket.isBound()) {
                try {
                    DatagramPacket datagramPacket = Communication.receive(socket);

                    Node incomingNode = new Node(datagramPacket);
                    String message = new String(datagramPacket.getData()).trim();

                    Integer length = Integer.parseInt(message.substring(0, 4));

                    List<String> tokens = Matching.getTokens(message);

                    if (tokens.get(1).equals("BOOTSTRAP")) {
                        BootstrapRequest bootstrapRequest = new BootstrapRequest(message);

                        BootstrapAnswer bootstrapAnswer;

                        if (nodesInTheSystem.isEmpty()) {
                            bootstrapAnswer = new BootstrapAnswer();
                            Communication.send(bootstrapAnswer.getMessage(), socket, bootstrapRequest.getNode());
                            nodesInTheSystem.put(bootstrapRequest.getKey(), bootstrapRequest.getNode());
                        } else {
                            ArrayList<Key> keys = new ArrayList<Key>(nodesInTheSystem.keySet());

                            if (keys.contains(bootstrapRequest.getKey())) {
                                System.out.println("Duplicate Key");
                                Communication.send(KeyConflict.getMessage(), socket, bootstrapRequest.getNode());
                            } else {
                                Collections.shuffle(keys);
                                bootstrapAnswer = new BootstrapAnswer(nodesInTheSystem.get(keys.get(0)));
                                Communication.send(bootstrapAnswer.getMessage(), socket, bootstrapRequest.getNode());
                                nodesInTheSystem.put(bootstrapRequest.getKey(), bootstrapRequest.getNode());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    private static void initializeLogging() {
        logger = LoggingUtility.getLogger("test");
        Communication.setLogger(logger);
    }
}
