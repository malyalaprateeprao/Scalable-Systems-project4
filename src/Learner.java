import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Learner implements Runnable{

    HashStorage storage = null;
    public Learner(HashStorage storage){
        this.storage = storage;
    }

    public String commit(String key, String value, int action) {

        log("Received a commit request to the Learner");
        String response = "";

        if(action == 1) {
            response = storage.get(key);
        }
        else if(action == 2) {
            response = storage.put(key, value);
        }
        else if(action == 3) {
            response = storage.delete(key);
        }

        return response;
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
