import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class Proposer implements Runnable{

    int myproid;
    ArrayList<String> allServers;

    public Proposer() {
        this.myproid = 0;
    }

    public void updateAllServers(ArrayList<String> allServers) {
        this.allServers = allServers;
    }

    public synchronized String propose(String key, String value, int action) {

        String response = "";
        try {

            //prepare
            log("***************** Started Paxos proposal functionality *******************");
            myproid++; int count =0;
            log("Sending prepare requests to all acceptors");
            for(int i=0; i<allServers.size(); i++){
                try {
                    //InetAddress ip = InetAddress.getLocalHost();
                    Registry registry = LocateRegistry.getRegistry(Integer.parseInt(allServers.get(i)));
                    ServerInterface server = (ServerInterface) registry.lookup("ServerInterface");
                    if(server.prepare(myproid, key, value, action)){
                        count ++;
                    }
                }
                catch (SocketTimeoutException se){
                    continue;
                }
                catch(Exception e){
                    continue;
                }
            }
            log("Servers accepted prepare requests are " + count);

            //accept only if server positive response of more than half
            if(count > allServers.size()/2) {
                count = 0;
                log("Sending accept requests to all acceptors");
                for(int i=0; i<allServers.size(); i++){
                    try {
                        //InetAddress ip = InetAddress.getLocalHost();
                        Registry registry = LocateRegistry.getRegistry(Integer.parseInt(allServers.get(i)));
                        ServerInterface server = (ServerInterface) registry.lookup("ServerInterface");
                        if(server.accept(myproid, key, value, action)){
                            count ++;
                        }
                    }
                    catch (SocketTimeoutException se){
                        continue;
                    }
                    catch (Exception e) {
                        continue;
                    }
                }
                log("Servers accepted accept requests are " + count);
            }
            else {
                response = "Consensus could not be reached as only " + count +
                        "servers replied to the prepare request";
                log(response);
                return response;
            }

            //commit only if server positive response of more than half
            if(count > allServers.size()/2) {
                count = 0;
                log("Sending commit requests to all Learners");
                for(int i=0; i<allServers.size(); i++){
                    try {
                        //InetAddress ip = InetAddress.getLocalHost();
                        Registry registry = LocateRegistry.getRegistry( Integer.parseInt(allServers.get(i)));
                        ServerInterface server = (ServerInterface) registry.lookup("ServerInterface");
                        if(response.equals("")){
                            response = server.commit(key, value, action);
                        }
                        server.commit(key, value, action);
                        server.setProposerProposalId(myproid);
                    }
                    catch (Exception e) {
                        continue;
                    }
                }
                //response = "Successfully committed values to all servers";
            }
            else {
                response = "Consensus could not be reached as only " + count +
                        "servers replied to the accept request";
                log(response);
                return response;
            }
            log("****************** Completed Paxos proposal functionality **************************");

        }

        catch(Exception e){
            System.out.println("Remote Exception" + e.getMessage());
        }

        return response;
    }

    public int getmyproid() {
        return myproid;
    }

    public void setmyproid(int id) {
        myproid = id;
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



