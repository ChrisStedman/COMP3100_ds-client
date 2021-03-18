import java.net.*;
import java.io.*;

public class Client {

    //Handshake Strings
    private final static String HELO = "HELO";
    private final static String AUTH = "AUTH "+System.getProperty("user.name");
    private final static String REDY = "REDY";
    private final static String OK = "OK";
    private final static String QUIT = "QUIT";

    //Server Job Strings
    private final static String JOBN = "JOBN";
    private final static String JOBP = "JOBP";
    private final static String JCPL = "JCPL";
    private final static String RESF = "RESF";
    private final static String RESR = "RESR";
    private final static String NONE = "NONE";

    //Socket & IO 
    private static Socket socket = null;
    private static BufferedReader inputStream = null;
    private static DataOutputStream outputStream = null;

    private static final int SERVERPORT = 50000;
    private static final String SERVERIP = "127.0.0.1";

    //Status Codes
    private static final int ERROR = -1;
    private static final int SUCCESS = 0;

    public static void main(String[] args)  {
        
        try {
            Client client = new Client();
            client.run();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Client() throws UnknownHostException, IOException {
        socket = new Socket(SERVERIP, SERVERPORT);
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    private void run() throws IOException {
        if(connectionHandshake() != 0)
            closeConnection(ERROR);

            String job[] = readFromSocket().split(" ");
            System.out.println(job.toString());
           // determineAction(job);
           closeConnection(SUCCESS);
    }

    private int connectionHandshake() throws IOException {
        writeToSocket(HELO);
        String response = readFromSocket();
        if(checkResponse(OK, response) != SUCCESS) 
            return ERROR;

        writeToSocket(AUTH);
        response = readFromSocket();
        if(checkResponse(OK, response) != SUCCESS)
            return ERROR;
 
        writeToSocket(REDY);
        return SUCCESS; 
    }

    private void writeToSocket(String message) throws IOException{
        outputStream.write(message.getBytes());
        outputStream.flush();

        System.out.println("Client: "+message);
    }

    private String readFromSocket() throws IOException{
        StringBuilder message = new StringBuilder();
        
        while(!inputStream.ready());

        while((inputStream.ready())){
            message.append((char)inputStream.read());
        }
        System.out.println("Server: "+message);
        return message.toString();
    }

    private int checkResponse(String expected, String message){
        return expected.equals(message) ? SUCCESS : ERROR;
    }

   /* private int determineAction(String[] job) {
        switch(job[0]){
            case: JOBN
        }
        return 0;
    }*/

    private void closeConnection(int status) throws IOException{
        if(status == SUCCESS){
            writeToSocket(QUIT);
            String response = readFromSocket();
            checkResponse(QUIT, response);
        }
        
        inputStream.close();
        outputStream.close();
        socket.close();
        
    }
}

           


