import java.io.*;
import java.net.*;
import java.util.*;

public class Upload extends Thread {
    private FileManager fileManager;
    private ServerSocket welcomeSocket;

    public Upload(FileManager fm, ServerSocket ss){
        fileManager = fm;
        welcomeSocket = ss;
    }

    @Override
    public void run() {
        for(;;){
            try {
                Socket client = welcomeSocket.accept();
                System.out.println("UPLOAD : Connected "+client.getInetAddress()+" "+client.getPort());
                OutputStream out = client.getOutputStream();
                InputStream in = client.getInputStream();
                DataInputStream fromClient = new DataInputStream(in);
                DataOutputStream toClient = new DataOutputStream(out);

                ArrayList<Integer>myChunkList = fileManager.getChunklist();

                toClient.writeUTF("BitMap");
                for (int i = 0; i < myChunkList.size();i++) {
                    Integer integer= myChunkList.get(i);
                    toClient.writeInt(integer);
                }
                toClient.writeInt(-1);
                String message = fromClient.readUTF();
                int cnt = 0;
                if(message.equals("Request")){
                    while(true){
                        int id = fromClient.readInt();
                        if(id == -1){
                            break;
                        }
                        System.out.println("UPLOAD : requested chunk "+id);
                        toClient.write(fileManager.readChunk(id));
                        System.out.println("UPLOAD : send chunk "+id);
                        cnt++;
                    }
                }

                if(cnt==0){
                    System.out.println("UPLOAD : no chunk to send");
                }
                toClient.writeInt(-1);

                fromClient.close();
                toClient.close();
                client.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
