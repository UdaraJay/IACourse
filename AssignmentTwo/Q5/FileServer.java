import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by noMoon on 2015-03-01.
 */
public class FileServer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("usage: java FileServer <port>");
            System.exit(0);
        }
        int port = Integer.parseInt(args[0]);
        try {
            System.out.println("File Server Waiting For Connection.");

            ServerSocket serverSocket = new ServerSocket(port);

            Socket clientSocket = serverSocket.accept();

            PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));

            String fileName = fromClient.readLine();
            File f = new File("./");
            boolean flag = false;
            for (String path : f.list()) {
                if (path.equals(fileName)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                toClient.println("FOUND");
                FileInputStream fis = new FileInputStream(new File(fileName));
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(fis));
                String fileLine;
                while (null != (fileLine = fileReader.readLine())) {
                    System.out.println(fileLine);
                    toClient.println(fileLine);
                }
            }
            toClient.println("END");
            toClient.close();
            fromClient.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("File Connection Closed");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
