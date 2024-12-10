package com.example;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class MyThread extends Thread {

    Socket s;
    String rcv = "";

    MyThread(Socket s) {
        this.s = s;
    }

    public void run() {
        do {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
    
                // Leggi la richiesta
                rcv = in.readLine();
                System.out.println(rcv + "\n");
    
                // Verifica che la richiesta sia valida (ad esempio GET)
                String[] request = rcv.split(" ");
                if (request.length < 3 || !request[0].equalsIgnoreCase("GET")) {
                    out.writeBytes("HTTP/1.1 400 Bad Request\n");
                    out.writeBytes("Content-Length: 0\n");
                    out.writeBytes("\n");
                    return;
                }
    
                // Decodifica l'URL e ottieni il percorso
                String page = URLDecoder.decode(request[1], StandardCharsets.UTF_8);
    
                // Gestisci la situazione di root ("/")
                if (page.equals("/")) {
                    page = "/index.html"; // Per esempio, restituisci index.html come predefinito
                }
    
                System.out.println("Richiesta Terminata");
    
                // Accedi al file richiesto
                File file = new File("htdocs" + page);
                if (file.exists() && !file.isDirectory()) {
                    // Se il file esiste, invia la risposta 200 OK
                    try (InputStream input = new FileInputStream(file)) {
                        out.writeBytes("HTTP/1.1 200 OK\n");
                        out.writeBytes("Content-Length: " + file.length() + "\n");
                        out.writeBytes("Content-Type: " + getContentType(file) + "\n");
                        out.writeBytes("\n"); // Riga vuota per separare intestazioni e body
    
                        // Invia il file
                        byte[] buf = new byte[8192];
                        int n;
                        while ((n = input.read(buf)) != -1) {
                            out.write(buf, 0, n);
                        }
                    }
    
                } else {
                    // Se il file non esiste, invia la risposta 404 Not Found
                    out.writeBytes("HTTP/1.1 404 Not Found\n");
                    out.writeBytes("Content-Length: 0\n");
                    out.writeBytes("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        } while (true);
    }

    private String getContentType(File f) {
        String[] s = f.getName().split("\\.");
        String ext = s[s.length - 1].toLowerCase();

        switch (ext) {
            case "html":
            case "htm":
                return "text/html";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "css":
                return "text/css";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "js":
                return "application/javascript";
            case "json":
                return "application/json";
            default:
                return "application/octet-stream"; // Tipo di default per file sconosciuti
        }
    }
}
