package network.threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import error.GlobalErrors;
import network.GameNetworkData;

/**
 * The NetworkClientThread connects to a host and sends and receives strings
 * from the connection.
 * 
 * @author Brandon Hua
 *
 */

public class NetworkClientThread extends NetworkThread {

  public NetworkClientThread(String ip, int port) {
    super(ip, port);
  }

  /**
   * A method to initialise the socket and input/output data streams.
   */

  @Override
  public void connect() {
    try {
      System.out.println("Connecting to " + ip + ":" + port);
      socket = new Socket(ip, port);
      System.out.println("Connected to " + socket.getRemoteSocketAddress());
      in = new DataInputStream(socket.getInputStream());
      out = new DataOutputStream(socket.getOutputStream());
      connected = true;
    } catch (Exception e) {
      e.printStackTrace();
      throwError(e);
    }
  }

}
