import java.io.*;
import java.net.*;
import java.util.*;

public class Download extends Thread {
    private FileManager fileManager;
    private ConnectionManager connectionManager;
    private String tuple;

    public Download(FileManager fm, ConnectionManager cm, String str){
        fileManager = fm;
        connectionManager = cm;
        tuple = str;
    }

    @Override
    public void run() {
        for(;;) {
            int cnt = 0, flag = 0;
            try {
                Socket server;
                while (true) {
                    try {
                        String[] arg = tuple.split(" ");
                        server = new Socket(arg[0], Integer.parseInt(arg[1]));
                        server.setSoTimeout(10000);
                        System.out.println("DOWNLOAD : connection with " + tuple);
                        break;
                    } catch (Exception e) {
                        tuple = connectionManager.getAnotherPeer(tuple);
                    }
                }
                InputStream in = server.getInputStream();
                OutputStream out = server.getOutputStream();

                DataOutputStream toServer = new DataOutputStream(out);
                DataInputStream fromServer = new DataInputStream(in);
                String message = fromServer.readUTF();
                ArrayList<Integer>myChunkList= fileManager.getChunklist();
                ArrayList<Integer>serverChunkList = new ArrayList<Integer>();
                if(message.equals("BitMap")){
                    while (true) {
                        int id = fromServer.readInt();
                        if (id == -1) {
                            break;
                        }
                        serverChunkList.add(id);
                    }
                }
                toServer.writeUTF("Request");
                for (int i = 0; i < serverChunkList.size();i++) {
                    Integer integer = serverChunkList.get(i);
                    if (!myChunkList.contains(integer)) {
                        toServer.writeInt(integer);
                        byte[] chunk = new byte[10240];
                        fromServer.read(chunk);
                        fileManager.writeChunk(integer, chunk);
                        System.out.println("DOWNLOAD : download chunk " + integer);
                        cnt++;
                    }
                    if (cnt == 3) {
                        break;
                    }
                }
                toServer.writeInt(-1);
                Thread.sleep(5000);
                System.out.println("DOWNLOAD : wait for 5 seconds");
                if(cnt==0){
                    System.out.println("DOWNLOAD : no chunk to download");
                    flag++;
                }else{
                    flag = 0;
                }
                if(flag==2){
                    System.out.println("DOWNLOAD : change friend");
                    tuple = connectionManager.getAnotherPeer(tuple);
                    flag = 0;
                }
                fromServer.close();
                toServer.close();
                server.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
