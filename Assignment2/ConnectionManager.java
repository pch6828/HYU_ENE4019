public class ConnectionManager {
    private String[] tuples;
    private boolean[] isConnected;
    private int idx = 0;

    public ConnectionManager(String[] tuples){
        this.tuples = new String[tuples.length];
        this.isConnected = new boolean[tuples.length];
        for(int i = 0; i < tuples.length; i++){
            this.tuples[i] = tuples[i];
            this.isConnected[i] = false;
        }
    }

    public synchronized String getPeer(){
        String res = "";
        for(int i = 0; i < tuples.length; i++){
            if(isConnected[i]==false){
                isConnected[i] = true;
                res = tuples[i];
                idx = i;
                break;
            }
        }
        System.out.println("CONNECTION MANAGER : NOW -"+res);
        return res;
    }

    public synchronized String getAnotherPeer(String tuple){
        String res = "";
        for(int i = 0; i < tuples.length; i++){
            if(tuples[i].equals(tuple)){
                isConnected[i] = false;
            }
        }
        while(true){
            idx++;
            idx%=tuples.length;
            if(isConnected[idx]==false){
                res = tuples[idx];
                isConnected[idx] = true;
                break;
            }
        }
        System.out.println("CONNECTION MANAGER : PREV -"+tuple);
        System.out.println("CONNECTION MANAGER : NEXT -"+res);
        return res;
    }
}
