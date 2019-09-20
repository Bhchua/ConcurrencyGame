package network.threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import error.GlobalErrors;
import game.Core;
import game.model.ShooterGame;
import game.screens.menus.MainMenuScreen;
import network.GameNetworkData;

/**
 * The NetworkHostThread method opens a connection to a client and
 * allows strings to be sent back and fourth between a socket.
 * 
 * @author Brandon Hua
 */

public class NetworkHostThread extends NetworkThread {

  /**
   * The NetworkHost thread initialises a server socket.
   * 
   * @param port The port number to host the server on.
   * 
   * @throws IOException If the server cannot be opened.
   */
  
  public NetworkHostThread(int port) throws IOException {
    super("localhost", port);
    serverSocket = new ServerSocket(port);
    serverSocket.setSoTimeout(60000);
  }
  
  /**
   * A method that accepts a connection and initialises the input/output data streams.
   */
  
  @Override
  public void connect() {
    try {
      System.out.println("Hosting on IP: " + serverSocket.getLocalSocketAddress() + "...");
      socket = serverSocket.accept();
      System.out.println("Connected to " + socket.getRemoteSocketAddress());
      in = new DataInputStream(socket.getInputStream());
      out = new DataOutputStream(socket.getOutputStream());
      connected = true;
    } catch (IOException e) {
      e.printStackTrace();
      throwError(e);
    }
  }
  
  

  

 

}
