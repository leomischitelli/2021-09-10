/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	cmbB1.getItems().clear();
    	cmbB2.getItems().clear();
    	String citta = cmbCitta.getValue();
    	if(citta == null) {
    		txtResult.setText("Selezionare una citta prima di creare il grafo.");
    		return;
    	}
    	this.model.creaGrafo(citta);
    	txtResult.setText("Grafo creato!\nNumero vertici: " + model.getNumeroVertici());
		txtResult.appendText("\nNumero archi: " + model.getNumeroArchi());
		cmbB1.getItems().addAll(this.model.getListaBusiness());
		cmbB2.getItems().addAll(this.model.getListaBusiness());
    	
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	
    	if(cmbCitta.getValue() == null) {
    		txtResult.appendText("\nSelezionare una citta e creare il grafo prima di procedere.");
    		return;
    	}
    	Business business = cmbB1.getValue();
    	if(business == null) {
    		txtResult.appendText("\nSelezionare un locale prima di procedere.");
    		return;
    	}
    	Business distante = this.model.getLocaleDistante(business);
    	txtResult.appendText("\nLocale più distante: " + distante.toString() + " = " + this.model.getDistanzaMax());
    	

    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	Business b1 = cmbB1.getValue();
    	Business b2 = cmbB2.getValue();
    	
    	if(b1 == null || b2 == null) {
    		txtResult.appendText("\nSelezionare b1 e b2 prima di procedere.");
    		return;
    	}
    	
    	try {
    		
    		Double soglia = Double.parseDouble(txtX2.getText());
    		if(soglia < 0.0 || soglia > 5.0) {
    			txtResult.appendText("\nInserire un numero decimale positivo fra 0 e 5 (inclusi) prima di procedere.");
    			return;
    		}
    		
    		Set<Business> sequenzaMigliore = this.model.trovaSequenza(b1, b2, soglia);
    		if(sequenzaMigliore.isEmpty()) {
    			txtResult.appendText("\nSequenza vuota, errore imprevisto.");
    			return;
    		}
    		
    		txtResult.appendText("\nTrovata sequenza di dimensione " + sequenzaMigliore.size());
    		for(Business b : sequenzaMigliore) {
    			txtResult.appendText("\n" + b.toString());
    		}
    		txtResult.appendText("\nDistanza totale: " + this.model.getDistanzaTotale(sequenzaMigliore) + " km");
    		
    	} catch (NumberFormatException e) {
    		txtResult.appendText("\nInserire un numero prima di procedere.");
    	} catch (NullPointerException e) {
    		txtResult.appendText("\nInserire un numero prima di procedere.");
    	}

    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbCitta.getItems().addAll(this.model.getAllCities());
    }
}
