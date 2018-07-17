package tester;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;

import business.JenaManager;
import org.json.simple.JSONObject;

public class TesterSPARQL {

	public static void main(String[] args) {

                
                String iri = "http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70";
		String inputFileName = "/Users/depiano/Desktop/Project_WebSemantico/OntologyOWL/ProgettoFinale_NEW.owl";
		
		
		//Istanzio il gestore
		JenaManager dealer = new JenaManager(iri, inputFileName);
		
		String ns = "<"+iri+"#";
		
		OntModel ont = dealer.getModel();
		
		Individual ind = ont.getIndividual(iri+"#Emilio_Arvonio");
		
		System.out.println(ind);
		
		String query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT  ?Descrizione ?cf ?Anno_di_costruzione ?Arredamento ?Codistat ?Latitudine ?Longitudine ?Nome_comune ?Dug ?Denominazione ?Civico ?giardino ?Metri_quad ?Num_vani ?piano ?Num_livelli  ?Num_bagni ?classe_energetica ?Posto_auto ?Prezzo ?nome ?cognome"
                              +" WHERE{ ?subject ws:è_posseduto ?proprietario."
                              +" ?subject ws:DESCRIZIONE ?Descrizione."
                              +" ?subject ws:ANNO_DI_COSTRUZIONE ?Anno_di_costruzione."
                              +" ?subject ws:ARREDAMENTO ?Arredamento."
                              +" ?subject ws:LATITUDINE ?Latitudine."
                              +" ?subject ws:LONGITUDINE ?Longitudine."
                              +" ?subject ws:CODISTAT ?Codistat."
                              +" ?subject ws:GIARDINO ?giardino."
                              +" ?subject ws:CLASSE_ENERGETICA ?classe_energetica."
                              +" ?subject ws:DUG ?Dug."
                              +" ?subject ws:PIANO ?piano."
                              +" ?subject ws:DENOMINAZIONE ?Denominazione."
                              +" ?subject ws:CIVICO ?Civico."
                              +" ?subject ws:METRI_QUADRATI ?Metri_quad."
                              +" ?subject ws:N_BAGNI ?Num_bagni."
                              +" ?subject ws:N_VANI ?Num_vani."
                              +" ?subject ws:N_LIVELLI ?Num_livelli."
                              +" ?subject ws:NOMECOMUNE ?Nome_comune."
                              +" ?subject ws:POSTO_AUTO ?Posto_auto."
                              +" ?subject ws:PREZZO ?Prezzo."
                              +" ?proprietario a ws:Proprietario."
                              +" ?proprietario ws:NOME ?nome."
                              +" ?proprietario ws:COGNOME ?cognome."
                              + "?proprietario ws:CF ?cf.}";
		System.out.println();
		JSONObject result=dealer.queryImmobili_1(query);
		System.out.println();
		dealer.close();
	}

}

