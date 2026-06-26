import java.io.*;
import java.net.*;

/**
 * Main class: server whoch listens for incoming telnet onnections
 * and creates a new ClientHandler for each connection
 */

public class Server {
    private static final int PORT = 8080;
    private static final Appointmentdatabase database = new Appointmentdatabase();

    public static void main(String[] args) {
        
        System.out.println("Appointment Database Server is running on port " + PORT);
        System.out.println("Loading appoinments..");
        System.out.println("Listening on port " + PORT);
        FileManager.loadAppointments(database);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                    ClientHandler handler = new ClientHandler(clientSocket, database);
                    Thread clienThread = new Thread(handler);
                    clienThread.start();
                } catch (IOException e) {
                    System.err.println("Error with the client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }

    public static synchronized void saveDatabase() {
        FileManager.saveAppointments(database);
    }
}