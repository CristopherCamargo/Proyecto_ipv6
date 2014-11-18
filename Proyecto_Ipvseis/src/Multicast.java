
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static ArrayList<String> Archivos_Publicos;
    public static ArrayList<String> Archivos_Por_nodo;
    public static ArrayList<String> Nombres_nodos;
    public static ArrayList<String> Mis_Archivos;
    
    private byte[] reciData;
    
    private DatagramPacket reciPacket;

    public Multicast() throws UnknownHostException {
                
        try {
            multi = new MulticastSocket(Parametros.Puerto_Multicast);
            InetAddress Grupo = InetAddress.getByName(Parametros.Grupo_Ip);
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
        Nombres_nodos = new ArrayList<String>();
        
        Limpiar_tabla Clear = new Limpiar_tabla();
        Clear.start();
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
                    for(int i = 2; i < split.length; i++){
                       boolean Comprueba = true;
                       for(int j=0; j < Archivos_Publicos.size();j++){
                           if(split[i].compareTo(Archivos_Publicos.get(j))==0){
                               Comprueba = false;
                               String split2[] = Nombres_nodos.get(j).split(" ");
                               boolean Comprueba2 = true;
                               for(int k=0; k<split2.length;k++){
                                    if(split[1].compareTo(split2[k])==0){
                                        Comprueba2 = false;
                                        break;
                                    }
                                }
                               if(Comprueba2){
                                   if(split[1].compareTo(Parametros.Nombre_computador)!=0){
                                   String resultado = Archivos_Por_nodo.get(j);
                                   String resultado2 = Nombres_nodos.get(j);
                                   Nombres_nodos.set(j,resultado2+" "+split[1]);
                                   Archivos_Por_nodo.set(j, resultado+"-"+reciPacket.getAddress().toString()); 
                                   }
                               }
                               break;
                           }
                       }
                       if(Comprueba){
                           if(Parametros.Nombre_computador.compareTo(split[1])!=0){
                           Archivos_Publicos.add(split[i]);
                           Nombres_nodos.add(split[1]+" ");
                           Archivos_Por_nodo.add(reciPacket.getAddress().toString()+"-");
                           }
                       }        
                    }
                    
                   
                   for(int i=0; i<Archivos_Publicos.size();i++){
                    Inicio.Tabla_Multicast.setValueAt(Archivos_Publicos.get(i), i, 0);
                    Inicio.Tabla_Multicast.setValueAt(Nombres_nodos.get(i), i, 1);
                   }
    }

}
