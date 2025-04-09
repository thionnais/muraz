/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classe;
import inventory.Formmain.*;
import static inventory.Formmain.conn;
import static inventory.Formmain.rs;
import static inventory.Formmain.st;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author LANKOANDE
 */
public class Codification {
    private static final String REGION_CODE = "09"; // Code constant de la région
    private static final String PROVINCE_CODE = "10"; // Code constant de la province
    private static final String DEPARTEMENT_CODE = "02"; // Code constant dU DEPARTEMENT
    private static final String BUREAU_CODE = "21"; // Code constant DU MINISTERE DE LA SANTE
    
   
    
    
    
    public Codification() {}
    // Constructeur de la classe Codification
    public String generateCodification(String souscodecat, int anneeacquisition) {
       
        String postcd;
        String nouveaucode="";
        if (anneeacquisition <2021) {
        postcd= "33.02";
       
        }
        else {
        postcd = "433.03";}
        //
        String combine = souscodecat+"/"+BUREAU_CODE+"/"+postcd+"/"+REGION_CODE+"/"+PROVINCE_CODE+"/"+DEPARTEMENT_CODE+"/"+anneeacquisition+"/";
        String requette= "SELECT codemateriel FROM materiels WHERE codemateriel LIKE '"+combine+"%' ORDER BY dateajout DESC LIMIT 1";
        try{
            // condition pour se connecter a la base de donnée, recuperer le dernier enregistrement et le concatener avec le 0001.
        conn=DBmanager.se_connecter();
        st=conn.createStatement();
        rs=st.executeQuery(requette);
        if (!rs.next()) {
        nouveaucode=combine+"0001";
        //System.out.println(nouveaucode);
        }
        else{ 
            //sous code pour recuperer et convertir en integer la taille du dernier numero ordre.ensuite proceder a une incrementation.
        String derniercode = rs.getString("codemateriel");
        String ordre = derniercode.substring( combine.length());
        int ordreconvert=Integer.parseInt(ordre);
        ordreconvert=ordreconvert+1;
        int tailleordreconvert=(""+ordreconvert+"").length();
        if (tailleordreconvert==1){
         nouveaucode= combine+"000"+ordreconvert;
        }
        else if(tailleordreconvert==2){
        nouveaucode= combine+"00"+ordreconvert;
        }
        else if (tailleordreconvert==3){
            nouveaucode= combine+"0"+ordreconvert;
        }
        else {
        nouveaucode= combine+""+ordreconvert;
        
        }
       
        }
        rs.close();
        st.close();
        conn.close();
        }catch(SQLException e) {
        
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Codification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nouveaucode;
    }

}
