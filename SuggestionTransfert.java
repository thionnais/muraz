/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classe;



/**
 *
 * @author LANKOANDE
 */
//public class Suggestion {
    
//}

import static inventory.Formmain.conn;
import static inventory.Formmain.pse;
import static inventory.Formmain.st;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class SuggestionTransfert {
    private final JTextField searchField;
    private final JPopupMenu suggestionsMenu;
    
            
    public SuggestionTransfert(JTextField searchField, JPanel parentPanel) {
        this.searchField = searchField;
        this.suggestionsMenu = new JPopupMenu();
        

        // Ajouter un écouteur pour mettre à jour les suggestions
        this.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    updateSuggestions();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SuggestionTransfert.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    updateSuggestions();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SuggestionTransfert.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    updateSuggestions();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SuggestionTransfert.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Méthode pour récupérer les noms et matricules à partir de la base de données
    private List<String[]> getEleves(String input) throws ClassNotFoundException {
        List<String[]> eleves = new ArrayList<>();
        String query = "SELECT codemateriel FROM materiels WHERE codemateriel LIKE ?";
         try{
                conn=DBmanager.se_connecter();
                st=conn.createStatement();
                pse = conn.prepareStatement(query);
                pse.setString(1, input + "%");// si le % est retirer la suggestion sera faite unique que si le code complet est saise
                ResultSet rs = pse.executeQuery();
            while (rs.next()) {
                String codemateriel = rs.getString("codemateriel");
                
                eleves.add(new String[]{codemateriel});
                
            }
            
            conn.close();
            st.close();
            pse.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des données: " + e.getMessage());
        }
        return eleves;
    }

    // Méthode pour mettre à jour les suggestions
    private void updateSuggestions() throws ClassNotFoundException {
        String input = searchField.getText().toLowerCase();
        suggestionsMenu.removeAll();

        if (!input.isEmpty()) {
            List<String[]> eleves = getEleves(input);
            //System.out.println();
            for (String[] eleve : eleves) {
                String codemateriel = eleve[0];
                
                
                JMenuItem item = new JMenuItem(codemateriel);
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        searchField.setText(codemateriel);
                        suggestionsMenu.setVisible(false);
                        
                    }
                });
                suggestionsMenu.add(item);
            }

            if (suggestionsMenu.getComponentCount() > 0) {
                suggestionsMenu.show(searchField, 0, searchField.getHeight());
                SwingUtilities.invokeLater(() -> searchField.requestFocusInWindow());
            } else {
                suggestionsMenu.setVisible(false);
            }
        } else {
            suggestionsMenu.setVisible(false);
        }
    }
    
    
}