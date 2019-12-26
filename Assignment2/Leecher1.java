import java.io.BufferedReader;
import java.io.FileReader;
import java.net.ServerSocket;

public class Leecher1 {
    private static FileManager fileManager;
    private static ConnectionManager connectionManager;
    private static Upload upload;
    private static Download download1, download2, download3;

    public static void main(String[] args)throws Exception{
        BufferedReader configReader = new BufferedReader(new FileReader("config_leecher1.txt"));
        String[] tuples = new String[4];
        String mytuple = configReader.readLine();

        for(int i = 0; i < 4; i++){
            tuples[i] = configReader.readLine();
        }
        String pathname = configReader.readLine();
        System.out.println("LEECHER : Read All Config File");
        fileManager = new FileManager(pathname);
        connectionManager = new ConnectionManager(tuples);

        ServerSocket welcomesocket = new ServerSocket(Integer.parseInt(mytuple.split(" ")[1]));
        upload = new Upload(fileManager, welcomesocket);
        download1 = new Download(fileManager, connectionManager, connectionManager.getPeer());
        download2 = new Download(fileManager, connectionManager, connectionManager.getPeer());
        download3 = new Download(fileManager, connectionManager, connectionManager.getPeer());

        upload.start();
        download1.start();
        download2.start();
        download3.start();
    }
}
