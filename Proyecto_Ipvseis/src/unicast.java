
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author leonel
 */
public class unicast extends Thread{
    int puerto = 20001;
    DatagramSocket socket;
    
    @Override
    public void run(){
        try {
            socket = new DatagramSocket( puerto );
            while(true){
                System.out.println("Esperando cabeceras");
                byte[] mensaje_bytes = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(mensaje_bytes,1024);
                socket.receive(paquete);
                System.out.println(paquete.getAddress());
                String mensaje = new String(mensaje_bytes);
                System.out.println("llego: "+mensaje);
            }
            
            
        } catch (Exception ex) {
            System.out.println("error: "+ex);
        }
    
    }
    
}
