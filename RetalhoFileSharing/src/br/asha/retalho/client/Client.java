/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.asha.retalho.client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author fir3destr0yer
 */
public class Client {
    public static void main (String [] args ) throws IOException {
	    int filesize=1022386; 
	    int bytesRead;
	    int currentTot = 0;
	    Socket socket = new Socket("200.235.88.221",15123);
	    byte [] bytearray  = new byte [filesize];
	    InputStream is = socket.getInputStream();
	    FileOutputStream fos = new FileOutputStream("copy.pptx");
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    bytesRead = is.read(bytearray,0,bytearray.length);
	    currentTot = bytesRead;

	    do {
	       bytesRead =
	          is.read(bytearray, currentTot, (bytearray.length-currentTot));
	       if(bytesRead >= 0) currentTot += bytesRead;
	    } while(bytesRead > -1);

	    bos.write(bytearray, 0 , currentTot);
	    bos.flush();
	    bos.close();
	    socket.close();
	  }
}
