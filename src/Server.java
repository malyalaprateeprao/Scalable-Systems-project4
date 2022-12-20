import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.rmi.server.RemoteServer.getClientHost;

public class Server implements ServerInterface {


    Proposer pro = null;
    Acceptor accept = null;
    Learner learn = null;

    ArrayList<String> allServers;
    String thisServer;


    public Server(String thisServer) {
        log("New Server Instance created");
        HashStorage storage = new HashStorage();
        pro = new Proposer();
        accept = new Acceptor(thisServer);
        learn = new Learner(storage);
    }

    public void updateAllServers(ArrayList<String> allServers) {
        this.allServers = allServers;
        pro.updateAllServers(allServers);
        accept.updateAllServers(allServers);
    }

    public String get(String key) throws ServerNotActiveException {
        log("Received a get request to the server from client for a key = " + key + " from Client host " + getClientHost());
        return this.pro.propose(key, "", 1);
    }

    public String put(String key, String value) throws ServerNotActiveException {
        log("Received a put request to the server from client  with key = " + key + " and value = " + value + " from Client host " + getClientHost());
        return this.pro.propose(key, value,  2);
    }

    public String delete(String key) throws ServerNotActiveException {
        log("Received a delete request to the server from client for a key = " + key + " from Client host " + getClientHost());
        return this.pro.propose(key, "", 3);
    }

    public boolean prepare(int id, String key, String value, int action) throws InterruptedException, RemoteException, SocketTimeoutException {
        return accept.prepare(id, key, value, action);
    }

    public boolean accept(int id, String key, String value, int action) throws InterruptedException, RemoteException, SocketTimeoutException {
        return accept.accept(id, key, value, action);
    }

    public String commit(String key, String value, int action) throws RemoteException {
        return learn.commit(key, value, action);
    }

    public int getProposerProposalId() {
        return pro.getmyproid();
    }

    public void setProposerProposalId(int id) {
        pro.setmyproid(id);
    }

    public static String getCurrentTimeStamp()
    {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date());
    }

    public static void log(String msg){
        System.out.println(getCurrentTimeStamp() +  " : " + msg);}

}






