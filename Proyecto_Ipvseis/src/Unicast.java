

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
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
public class Unicast extends Thread{
    Inicio ventana; 
    String puerto_escucha = ventana.unicastPuerto1.getText();
    public static ArrayList<String> Archivos_Privados;
   
    @Override
    public void run(){
        try {
            DatagramSocket socket = new DatagramSocket(Integer.parseInt(puerto_escucha));
            while(true){
                System.out.println("Esperando cabeceras");
                byte[] mensaje_bytes = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(mensaje_bytes,1024);
                socket.receive(paquete);
                String mensaje_archivo = new String(mensaje_bytes);
                
                String[] _val;
                _val = mensaje_archivo.split("/");

                
                int resp = JOptionPane.showConfirmDialog(null, "Archivo nuevo: "+_val[0]+" tamaño: "+_val[1]+" byte De:"+paquete.getAddress(), "¿Desea recibirlo?",1);
                if(resp==0){
                    System.out.println("Leyendo archivo");
                    String mensaje = "Acepto";
                    InetAddress address = InetAddress.getByName(paquete.getAddress().getHostAddress());
                    System.out.println(paquete.getAddress().getHostAddress());
                    DatagramPacket acepto = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length,address, 20002 );
                    socket.send(acepto);
                    
                    int data_rec = 0;
                    int tamahno = Integer.parseInt(_val[1].trim());
                    int velocidad = 20480;
                    
                    FileOutputStream file_recibido = new FileOutputStream("Recibidos/"+_val[0]);
                    while (data_rec < tamahno) {

                        System.out.println("leido: "+data_rec);
                        byte[] mensaje_bytes_archivo = new byte[velocidad];
                        DatagramPacket archivo = new DatagramPacket(mensaje_bytes_archivo,mensaje_bytes_archivo.length);
                        socket.receive(archivo);

                        data_rec +=velocidad;
                        file_recibido.write(mensaje_bytes_archivo, 0, mensaje_bytes_archivo.length);

                    }

                    System.out.println("Lectura finalizada corectamente");
                    JOptionPane.showMessageDialog(null, "Archivo recibido exitosamente...");
                    file_recibido.close();
                    
                }
            }
        } catch (Exception ex) {
            System.out.println("error: "+ex);
        }
    
    }
    
    public void listarArchivosPrivados(){
        Archivos_Privados = new ArrayList<String>();
        String Directorio = "Privado";

        File file = new File(Directorio); 
        if (file.exists()){  
            File[] ficheros = file.listFiles();
            Archivos_Privados.clear();
            for (int x = 0; x < ficheros.length; x++ ){ 
                Archivos_Privados.add(ficheros[x].getName());
            }
            
        ventana.archivosList.setListData(Archivos_Privados.toArray());
        }
    }
    
}
