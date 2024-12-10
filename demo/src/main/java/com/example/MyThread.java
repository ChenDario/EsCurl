package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyThread extends Thread {
   
    Socket s;
    String rcv = "";
    MyThread(Socket s) {
        this.s = s;
    }

    public void run() {
       
        try {
            BufferedReader  in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            do {      
                rcv = in.readLine();
                System.out.println(rcv + "\n");
            } while (!rcv.equals("") );
            
            System.out.println("Richiesta Terminata");

            /*
            String responseBody  = "<b>Hello World!</b> <img src = 'smile.png'>";
            out.writeBytes("HTTP/1.1 200 OK\n");    
            out.writeBytes("Content-Type: text/html\n");
            out.writeBytes("Content-Length: " + responseBody.length() + "\n");
            out.writeBytes("\n"); //Riga vuota per far capire che ho finito di inviare le intestazioni e inizio col body
            out.writeBytes(responseBody + "\r\n"); 
            */
            //Risposta di pagina non trovata
            //String responseBody  = "Pagina non trovata";
            out.writeBytes("HTTP/1.1 404 Not Found\n");
            out.writeBytes("Content-Length: 0\n");
            out.writeBytes("\n"); //Riga vuota per far capire che ho finito di inviare le intestazioni e inizio col body
            //out.writeBytes(responseBody + "\r\n"); 
            

        } catch (IOException e) { 
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
    }
}