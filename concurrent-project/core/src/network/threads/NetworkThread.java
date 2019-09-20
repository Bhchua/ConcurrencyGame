package network.threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import error.GlobalErrors;

public class NetworkThread extends Thread {

  protected ServerSocket serverSocket;
  protected Socket socket;
  protected DataInputStream in;
  protected DataOutputStream out;
  protected boolean connected = false;
  protected boolean done = false;
  protected String inputString;
  protected String outputString;
  protected String ip;
  protected int port;

  /**
   * The NetworkThread initialises the ip and port number of a connection.
   * 
   * @param ip   The domain of the connection.
   * @param port The port of the connection.
   */
  public NetworkThread(String ip, int port) {
    this.ip = ip;
    this.port = port;
    outputString = "ready";
  }

  /**
   * The run method first connects to a host/client and then sends/reads strings
   * sent with the socket.
   */
  public void run() {
    connect();
    while (!done) {
      try {
        sendString();
        readInput();
        // System.out.println("Client: " + inputString);
      } catch (Exception e) {
        throwError(e);
      }
    }
  }

  /**
   * A method to send strings using the output data stream.
   * 
   * @throws IOException If the out DataStream cannot send the string;
   */

  public void sendString() throws IOException {
    out.writeUTF(outputString);
  }

  /**
   * A method to set the local inputString variable.
   */

  public void readInput() {
    try {
      inputString = in.readUTF();
    } catch (Exception e) {
      throwError(e);
    }
  }

  /**
   * A method to close the socket when it's no longer needed.
   */

  public void stopConnection() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      throwError(e);
    }
  }

  public boolean isConnected() {
    return connected;
  }

  public void finish() {
    done = true;
    stopConnection();
  }

  public String getInputString() {
    return inputString;
  }

  public void setOutputString(String out) {
    outputString = out;
  }

  /**
   * A method that stops the thread and sets the global error message.
   * 
   * @param e The Exception thrown.
   */

  public void throwError(Exception e) {
    finish();
    String error = e.getClass().getSimpleName() + ": " + e.getMessage();
    GlobalErrors.setError(error);
  }

  /**
   * A method that connects to the host/client.
   */
  public void connect() {
    // stub class, must be overwritten in the child class.
  }

}
