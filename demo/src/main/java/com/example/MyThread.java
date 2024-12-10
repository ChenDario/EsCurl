package com.example;

import java.io.*;
import java.net.Socket;

public class MyThread extends Thread {
   
    Socket s;
    String rcv = "";
    MyThread(Socket s) {
        this.s = s;
    }

    public void run() {
        do {
            try {
                BufferedReader  in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
    
                rcv = in.readLine();
                System.out.println(rcv + "\n");
    
                String request[] = rcv.split(" ");
    
                String page = request[1].replaceFirst("^/", "");
                
                System.out.println("Richiesta Terminata");
    
                //Risposta
                if(page.isBlank() || page.equals("index.html") || page.equals("file.txt")){
                    //String responseBody  = "<h1><b><i><center>Benvenuti nella WebPage di Chen!<center></i></b></h1>";
                    
                    //Accedo al file
                    File file = new File("htdocs/index.html");
                    InputStream input = new FileInputStream(file);

                    out.writeBytes("HTTP/1.1 200 OK\n");
                    out.writeBytes("Content-Length: " + file.length() + "\n");
                    out.writeBytes("Content-Type: text/html\n");
                    //out.writeBytes("Content-Length: " + responseBody.length() + "\n");
                    out.writeBytes("\n"); //Riga vuota per far capire che ho finito di inviare le intestazioni e inizio col body
                    //out.writeBytes(responseBody + "\r\n"); 

                    //Invio il file
                    byte[] buf = new byte[8192];
                    int n;
                    while((n = input.read(buf)) != -1){
                        out.write(buf, 0, n);
                    }
                    input.close();

    
                } else {
                    out.writeBytes("HTTP/1.1 404 Not Found\n");
                    out.writeBytes("Content-Length: 0\n");
                    out.writeBytes("\n");
                }
    
            } catch (IOException e) { 
                // TODO Auto-generated catch block
                e.printStackTrace();
            }    
        } while (true);
           
    }
}