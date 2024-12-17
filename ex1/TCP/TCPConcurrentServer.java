import java.io.*;
import java.net.*;

public class TCPConcurrentServer {
    public static void main(String[] args) {
        int port = 1667;
        System.out.println("Server is waiting for client connection at port number " + port);
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
             
            while (true) {
                out.println("Enter number 1 (to end just press enter):");
                String input1 = in.readLine();
                if (input1 == null || input1.isEmpty()) {
                    out.println("Connection closed.");
                    break;
                }
                
                try {
                    int num1 = Integer.parseInt(input1);
                    
                    out.println("Enter number 2 (to end just press enter):");
                    String input2 = in.readLine();
                    if (input2 == null || input2.isEmpty()) {
                        out.println("Connection closed.");
                        break;
                    }
                    
                    int num2 = Integer.parseInt(input2);
                    
                    int result = num1 + num2;
                    out.println("The result is " + result);
                } catch (NumberFormatException e) {
                    out.println("Invalid input. Please enter a valid number.");
                }
            }
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
