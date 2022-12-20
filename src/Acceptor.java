import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Acceptor implements Runnable{

    int myOldProposalId = Integer.MIN_VALUE;
    ArrayList<String> allServers;
    String thisServer;


    public Acceptor(String thisServer) {
        this.thisServer = thisServer;

    }

    public void updateAllServers(ArrayList<String> allServers) {
        this.allServers = allServers;
    }

    public boolean prepare(int id, String key, String value, int action) throws InterruptedException, RemoteException, SocketTimeoutException {
        log("Received a prepare request to the Paxos Acceptor");
        return conditionCheck(id, key, value, action);
    }

    public boolean accept(int id, String key, String value, int action) throws InterruptedException, RemoteException, SocketTimeoutException{
        log("Received a accept request to the Paxos Acceptor");
        return conditionCheck(id, key, value, action);
    }

    public boolean conditionCheck(int id, String key, String value, int action) throws InterruptedException, RemoteException, SocketTimeoutException{

        /*
         to sleep a random server, I randomly selected a no from count of servers and if the no is same as this server than we can
         make it sleep.
         */

        Random rand = new Random();
        // Generate random integers in range 0 to no of servers
        int randomNumber = rand.nextInt(allServers.size());
        if (randomNumber <allServers.size() && allServers.get(randomNumber).equals(thisServer)) {

            log("Thread going into sleep for 100000 ms, it is part of the project to kill or pass a thread for sometime and resume it from their");
            throw new SocketTimeoutException(" thread slept or killing it");

            // here instead of throwing exception we can use thread sleep or wait but i used process only instead of threads at last minute
        }

        if(myOldProposalId > id) {
            return false;
        }
        else {
            this.myOldProposalId = id;
            return true;
        }

    }

    public void run() {

    }

    public static String getCurrentTimeStamp()
    {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date());
    }

    public static void log(String msg){
        System.out.println(getCurrentTimeStamp() +  " : " + msg);}
}
