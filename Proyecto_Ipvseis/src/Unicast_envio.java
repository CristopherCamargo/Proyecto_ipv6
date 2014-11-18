
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author leonel
 */
public class Unicast_envio extends Thread  {
    Inicio ventana;
    @Override
    public void run(){
        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramSocket socket_recibe = new DatagramSocket(20002);
            
            //Enviando archivo
            InetAddress address = InetAddress.getByName(ventana.unicastIP.getText());
            String archivo = ventana.archivosList.getSelectedValue().toString();
            File abre = new File("Privado/"+archivo);
            String mensaje = abre.getName() + "/" + abre.length();
            DatagramPacket paquete = new DatagramPacket(mensaje.getBytes(),mensaje.getBytes().length,address, Integer.parseInt(ventana.unicastPuerto.getText()));
            socket.send(paquete);
            
            //Recibiendo respuesta de aceptacion
            byte[] mensaje_bytes = new byte[256];
            DatagramPacket paquete_recibido = new DatagramPacket(mensaje_bytes,256);
            System.out.println("Esperando aceptacion");
            socket_recibe.receive(paquete_recibido);
            
            String mensaje_recibido = new String(mensaje_bytes);
            System.out.println("Llego respuesta: " + mensaje_recibido);
            
            mensaje_recibido = mensaje_recibido.trim();
            //Enviar archivo
            if( "Acepto".equals(mensaje_recibido) ) {
                FileInputStream f = new FileInputStream("Privado/"+archivo);
               
                int velocidad = 256;
                byte[] buffer = new byte[velocidad];
                int len;
                int len_tem=0;
                float porcentaje;
                ventana.unicastProgress.setVisible(true);
                while ((len = f.read(buffer)) > 0) {
                    DatagramPacket paq = new DatagramPacket(buffer, buffer.length, address,Integer.parseInt(ventana.unicastPuerto.getText()));
                    socket.send(paq);
                    len_tem +=velocidad;
                    porcentaje = (float) (len_tem*100/abre.length());
                    ventana.unicastProgress.setValue((int)porcentaje);
                    System.out.println("Enviado "+len_tem);
                    Thread.sleep(1000);
                }
                ventana.unicastProgress.setVisible(false);
                ventana.unicastProgress.setValue(0);
                System.out.println("¡Archivo enviado exitosamente!");
                JOptionPane.showMessageDialog(null, "¡Archivo enviado exitosamente!");
                socket.close();
                socket_recibe.close();
            }else{
                JOptionPane.showMessageDialog(null, "¡Archivo cancelado!");
            }
            
         } catch (Exception ex1) {
            System.out.println("Error: " +ex1);
         } 
    }
}
