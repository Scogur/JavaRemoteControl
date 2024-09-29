package com.github.scogur.jrc;

import java.io.*;
import java.net.*;
import java.util.Objects;
//import java.util.concurrent.*;
//import java.util.function.Consumer;

public class JRC {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        JRC server = new JRC();
        server.start(port);
    }
    public void start(int port){
        System.out.println("Server is listening on port " + port);
        System.out.println("IP: " + getIP());
        System.out.println("System launched: " + System.getProperty("os.name"));
        System.out.println("Ctrl + C to stop");
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            socket = serverSocket.accept();
            //ConsoleMagic.loading();

            System.out.println("New connection ");

            while (socket.isConnected()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String input = in.readLine();
                System.out.println(input);
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println("hello there");
                if (Objects.equals(input, "exitJRC")){
                    socket.close();
                } else {
                    alterExec(input);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static String getIP(){
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            return socket.getLocalAddress().getHostAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void alterExec(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Windows
        if (System.getProperty("os.name").toLowerCase().contains("windows")){
            processBuilder.command("cmd", "-C", command);
        } else {
            processBuilder.command("bash", "-c", command);
        }


        try {

            Process process = processBuilder.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            reader.lines().forEach(System.out::println);
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with code : " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}


