package business;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.XSD;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JenaManager {

	public JenaManager(String iri, String inputFileName)
	{
		ns = iri+"#";
		filename = inputFileName;
		inf = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open(filename);
		if ( in == null) {
			throw new IllegalArgumentException("File: " + filename + " not found");
		}

		inf.read(in, null);
	}

	public void close()
	{
		inf.close();
	}

	public void createJDBC(String db)
	{
		try {
			jdbc = new JDBCManager(db);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Wrapping della creazione di una classe
	private OntClass createClass(String className)
	{

		return inf.createClass(ns+className);
	}

	//Wrapping della creazione di un Individuo
	private Individual createIndividual(String individual, String className)
	{
		OntClass classA = inf.getOntClass(ns+className);
		return inf.createIndividual(ns+individual, classA);
	}

	public OntModel getModel() 
	{
		return inf;
	}
	
	
	//Metodo che avvia gli inserimenti degli Individui nell'ontologia
	public void insertion() throws SQLException
	{
            
            //Vengono inseriti tutte le persone dal DB
            insertPersona();
            // Vengono inseriti tutte le agenzie dal db
            insertAgenzia();
            //Vengono inseriti tutti gli immobili dal DB
            insertImmobile();
            
		//Vengono inseriti gli Universi dal DB
		//insertDBUniverses();
		//Vengono inseriti i Personaggi dal DB
		//insertDBCharacters();
	}

           private void insertPersona() throws SQLException{
		ResultSet rs = jdbc.query("SELECT * FROM user");
		//Ciclo di inserimento degli universi
		while (rs.next())
		{
			//Si ottengono nome, in cui vengono rimossi gli spazi, cancellate le parentesi e spazi sostituiti con unde
			String nome = rs.getString("NOME");
			String cognome = rs.getString("COGNOME");
			String email = rs.getString("EMAIL");
			String phone=rs.getString("PHONE");
			String indirizzo=rs.getString("INDIRIZZO").trim().replace(" ", "_");
			String cf=rs.getString("CF").trim().replace(" ", "");


			//Si genera l'Individuo
			
                        Individual persona = createIndividual(cf,"Persona");
                      /*  persona.addOntClass(inf.getResource("Proprietario"));*/

			//Si settano le Properties
                        
                        DatatypeProperty nomeProperty = inf.createDatatypeProperty(ns+"NOME");
                	DatatypeProperty cognomeProperty = inf.createDatatypeProperty(ns+"COGNOME");
			DatatypeProperty indirizzoProperty = inf.createDatatypeProperty(ns+"INDIRIZZO");
			DatatypeProperty emailProperty = inf.createDatatypeProperty(ns+"EMAIL");
			DatatypeProperty phoneProperty = inf.createDatatypeProperty(ns+"TELEFONO");
			DatatypeProperty cfProperty = inf.createDatatypeProperty(ns+"CF");



			inf.add(persona, indirizzoProperty, indirizzo);
			inf.add(persona, cognomeProperty, cognome);
			inf.add(persona, nomeProperty, nome);
			inf.add(persona, emailProperty, email);
			inf.add(persona, phoneProperty, phone);
			inf.add(persona, cfProperty, cf);






		

		}


	}

        private void insertAgenzia() throws SQLException{
		ResultSet rs = jdbc.query("SELECT * FROM agenzia");
		//Ciclo di inserimento degli universi
		while (rs.next())
		{
			//Si ottengono nome, in cui vengono rimossi gli spazi, cancellate le parentesi e spazi sostituiti con unde
                        String pIva = rs.getString("P_IVA");
			String ragioneSociale = rs.getString("RAGIONE_SOCIALE").trim().replace(" ", "_");
			String email = rs.getString("EMAIL");
			String telefono=rs.getString("TELEFONO");
			String indirizzo=rs.getString("INDIRIZZO").trim().replace(" ", "_");

			//Si genera l'Individuo
                        
                       
                        ragioneSociale=ragioneSociale.trim().replace(" ", "_");
                       ragioneSociale=ragioneSociale.trim().replace(":", "_");
                       ragioneSociale=ragioneSociale.trim().replace("'\'", "_");
                       ragioneSociale=ragioneSociale.trim().replace(".", "_");
                       ragioneSociale=ragioneSociale.trim().replace("/", "_");
                       ragioneSociale=ragioneSociale.trim().replace("-", "_");
                       ragioneSociale=ragioneSociale.trim().replace("'", "_");
                       
                       
			Individual agenzia = createIndividual(pIva,"Agenzia");

			//Si settano le Properties
			DatatypeProperty pIvaProperty = inf.getDatatypeProperty(ns+"P_IVA");
			DatatypeProperty ragioneSocialeProperty = inf.getDatatypeProperty(ns+"RAGIONE_SOCIALE");
			DatatypeProperty indirizzoProperty = inf.getDatatypeProperty(ns+"INDIRIZZO");
			DatatypeProperty emailProperty = inf.getDatatypeProperty(ns+"EMAIL");
			DatatypeProperty telefonoProperty = inf.getDatatypeProperty(ns+"TELEFONO");




			inf.add(agenzia, indirizzoProperty, indirizzo);
			inf.add(agenzia, emailProperty, email);
			inf.add(agenzia, telefonoProperty, telefono);
			inf.add(agenzia, pIvaProperty, pIva);
			inf.add(agenzia, ragioneSocialeProperty, ragioneSociale);

	}

	}

        //Metodo di inserimento degli immobili nell'ontologia
	private void insertImmobile() throws SQLException
	{       OntClass Immobile = createClass("Immobile");
		OntClass Persona = createClass("Persona");
            
		//Query con cui si ottengono TUTTI gli immobili

		ResultSet rs = jdbc.query("SELECT * FROM rilievo");


		//Variabile di testing
		int count = 0;

		//Ciclo di inserimento degli universi
		while (rs.next())
		{
			//Si ottengono nome, in cui vengono rimossi gli spazi, cancellate le parentesi e spazi sostituiti con unde
			String codistat = rs.getString("CODISTAT");
                        String longitudine = rs.getString("LONGITUDINE");
                        String latitudine = rs.getString("LATITUDINE");
			String comune=rs.getString("NOMECOMUNE");
			String dug=rs.getString("DUG");
			String denominazione=rs.getString("DENOMINAZIONE").trim().replace(" ", "_");
			String civico=rs.getString("CIVICO");
			String esponente=rs.getString("ESPONENTE").trim().replace(" ", "_");
			String annoDiCostruzione=rs.getString("ANNO_DI_COSTRUZIONE");
			String classeEnergetica=rs.getString("CLASSE_ENERGETICA");
			String descrizione=rs.getString("DESCRIZIONE");
			String giardino=rs.getString("GIARDINO");
			String metriquadrati=rs.getString("METRI_QUADRATI");
			String bagni=rs.getString("N_BAGNI");
			String livelli=rs.getString("N_LIVELLI");
			String vani=rs.getString("N_VANI");
			String piano=rs.getString("PIANO").trim().replace(" ", "_");;
			String auto=rs.getString("POSTO_AUTO");
			String prezzo=rs.getString("PREZZO");
			String tipologia=rs.getString("TIPOLOGIA").trim().replace(" ", "_");;
			String arredamento=rs.getString("ARREDAMENTO");
			String proprietario=rs.getString("PROPRIETARIO");
			String venditore=rs.getString("VENDITORE");
			String acquirente=rs.getString("ACQUIRENTE");
			String locatore=rs.getString("LOCATORE");
                        String locatario=rs.getString("LOCATARIO");





                        String id_protege_immobile=tipologia+"_"+dug+"_"+denominazione+"_"+civico;
                       id_protege_immobile=id_protege_immobile.trim().replace(" ", "_");
                       id_protege_immobile=id_protege_immobile.trim().replace(":", "_");
                       id_protege_immobile=id_protege_immobile.trim().replace("'\'", "_");
                       id_protege_immobile=id_protege_immobile.trim().replace(".", "_");
                       id_protege_immobile=id_protege_immobile.trim().replace("/", "_");
                       id_protege_immobile=id_protege_immobile.trim().replace("-", "_");
                       id_protege_immobile=id_protege_immobile.trim().replace("'", "_");

			//Si genera l'Individuo
			Individual immobile = createIndividual(id_protege_immobile, "Immobile");

			//Si settano le Properties
			DatatypeProperty annoCostruzioneProperty = inf.getDatatypeProperty(ns+"ANNO_DI_COSTRUZIONE");
			DatatypeProperty arredamentoProperty = inf.getDatatypeProperty(ns+"ARREDAMENTO");
			DatatypeProperty civicoProperty = inf.getDatatypeProperty(ns+"CIVICO");
			DatatypeProperty classeEnergeticaProperty = inf.getDatatypeProperty(ns+"CLASSE_ENERGETICA");
			DatatypeProperty codIstatProperty = inf.getDatatypeProperty(ns+"CODISTAT");
			DatatypeProperty denominazioneProperty = inf.getDatatypeProperty(ns+"DENOMINAZIONE");
			DatatypeProperty descrizioneProperty = inf.getDatatypeProperty(ns+"DESCRIZIONE");
			DatatypeProperty dugProperty = inf.getDatatypeProperty(ns+"DUG");
			DatatypeProperty esponenteProperty = inf.getDatatypeProperty(ns+"ESPONENTE");
			DatatypeProperty giardinoProperty = inf.getDatatypeProperty(ns+"GIARDINO");
			DatatypeProperty latitudineProperty = inf.getDatatypeProperty(ns+"LATITUDINE");
			DatatypeProperty longitudineProperty = inf.getDatatypeProperty(ns+"LONGITUDINE");
			DatatypeProperty metriProperty = inf.getDatatypeProperty(ns+"METRI_QUADRATI");
			DatatypeProperty bagniProperty = inf.getDatatypeProperty(ns+"N_BAGNI");
			DatatypeProperty livelliProperty = inf.getDatatypeProperty(ns+"N_LIVELLI");
			DatatypeProperty vaniProperty = inf.getDatatypeProperty(ns+"N_VANI");
			DatatypeProperty comuneProperty = inf.getDatatypeProperty(ns+"NOMECOMUNE");
			DatatypeProperty pianoProperty = inf.getDatatypeProperty(ns+"PIANO");
			DatatypeProperty autoProperty = inf.getDatatypeProperty(ns+"POSTO_AUTO");
			DatatypeProperty prezzoProperty = inf.getDatatypeProperty(ns+"PREZZO");




			inf.add(immobile, annoCostruzioneProperty, annoDiCostruzione);
			inf.add(immobile,arredamentoProperty,arredamento);
			inf.add(immobile,civicoProperty,civico );
			inf.add(immobile,classeEnergeticaProperty,classeEnergetica );
			inf.add(immobile,codIstatProperty,codistat);
			inf.add(immobile,denominazioneProperty,denominazione);
			inf.add(immobile,descrizioneProperty,descrizione);
			inf.add(immobile,dugProperty,dug);
			inf.add(immobile,esponenteProperty,esponente);
			inf.add(immobile,giardinoProperty,giardino);
			inf.add(immobile,latitudineProperty,latitudine);
			inf.add(immobile,longitudineProperty,longitudine);
			inf.add(immobile,metriProperty,metriquadrati);
			inf.add(immobile,livelliProperty,livelli);
			inf.add(immobile,bagniProperty,bagni);
			inf.add(immobile,vaniProperty,vani);
			inf.add(immobile,comuneProperty,comune);
			inf.add(immobile,pianoProperty,piano);
			inf.add(immobile,autoProperty,auto);
			inf.add(immobile,prezzoProperty,prezzo);




			if(proprietario!=null) {
                              Individual proprietarioIndividual  = createIndividual(proprietario,"Proprietario");
                                ObjectProperty proprietàProperty = inf.getObjectProperty(ns+"è_posseduto");
				 ObjectProperty proprietarioProperty = inf.getObjectProperty(ns+"è_proprietario");
                                 inf.add(immobile, proprietàProperty, proprietarioIndividual);
                                 inf.add(proprietarioIndividual,proprietarioProperty,immobile);
			}

			if(venditore!=null) {
				//persona che vende
				if(isPerson(venditore)==true) {


					
					ObjectProperty venditaPersonaProperty = inf.getObjectProperty(ns+"é_proposto_in_vendita");
                                        Individual venditoreIndividual  = createIndividual(venditore,"Venditore");
                                         
                                       
                                         ObjectProperty venditorePersonaProperty = inf.getObjectProperty(ns+"ha_proposto_in_vendita");
					inf.add(immobile, venditaPersonaProperty, venditoreIndividual);
                                        inf.add(venditoreIndividual,venditorePersonaProperty,immobile);
				}
				else {
					//agenzia che vende
					ObjectProperty venditaAgenziaProperty = inf.getObjectProperty(ns+"è_proposto_in_vendita");
					
                                      	Individual agenziaVenditoreIndividual=createIndividual(venditore,"Agenzia");
                                        ObjectProperty venditoreAgenziaProperty = inf.getObjectProperty(ns+"ha_proposto_in_vendita");
                                        
					
                                        inf.add(immobile, venditaAgenziaProperty,agenziaVenditoreIndividual);
                                        
                                        inf.add(agenziaVenditoreIndividual,venditoreAgenziaProperty,immobile);
                                        
				}
			}

			if(acquirente!=null) {
				//persona che compra
				ObjectProperty acquistoProperty = inf.createObjectProperty(ns+"é_acquistato");
				Individual acquirenteIndividual=createIndividual(acquirente,"Acquirente");
                                ObjectProperty acquistatoProperty = inf.createObjectProperty(ns+"ha_acquistato");
				inf.add(immobile, acquistoProperty, acquirenteIndividual);
                                inf.add(acquirenteIndividual, acquistatoProperty, immobile);
			}
			if(locatore!=null) {
				if(isPerson(venditore)==true) {

					ObjectProperty dainfittoPersonaProperty = inf.createObjectProperty(ns+"é_proposto_in_fitto");
				        Individual locatoreIndividual =createIndividual(locatore,"Locatore");
                                        ObjectProperty datoinfittoPersonaProperty = inf.createObjectProperty(ns+"ha_proposto_in_fitto");
					inf.add(immobile, dainfittoPersonaProperty, locatoreIndividual);
                                        inf.add(locatoreIndividual, datoinfittoPersonaProperty, immobile);

				}
				else {
					ObjectProperty dainfittoAgenziaProperty = inf.createObjectProperty(ns+"é_proposto_in_fitto");
					
                                         Individual agenziaLocatoreIndividual =createIndividual(locatore,"Agenzia");
                                        ObjectProperty datoinfittoAgenziaProperty = inf.createObjectProperty(ns+"ha_proposto_in_fitto");
					inf.add(immobile, dainfittoAgenziaProperty, agenziaLocatoreIndividual);
                                        inf.add(agenziaLocatoreIndividual, datoinfittoAgenziaProperty, immobile);

				}

			}
                        
                        if(locatario!=null){
                             ObjectProperty presoinfittoProperty =inf.createObjectProperty(ns+"ha_preso_in_fitto");
                             Individual locatarioIndividual =createIndividual(locatario,"Locatario");
                             inf.add(locatarioIndividual, presoinfittoProperty, immobile);
                          
                        }
                      
		}
	}

          public  boolean isPerson(String cf) {
		char c;
		int n=0;
		int lunghezza=cf.length();
		for(int i=0;i<lunghezza;i++) {
			c=cf.charAt(i);
			if(	Character.isLetter(c)) {

				n=1;
				break;
			}

		}

		if(n==0) {
			return false;
		}
		else {
			return true;
		}


	}
	
    /*
        ****
        ****
        ****
        **** QUI LE QUERY SCRITTE DA ME
        ****
        **** 
        */ 
        
        
        
        
        
        

        //Query: Dettaglio informazioni di tutte le agenzie immobiliari
          //idem => dettaglio di una specifica agenzia immobiliare
	@SuppressWarnings("unchecked")
	public JSONObject queryAgenzia_1(String q)
	{
		Query query = QueryFactory.create(q);
		QueryExecution qe = QueryExecutionFactory.create(query, inf);
		org.apache.jena.query.ResultSet results =  qe.execSelect();  

		//DEBUG PER I RISULTATI DELLA QUERY
		//ResultSetFormatter.out(System.out, results, query);

		JSONObject json = new JSONObject();

                JSONObject json_item;
                
                JSONArray json_agenzia = new JSONArray();
                JSONArray json_persona=new JSONArray();
                JSONArray json_immobile=new JSONArray();
                
                int count=0;
		while (results.hasNext())
		{
			QuerySolution tuple = results.next();
			String ragione_sociale = tuple.getLiteral("ragione_sociale").getString();
			String indirizzo = tuple.getLiteral("indirizzo").getString();
			String piva = tuple.getLiteral("piva").getString();
			String telefono = tuple.getLiteral("telefono").getString();
			String email = tuple.getLiteral("email").getString();

                        json_item=new JSONObject();
			json_item.put("ragione_sociale", ragione_sociale);
                        json_item.put("indirizzo", indirizzo);
                        json_item.put("piva", piva);
                        json_item.put("telefono", telefono);
                        json_item.put("email", email);
                        
                        json_agenzia.add(json_item);
                       
		}

                json.put("Agenzia", json_agenzia);
                json.put("Persona",json_persona);
                json.put("Immobile",json_immobile);
                
                System.out.println("Result:"+json);
		return json;
	}
        
        //Query: Dettaglio informazioni agenzia che ha proposto in fitto un immobile
        // idem => Dettaglio informazioni agenzia che ha proposto in vendita un immobile
	@SuppressWarnings("unchecked")
	public JSONObject queryAgenzia_2(String q)
	{
		Query query = QueryFactory.create(q);
		QueryExecution qe = QueryExecutionFactory.create(query, inf);
		org.apache.jena.query.ResultSet results =  qe.execSelect();  

		//DEBUG PER I RISULTATI DELLA QUERY
		//ResultSetFormatter.out(System.out, results, query);

		JSONObject json = new JSONObject();

                JSONObject json_item;
                
                JSONArray json_agenzia = new JSONArray();
                JSONArray json_persona=new JSONArray();
                JSONArray json_immobile=new JSONArray();
                
                int count=0;
                
		while (results.hasNext())
		{
			QuerySolution tuple = results.next();
                        System.out.println("Leggo tupla: "+tuple);
			String ragione_sociale = tuple.getLiteral("ragione_sociale").getString();
			String indirizzo = tuple.getLiteral("indirizzo").getString();
			String piva = tuple.getLiteral("piva").getString();
			String telefono = tuple.getLiteral("telefono").getString();
			String email = tuple.getLiteral("email").getString();
                        String immobile_lat = tuple.getLiteral("latitudine").getString();
                        String immobile_long = tuple.getLiteral("longitudine").getString();

                        json_item=new JSONObject();
			json_item.put("ragione_sociale", ragione_sociale);
                        json_item.put("indirizzo", indirizzo);
                        json_item.put("piva", piva);
                        json_item.put("telefono", telefono);
                        json_item.put("email", email);
                        json_item.put("latitudine", immobile_lat);
                        json_item.put("longitudine", immobile_long);
                        
                        json_agenzia.add(json_item);
                }
                
                json.put("Agenzia", json_agenzia);
                json.put("Persona",json_persona);
                json.put("Immobile",json_immobile);
                
                System.out.println("Result:"+json);
		return json;
	}
        
        //Query: stampa tutti gli immobili e viene usata anche per fare il dettaglio
	@SuppressWarnings("unchecked")
	public JSONObject queryImmobili_1(String q)
	{
		Query query = QueryFactory.create(q);
		QueryExecution qe = QueryExecutionFactory.create(query, inf);
		org.apache.jena.query.ResultSet results =  qe.execSelect();  

		//DEBUG PER I RISULTATI DELLA QUERY
		//ResultSetFormatter.out(System.out, results, query);

		JSONObject json = new JSONObject();

                JSONObject json_item;
                
                JSONArray json_agenzia = new JSONArray();
                JSONArray json_persona=new JSONArray();
                JSONArray json_immobile=new JSONArray();
                
                int count=0;
		while (results.hasNext())
		{
			QuerySolution tuple = results.next();
                        System.out.println("Stampo la tupla: "+tuple);
			String descrizione = tuple.getLiteral("Descrizione").getString();
			String anno_costruzione = tuple.getLiteral("Anno_di_costruzione").getString();
                        String arredamento = tuple.getLiteral("Arredamento").getString();
                        String latitudine = tuple.getLiteral("Latitudine").getString();
                        String longitudine = tuple.getLiteral("Longitudine").getString();
                        String codistat = tuple.getLiteral("Codistat").getString();
                        String giardino = tuple.getLiteral("giardino").getString();
                        String classe_energetica = tuple.getLiteral("classe_energetica").getString();
                        String dug = tuple.getLiteral("Dug").getString();
                        String piano = tuple.getLiteral("piano").getString();
                        String denominazione = tuple.getLiteral("Denominazione").getString();
                        String civico = tuple.getLiteral("Civico").getString();
                        String metri_quadrati = tuple.getLiteral("Metri_quad").getString();
                        String num_bagni = tuple.getLiteral("Num_bagni").getString();
                        String num_vani = tuple.getLiteral("Num_vani").getString();
                        String num_livelli = tuple.getLiteral("Num_livelli").getString();
                        String nome_comune = tuple.getLiteral("Nome_comune").getString();
                        String posto_auto = tuple.getLiteral("Posto_auto").getString();
                        String prezzo = tuple.getLiteral("Prezzo").getString();
                        String proprietario = tuple.getLiteral("cf").getString();
                        String nome = tuple.getLiteral("nome").getString();
                        String cognome = tuple.getLiteral("cognome").getString();
                                
                                
			json_item=new JSONObject();
			json_item.put("descrizione",descrizione);
                        json_item.put("anno_costruzione", anno_costruzione);
                        json_item.put("arredamento", arredamento);
                        json_item.put("latitudine", latitudine);
                        json_item.put("longitudine", longitudine);
                        json_item.put("codistat", codistat);
                        json_item.put("classe_energetica", classe_energetica);
                        json_item.put("giardino", giardino);
                        json_item.put("dug", dug);
                        json_item.put("piano", piano);
                        json_item.put("denominazione", denominazione);
                        json_item.put("civico", civico);
                        json_item.put("metri_quadrati", metri_quadrati);
                        json_item.put("num_bagni", num_bagni);
                        json_item.put("num_vani", num_vani);
                        json_item.put("num_livelli", num_livelli);
                        json_item.put("nome_comune", nome_comune);
                        json_item.put("posto_auto", posto_auto);
                        json_item.put("prezzo", prezzo);
                        json_item.put("cf", proprietario);
                        json_item.put("nome", nome);
                        json_item.put("cognome", cognome);
                        
                         json_immobile.add(json_item);
                        
                }
                
		json.put("Agenzia", json_agenzia);
                json.put("Persona",json_persona);
                json.put("Immobile",json_immobile);
                
                System.out.println("Result:"+json);
		return json;
	}
        
        //Query: 
	@SuppressWarnings("unchecked")
	public JSONObject queryImmobili_3(String q)
	{
		Query query = QueryFactory.create(q);
		QueryExecution qe = QueryExecutionFactory.create(query, inf);
		org.apache.jena.query.ResultSet results =  qe.execSelect();  

		//DEBUG PER I RISULTATI DELLA QUERY
		//ResultSetFormatter.out(System.out, results, query);

		JSONObject json = new JSONObject();

                JSONObject json_item;
                
                JSONArray json_agenzia = new JSONArray();
                JSONArray json_persona=new JSONArray();
                JSONArray json_immobile=new JSONArray();
                
                int count=0;
		while (results.hasNext())
		{
			QuerySolution tuple = results.next();
                        System.out.println("Stampo la tupla: "+tuple);
			String descrizione = tuple.getLiteral("Descrizione").getString();
			String anno_costruzione = tuple.getLiteral("Anno_di_costruzione").getString();
                        String arredamento = tuple.getLiteral("Arredamento").getString();
                        String latitudine = tuple.getLiteral("Latitudine").getString();
                        String longitudine = tuple.getLiteral("Longitudine").getString();
                        String codistat = tuple.getLiteral("Codistat").getString();
                        String giardino = tuple.getLiteral("giardino").getString();
                        String classe_energetica = tuple.getLiteral("classe_energetica").getString();
                        String dug = tuple.getLiteral("Dug").getString();
                        String piano = tuple.getLiteral("piano").getString();
                        String denominazione = tuple.getLiteral("Denominazione").getString();
                        String civico = tuple.getLiteral("Civico").getString();
                        String metri_quadrati = tuple.getLiteral("Metri_quad").getString();
                        String num_bagni = tuple.getLiteral("Num_bagni").getString();
                        String num_vani = tuple.getLiteral("Num_vani").getString();
                        String num_livelli = tuple.getLiteral("Num_livelli").getString();
                        String nome_comune = tuple.getLiteral("Nome_comune").getString();
                        String posto_auto = tuple.getLiteral("Posto_auto").getString();
                        String prezzo = tuple.getLiteral("Prezzo").getString();
                        String piva = tuple.getLiteral("piva").getString();
                        String ragione_sociale = tuple.getLiteral("ragione_sociale").getString();
                                
                                
			json_item=new JSONObject();
			json_item.put("descrizione",descrizione);
                        json_item.put("anno_costruzione", anno_costruzione);
                        json_item.put("arredamento", arredamento);
                        json_item.put("latitudine", latitudine);
                        json_item.put("longitudine", longitudine);
                        json_item.put("codistat", codistat);
                        json_item.put("classe_energetica", classe_energetica);
                        json_item.put("giardino", giardino);
                        json_item.put("dug", dug);
                        json_item.put("piano", piano);
                        json_item.put("denominazione", denominazione);
                        json_item.put("civico", civico);
                        json_item.put("metri_quadrati", metri_quadrati);
                        json_item.put("num_bagni", num_bagni);
                        json_item.put("num_vani", num_vani);
                        json_item.put("num_livelli", num_livelli);
                        json_item.put("nome_comune", nome_comune);
                        json_item.put("posto_auto", posto_auto);
                        json_item.put("prezzo", prezzo);
                        json_item.put("piva", piva);
                        json_item.put("ragione_sociale", ragione_sociale);
                        
                         json_immobile.add(json_item);
                        
                }
                
		json.put("Agenzia", json_agenzia);
                json.put("Persona",json_persona);
                json.put("Immobile",json_immobile);
		return json;
	}
        
      
        
        //Query: Stampa tutti le informazioni dei proprietari e fa anche il dettaglio per cf
	@SuppressWarnings("unchecked")
	public JSONObject queryProprietari_1(String q)
	{
		Query query = QueryFactory.create(q);
		QueryExecution qe = QueryExecutionFactory.create(query, inf);
		org.apache.jena.query.ResultSet results =  qe.execSelect();  

		//DEBUG PER I RISULTATI DELLA QUERY
		//ResultSetFormatter.out(System.out, results, query);

		JSONObject json = new JSONObject();

                JSONObject json_item;
                
                JSONArray json_agenzia = new JSONArray();
                JSONArray json_persona=new JSONArray();
                JSONArray json_immobile=new JSONArray();
                
                int count=0;
                
		while (results.hasNext())
		{
			QuerySolution tuple = results.next();
                        System.out.println("Leggo tupla: "+tuple);
                         
			//String immobile = tuple.getLiteral("immobile").getString();
			String nome = tuple.getLiteral("nome").getString();
			String cognome = tuple.getLiteral("cognome").getString();
			String indirizzo = tuple.getLiteral("indirizzo").getString();
			String email = tuple.getLiteral("email").getString();
                        String telefono = tuple.getLiteral("telefono").getString();
                        String cf = tuple.getLiteral("cf").getString();
                        String lat_immobile=tuple.getLiteral("latitudine").getString();
                        String long_immobile=tuple.getLiteral("longitudine").getString();

                        json_item=new JSONObject();
			//json_item.put("immobile", immobile);
                        json_item.put("nome", nome);
                        json_item.put("cognome", cognome);
                        json_item.put("indirizzo", indirizzo);
                        json_item.put("email", email);
                        json_item.put("telefono", telefono);
                        json_item.put("cf", cf);
                        json_item.put("latitudine", lat_immobile);
                        json_item.put("longitudine", long_immobile);
                        json_persona.add(json_item);
                }
                
                json.put("Agenzia", json_agenzia);
                json.put("Persona",json_persona);
                json.put("Immobile",json_immobile);
		return json;
	}
        
      //Query: Dettaglio utente
	@SuppressWarnings("unchecked")
	public JSONObject queryDettaglioUtente(String q)
	{
		Query query = QueryFactory.create(q);
		QueryExecution qe = QueryExecutionFactory.create(query, inf);
		org.apache.jena.query.ResultSet results =  qe.execSelect();  

		//DEBUG PER I RISULTATI DELLA QUERY
		//ResultSetFormatter.out(System.out, results, query);

		JSONObject json = new JSONObject();

                JSONObject json_item;
                
                JSONArray json_agenzia = new JSONArray();
                JSONArray json_persona=new JSONArray();
                JSONArray json_immobile=new JSONArray();
                
                int count=0;
                
		while (results.hasNext())
		{
			QuerySolution tuple = results.next();
                        System.out.println("Leggo tupla: "+tuple);
                         
			//String immobile = tuple.getLiteral("immobile").getString();
			String nome = tuple.getLiteral("nome").getString();
			String cognome = tuple.getLiteral("cognome").getString();
			String indirizzo = tuple.getLiteral("indirizzo").getString();
			String email = tuple.getLiteral("email").getString();
                        String telefono = tuple.getLiteral("telefono").getString();
                        String cf = tuple.getLiteral("cf").getString();

                        json_item=new JSONObject();
			//json_item.put("immobile", immobile);
                        json_item.put("nome", nome);
                        json_item.put("cognome", cognome);
                        json_item.put("indirizzo", indirizzo);
                        json_item.put("email", email);
                        json_item.put("telefono", telefono);
                        json_item.put("cf", cf);
                        json_persona.add(json_item);
                }
                
                json.put("Agenzia", json_agenzia);
                json.put("Persona",json_persona);
                json.put("Immobile",json_immobile);
		return json;
	}
       
        
        /*
        ***
        ***
        ***
        *** FINE DEL CODICE CHE HO AGGIUNTO
        ***
        ***
        ***
        ***
        */
        
        //Funzione di salvataggio dell'ontologia
	public void save() throws FileNotFoundException
	{
		inf.write(new FileOutputStream("/Users/depiano/Desktop/Project_WebSemantico/OntologyOWL/ProgettoFinale_NEW.owl"));
	}

	private String ns;
	private String filename;
	private OntModel inf;

	private JDBCManager jdbc;
}
