
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.InetAddress;
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
public class Multicast_envio extends Thread{
    
    private byte[] enviData;
    
    private DatagramPacket enviPacket;
    public static ArrayList<String> Mis_archivos;

    public Multicast_envio() {
        
        enviData = new byte[1000];
        
        enviPacket = new DatagramPacket(enviData, enviData.length);
        
        Mis_archivos = new ArrayList<String>();
    }
    
    public String Leer_Archivos(){
        String resultado = "-";
        String Directorio = "Publico";
        File file = new File(Directorio); 
        if (file.exists()){  
            File[] ficheros = file.listFiles();
            Mis_archivos.clear();
            for (int x=0;x<ficheros.length;x++){ 
                Mis_archivos.add(ficheros[x].getName());
                resultado += ficheros[x].getName()+"-";
            }
            
            Inicio.Lista_mis_archivos.setListData(Mis_archivos.toArray());
            
            return resultado;
        }else { 
            return resultado;
        }
    }
    
    public boolean Enviar_lista_archivos() throws IOException{
        
        String mensaje = "Mul::Archivos";
        
        mensaje += Leer_Archivos()+"&&.";
        
        byte [] buffer = new byte [0];
        buffer = mensaje.getBytes();
        
        try {
            DatagramPacket packete = new DatagramPacket(buffer, 0, InetAddress.getByName(Multicast.Grupo_Ip),Multicast.Puerto_Multicast);
            packete.setData(buffer);
            Multicast.multi.send(packete);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Multicast_envio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    } 

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        
        while(true){
            try {
                if(Enviar_lista_archivos()){
                    System.out.println("Envio lista a los nodos correctamente");
                }else{
                    System.out.println("Hubo un error al enviar lista a los nodos");
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Multicast_envio.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Multicast_envio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
