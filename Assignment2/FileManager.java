import java.io.*;
import java.util.*;

public class FileManager {
    private RandomAccessFile raf;
    private ArrayList<Integer>chunklist;

    public FileManager(String pathname)throws Exception{
        raf = new RandomAccessFile(pathname, "rw");
        chunklist = new ArrayList<Integer>();
    }

    public synchronized void writeChunk(int chunkId, byte[] chunk)throws Exception{
        if(chunklist.contains(chunkId)){
            return;
        }
        chunklist.add(chunkId);
        chunklist.sort(null);

        raf.seek(0);
        raf.seek(10240*chunkId);

        raf.write(chunk);
    }

    public synchronized byte[] readChunk(int chunkId)throws Exception{
        raf.seek(0);
        raf.seek(10240*chunkId);

        byte[] res = new byte[10240];
        raf.read(res);

        return res;
    }
    public synchronized ArrayList<Integer> getChunklist(){
        ArrayList<Integer> chunklist = this.chunklist;
        return chunklist;
    }
    public synchronized void readAllChunk()throws Exception{
        byte[] chunk = new byte[10240];
        raf.seek(0);
        int idx = 0;
        while(raf.read(chunk)!=-1){
            chunklist.add(idx);
            idx++;
        }
    }
}
