import java.io.*;
import java.net.*;

public class Server {
    public static void main(String argv[])throws Exception{
        //read configuration file
        BufferedReader br = new BufferedReader(new FileReader("config_server.txt"));

        //server ip
        String ip = br.readLine();
        //server's welcoming socket port number
        int port = Integer.parseInt(br.readLine());
        //pathname of file that will be transferred
        String pathname = br.readLine();
        System.out.println("SERVER : read config file");
        serve(ip, port, pathname);
    }

    public static void serve(String ip, int port, String pathname)throws IOException{
        //creating welcoming socket
        final ServerSocket welcomeSocket = new ServerSocket(port);
        System.out.println("SERVER : create welcoming socket");
        try {
            //server is always working because of client's connection can be appear anytime
            for (;;) {
                //TCP connection
                Socket clientSocket = welcomeSocket.accept();
                System.out.println("SERVER : create clientsocket");
                //read file that will be transferred
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pathname));

                byte[] buffer = new byte[10240];
                int off = 0;

                //client's outputstream
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

                //read 10KB chunk
                while ((off = bis.read(buffer)) != -1) {
                    //transfer chunk
                    outToClient.write(buffer, 0, off);
                    System.out.println("SERVER : transfered "+off+"bytes");
                }
                bis.close();
                outToClient.close();
                clientSocket.close();
                System.out.println("SERVER : send all file");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
