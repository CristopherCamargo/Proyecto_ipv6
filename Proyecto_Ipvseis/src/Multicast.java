
import java.awt.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.crypto.KeySelector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Caceres
 */
public class Multicast extends Thread{
    
    public static MulticastSocket multi;
    public static String Grupo_Ip;
    public static int Puerto_Multicast;
    public static ArrayList<String> Archivos_Publicos;
    public static ArrayList<String> Archivos_Por_nodo;
    public static ArrayList<String> Mis_Archivos;
    
    private byte[] reciData;
    
    private DatagramPacket reciPacket;

    public Multicast() throws UnknownHostException {
        
        Puerto_Multicast = 9898;
        Grupo_Ip = "ffeb:1814:5824:542::1"; 
        
        try {
            multi = new MulticastSocket(Puerto_Multicast);
            InetAddress Grupo = InetAddress.getByName(Grupo_Ip);
            multi.joinGroup(Grupo);
        } catch (IOException ex) {
            Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        reciData = new byte[1000];
        
        reciPacket = new DatagramPacket(reciData, reciData.length);
        
        Multicast_envio multicast_envio = new Multicast_envio();
        multicast_envio.start();
        
        Archivos_Publicos = new ArrayList<String>();
        Archivos_Por_nodo = new ArrayList<String>();
            
    }
    
    public void Enviar_Archivo(){
        
    
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        
        System.out.println("Creado hilo para ejecucion de multicast");
        
        while(true){
            try {
                multi.receive(reciPacket);   
            } catch (IOException ex) {
                Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String cadena = new String(reciPacket.getData());
            
                if(cadena.startsWith("Mul::Archivos")){
                      Recibiendo_Lista_Archivos(cadena);
                }
                else{
                    if(cadena.startsWith("Mul::PeticionArchivo")){
                    
                    }
                }
               
        }
    }
    
    public void Recibiendo_Lista_Archivos(String cadena){
                    String zero[] = cadena.split("&&.");
                    String split[] = zero[0].split("-");
                    for(int i = 1; i < split.length; i++){
                       boolean Comprueba = true;
                       for(int j=0; j < Archivos_Publicos.size();j++){
                           if(split[i].compareTo(Archivos_Publicos.get(j))==0){
                               Comprueba = false;
                               String split2[] = Archivos_Por_nodo.get(j).split("-");
                               boolean Comprueba2 = true;
                               for(int k=0; k<split2.length;k++){
                                    if(reciPacket.getAddress().toString().compareTo(split2[k])==0){
                                        Comprueba2 = false;
                                        break;
                                    }
                                }
                               if(Comprueba2){
                                   String resultado = Archivos_Por_nodo.get(j);
                                   Archivos_Por_nodo.set(j, resultado+" "+reciPacket.getAddress().toString()); 
                               }
                               break;
                           }
                       }
                       if(Comprueba){
                           
                           Archivos_Publicos.add(split[i]);
                           Archivos_Por_nodo.add(reciPacket.getAddress().toString()+"-");
                       }        
                    }
                   for(int i=0; i<Archivos_Publicos.size();i++){
                    Inicio.Tabla_Multicast.setValueAt(Archivos_Publicos.get(i), i, 0);
                    Inicio.Tabla_Multicast.setValueAt(Archivos_Por_nodo.get(i), i, 1);
                   }
    }

}
