import java.io.*;
import java.net.*;

public class Seeder {
    private static FileManager fileManager;
    private static ConnectionManager connectionManager;
    private static Upload upload;

    public static void main(String args[])throws Exception {
        BufferedReader configReader = new BufferedReader(new FileReader("config_seeder.txt"));
        String[] tuples = new String[4];
        String mytuple = configReader.readLine();
        for (int i = 0; i < 4; i++) {
            tuples[i] = configReader.readLine();
        }
        String pathname = configReader.readLine();
        System.out.println("SEEDER : Read All Config File");
        fileManager = new FileManager(pathname);
        fileManager.readAllChunk();
        connectionManager = new ConnectionManager(tuples);
        ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(mytuple.split(" ")[1]));
        upload = new Upload(fileManager, welcomeSocket);

        upload.start();
    }

}
