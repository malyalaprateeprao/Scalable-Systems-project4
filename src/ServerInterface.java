import java.net.SocketTimeoutException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {


    String get(String key) throws RemoteException, ServerNotActiveException;

    String put(String key, String value) throws RemoteException, ServerNotActiveException;

    String delete(String key) throws RemoteException, ServerNotActiveException;

    boolean prepare(int id, String key, String value, int action) throws InterruptedException, RemoteException, SocketTimeoutException;

    boolean accept(int id, String key, String value, int action) throws InterruptedException, RemoteException, SocketTimeoutException;
    String commit(String key, String value, int action) throws RemoteException;

    public int getProposerProposalId() throws RemoteException;

    public void setProposerProposalId(int id) throws RemoteException;

    void updateAllServers(ArrayList<String> allServers) throws RemoteException;
}
