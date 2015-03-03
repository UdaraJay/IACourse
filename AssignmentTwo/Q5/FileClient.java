import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by noMoon on 2015-03-01.
 */
public class FileClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("usage: java FileClient <compname> <port>");
            System.exit(0);
        }
        String computer = args[0];
        int port = Integer.parseInt(args[1]);
        try {
            Socket server = new Socket(computer, port);
            PrintWriter toServer = new PrintWriter(server.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(
                    server.getInputStream()));
            BufferedReader stdIn = new BufferedReader(
                    new InputStreamReader(System.in));
            System.out.println("please input the file name");
            String fileName = stdIn.readLine();
            toServer.println(fileName);
            String message = fromServer.readLine();
            if (message.equals("FOUND")) {
                System.out.println("The file is found");
                File f = new File(fileName);
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                String line=fromServer.readLine();
                while (!line.equals("END")) {
                    System.out.println(line);
                    bw.write(line);
                    bw.newLine();
                    line=fromServer.readLine();
                }
                bw.close();
            } else if (message.equals("END")) {
                System.out.println("The file " + fileName + " is not found");
            } else {
                throw new Exception("message out of protocol");
            }

            toServer.close();
            fromServer.close();
            stdIn.close();
            server.close();
            System.out.println("File Connection Closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
