package preprocessing;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import business.JenaManager;

public class OntologyGenerator {

	public static void main(String[] args) {

                
                String iri = "http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70";
		String inputFileName = "/Users/depiano/Desktop/Project_WebSemantico/OntologyOWL/ProgettoFinale.owl";
		
		
		//Istanzio il gestore
		JenaManager dealer = new JenaManager(iri, inputFileName);
		
		//Provo l'inserimento nell'ontologia
		try {
			dealer.createJDBC("my_tcnapp");
			dealer.insertion();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Effettuo il salvataggio dell'ontologia
                
		try {
			dealer.save();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		dealer.close();
	}

}

