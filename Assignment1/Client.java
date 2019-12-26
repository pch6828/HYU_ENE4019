import java.io.*;
import java.net.*;

public class Client {
    public static void main(String argv[])throws Exception{
        //read configuration file
        BufferedReader br = new BufferedReader(new FileReader("config_client.txt"));
        //server ip (not client ip)
        String ip = br.readLine();
        //server port (not client port)
        int port = Integer.parseInt(br.readLine());
        //pathname that will be store transferred file
        String pathname = br.readLine();
        System.out.println("CLIENT : read config file");
        receive(ip,port, pathname);
    }
    public static void receive(String ip, int port, String pathname)throws IOException{
        try {
            //TCP connection between server
            Socket clientSocket = new Socket(ip, port);
            System.out.println("CLIENT : create socket");

            BufferedInputStream fromserver = new BufferedInputStream(clientSocket.getInputStream());
            BufferedOutputStream writeclient = new BufferedOutputStream(new FileOutputStream(pathname));

            byte[] buffer = new byte[10240];
            int off = 0;
            //download chunks
            while((off = fromserver.read(buffer))!=-1){
                //write chunk
                writeclient.write(buffer, 0, off);
                System.out.println("CLIENT : downloaded "+off+"bytes");
            }
            System.out.println("CLIENT : download file");
            writeclient.close();
            fromserver.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
