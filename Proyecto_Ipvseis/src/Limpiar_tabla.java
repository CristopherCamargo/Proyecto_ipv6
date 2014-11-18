
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
public class Limpiar_tabla extends Thread{

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        
        while(true){
            
            for(int i=0;i<50;i++){
                    Inicio.Tabla_Multicast.setValueAt(" ", i, 0);
                    Inicio.Tabla_Multicast.setValueAt(" ", i, 1);
            }
            
            Multicast.Archivos_Por_nodo.clear();
            Multicast.Archivos_Publicos.clear();
            Multicast.Nombres_nodos.clear();
            
            try {
                sleep(20000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Limpiar_tabla.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
