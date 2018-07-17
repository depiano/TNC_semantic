/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import business.JenaManager;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static javax.ws.rs.client.Entity.json;
import org.json.simple.JSONObject;

/**
 *
 * @author depiano
 */
@WebServlet(name = "Helloworld", urlPatterns = {"/Helloworld"})
public class Helloworld extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private JenaManager dealer;

	private static final String ONTOLOGYIRI = "http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70";
	private static final String ONTOLOGYNS = "<"+ONTOLOGYIRI+"\"#\";";
	private static final String ONTOFILE = "/Users/depiano/Desktop/Project_WebSemantico/OntologyOWL/ProgettoFinale_NEW.owl";
        	
        
    public Helloworld()
    { 
		super();
                //Istanzio il gestore
		dealer = new JenaManager(ONTOLOGYIRI, ONTOFILE);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Write response header.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
                        
        // System.out.println(""+ item.toJSONString());
        
        JSONObject json_result = new JSONObject();
        json_result.put("query_id",request.getParameter("query_id"));
        JSONObject result_query;
        
        result_query=executeQuery(request.getParameter("query_id"));
        System.out.println("Result query:"+result_query);
        json_result.put("result_query",result_query);
        // Write response body.
        response.getWriter().write(json_result.toJSONString());       
                       
    }
    
    private JSONObject executeQuery(String query_id)
    {
		//write object json
                System.out.println("QUERY_ID:"+query_id);
                String query="";
                JSONObject json_query = null;
                boolean dett_agenzia = query_id.contains("dettaglioAgenzia");
                boolean dett_immobili = query_id.contains("dettaglioImmobili");
                boolean dett_user = query_id.contains("dettaglioUtente");
                String piva="";
                String cf_user="";
                String immobile_lat="";
                String immobile_long="";
                
                if(dett_agenzia)
                {     
                    String[] parts = query_id.split("-");
                    query_id = parts[0]; // dettaglioAgenzia
                    piva = parts[1]; // piva      
                }
                
                if(dett_user)
                {     
                    String[] parts = query_id.split("-");
                    query_id = parts[0]; // dettaglioUtente
                    cf_user = parts[1]; // cf     
                }
                
                if(dett_immobili)
                {     
                    String[] parts = query_id.split("-");
                    query_id = parts[0]; // dettaglioImmobili
                    immobile_lat = parts[1]; // latitudine
                    immobile_long= parts[2]; //longitudine
                }
                    
                switch(query_id)
                {
                    case "dettaglioAgenzia":
                        //Funziona
                        query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                                +" SELECT ?ragione_sociale ?indirizzo ?piva ?telefono ?email"
                                +" WHERE { ?subject a ws:Agenzia."
                                +" ?subject ws:RAGIONE_SOCIALE ?ragione_sociale."
                                +" ?subject ws:INDIRIZZO ?indirizzo."
                                +" ?subject ws:P_IVA ?piva."
                                +" ?subject ws:EMAIL ?email."
                                +" ?subject ws:TELEFONO ?telefono."
                                +" FILTER (?piva=\""+piva+"\").}";
                         json_query = dealer.queryAgenzia_1(query);
                          break;
                        case "dettaglioUtente":
                        //Funziona
                        query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?nome ?cognome ?indirizzo ?telefono ?email ?cf"
                              +" WHERE { ?subject ws:è_proprietario ?immobile."
                              +" ?subject ws:NOME ?nome."
                              +" ?subject ws:COGNOME  ?cognome."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?subject ws:CF ?cf."
                              +" ?subject ws:EMAIL ?email."
                              +" FILTER (?cf=\""+cf_user+"\").}";
                      json_query = dealer.queryDettaglioUtente(query);
                          break;
                    case "dettaglioImmobili":
                        //Funziona
                        query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER (?Longitudine = '"+immobile_long+"')."
                              +" FILTER (?Latitudine = '"+immobile_lat+"').}";
                        json_query = dealer.queryImmobili_1(query);
                        break;
                  case "immobili_1":
                      //Dettaglio informazioni di tutti gli immobili 
                      //Funziona
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                       json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_2":
                      //Dettaglio informazioni di tutti gli immobili proposti in vendita da una persona 
                        query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                                +" SELECT  ?Descrizione ?cf ?Anno_di_costruzione ?Arredamento ?Codistat ?Latitudine ?Longitudine ?Nome_comune ?Dug ?Denominazione ?Civico ?giardino ?Metri_quad ?Num_vani ?piano ?Num_livelli  ?Num_bagni ?classe_energetica ?Posto_auto ?Prezzo ?nome ?cognome"
                                +" WHERE{ ?subject ws:è_proposto_in_vendita ?venditore."
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
                                +" ?venditore a ws:Venditore."
                                +" ?venditore ws:NOME ?nome."
                                +" ?venditore ws:COGNOME ?cognome."
                                + "?venditore ws:CF ?cf.}";
		json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_3":
                      //Dettaglio informazioni di tutti gli immobili proposti in vendita da un’agenzia
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                                +" SELECT  ?Descrizione ?piva ?Anno_di_costruzione ?Arredamento ?Codistat ?Latitudine ?Longitudine ?Nome_comune ?Dug ?Denominazione ?Civico ?giardino ?Metri_quad ?Num_vani ?piano ?Num_livelli  ?Num_bagni ?classe_energetica ?Posto_auto ?Prezzo ?ragione_sociale"
                                +" WHERE{ ?subject ws:è_proposto_in_vendita ?venditore."
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
                                +" ?venditore a ws:Agenzia."
                                +" ?venditore ws:RAGIONE_SOCIALE ?ragione_sociale."
                                +" ?venditore ws:P_IVA ?piva.}";
                      json_query = dealer.queryImmobili_3(query);
                          break;
                  case "immobili_4":
                      //Dettaglio informazioni di tutti gli immobili proposti in fitto da una persona 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                                +" SELECT  ?Descrizione ?cf ?Anno_di_costruzione ?Arredamento ?Codistat ?Latitudine ?Longitudine ?Nome_comune ?Dug ?Denominazione ?Civico ?giardino ?Metri_quad ?Num_vani ?piano ?Num_livelli  ?Num_bagni ?classe_energetica ?Posto_auto ?Prezzo ?nome ?cognome"
                                +" WHERE{ ?subject ws:è_proposto_in_fitto ?locatore."
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
                                +" ?locatore a ws:Locatore."
                                +" ?locatore ws:NOME ?nome."
                                +" ?locatore ws:COGNOME ?cognome."
                                + "?locatore ws:CF ?cf.}";
                      json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_5":
                      //Dettaglio informazioni di tutti gli immobili proposti in fitto da un’ agenzia
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT  ?Descrizione ?piva ?Anno_di_costruzione ?Arredamento ?Codistat ?Latitudine ?Longitudine ?Nome_comune ?Dug ?Denominazione ?Civico ?giardino ?Metri_quad ?Num_vani ?piano ?Num_livelli  ?Num_bagni ?classe_energetica ?Posto_auto ?Prezzo ?ragione_sociale"
                              +" WHERE{ ?subject ws:è_proposto_in_fitto ?locatore."
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
                                +" ?locatore a ws:Agenzia."
                                +" ?locatore ws:RAGIONE_SOCIALE ?ragione_sociale."
                                +" ?locatore ws:P_IVA ?piva.}";
                       json_query = dealer.queryImmobili_3(query);
                          break;
                  case "immobili_6":
                      //Dettaglio informazioni di tutti gli immobili con una specifica misura (metri quadrati) 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER (?Metri_quad = '90')."
                              +"}";
                      json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_7":
                      //Dettaglio informazioni di tutti gli immobili costruiti in un determinato anno 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER (?Anno_di_costruzione = 2000).}";
                      json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_8":
                      //Dettaglio informazioni di tutti gli immobili già arredati 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER (?Arredamento = true).}";
                      json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_9":
                      //Dettaglio informazioni di tutti gli immobili con una determinata classe energetica 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +"FILTER (?classe_energetica = 'E').}";
                           json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_10":
                      //Dettaglio informazioni di tutti gli immobili con giardino 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +"FILTER (?giardino = true).}";
                       json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_11":
                      //Dettaglio informazioni di tutti gli immobili con un determinato numero di bagni
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER (?Num_bagni >=1).}";
                      json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_12":
                      //Dettaglio informazioni di tutti gli immobili con un determinato numero di livelli 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER (?Num_livelli =1).}";
                      json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_13":
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER (?Num_vani >=1).}";
                      json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_14":
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER (?piano='TERRA').}";
                      json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_15":
                      //Dettaglio informazioni di tutti gli immobili con un determinato prezzo 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER(?Prezzo>=500).}";
                       json_query = dealer.queryImmobili_1(query);
                          break;
                  case "immobili_16":
                      //Dettaglio informazioni di tutti gli immobili dotati di posto auto 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
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
                              + "?proprietario ws:CF ?cf."
                              +" FILTER (?Posto_auto='DISPONIBILE').}";
                      json_query = dealer.queryImmobili_1(query);
                      break;
                  case "Agenzia_Immobiliare_1":
                      //Visualizza informazioni di tutte le agenzie immobiliari
                      query="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                              +"PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                              +"PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                              +"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                              +" SELECT ?ragione_sociale ?indirizzo ?piva ?telefono ?email"
                              +" WHERE { ?subject a ws:Agenzia."
                              +" ?subject ws:RAGIONE_SOCIALE ?ragione_sociale."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:P_IVA ?piva."
                              +" ?subject ws:EMAIL ?email."
                              +" ?subject ws:TELEFONO ?telefono.}";

			json_query = dealer.queryAgenzia_1(query);
                          break;
                  case "Agenzia_Immobiliare_2":
                      //Visualizza informazioni agenzia che ha proposto in fitto un immobile
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?ragione_sociale ?indirizzo ?piva ?telefono ?email ?immobile ?dug ?denominazione ?civico ?esponente ?latitudine ?longitudine"
                              +" WHERE { ?subject ws:ha_proposto_in_fitto ?immobile."
                              +" ?subject ws:RAGIONE_SOCIALE ?ragione_sociale."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:P_IVA ?piva."
                              +" ?subject ws:EMAIL ?email."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile  ws:DUG ?dug."
                              +" ?immobile  ws:DENOMINAZIONE ?denominazione."
                              +" ?immobile ws:CIVICO ?civico."
                              +" ?immobile ws:LATITUDINE ?latitudine."
                              +" ?immobile ws:LONGITUDINE ?longitudine."
                              +" ?immobile ws:ESPONENTE ?esponente.}";
                      json_query = dealer.queryAgenzia_2(query);
                          break;
                  case "Agenzia_Immobiliare_3":
                      //Dettaglio informazioni agenzia che ha proposto in vendita un immobile
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?ragione_sociale ?indirizzo ?piva ?telefono ?email ?immobile ?dug ?denominazione ?civico ?esponente ?latitudine ?longitudine"
                              +" WHERE { ?subject ws:ha_proposto_in_vendita ?immobile."
                              +" ?subject ws:RAGIONE_SOCIALE ?ragione_sociale."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:P_IVA ?piva."
                              +" ?subject ws:EMAIL ?email."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile ws:LATITUDINE ?latitudine."
                              +" ?immobile ws:LONGITUDINE ?longitudine."
                              +" ?immobile  ws:DUG ?dug."
                              +" ?immobile  ws:DENOMINAZIONE ?denominazione."
                              +" ?immobile ws:CIVICO ?civico."
                              +" ?immobile ws:ESPONENTE ?esponente."
                              +"}";
                      json_query = dealer.queryAgenzia_2(query);
                          break;
                  case "proprietari_1":
                      //Dettaglio informazioni Proprietari
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?immobile ?latitudine ?longitudine ?nome ?cognome ?indirizzo ?telefono ?email ?cf"
                              +" WHERE { ?subject ws:è_proprietario ?immobile."
                              +" ?subject ws:NOME ?nome."
                              +" ?subject ws:COGNOME  ?cognome."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?subject ws:CF ?cf."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile ws:LATITUDINE ?latitudine."
                              +" ?immobile ws:LONGITUDINE ?longitudine."
                              +" ?subject ws:EMAIL ?email.}";
                      json_query = dealer.queryProprietari_1(query);
                          break;
                  case "proprietari_2":
                      //Dettaglio informazioni proprietario specifico 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?immobile ?latitudine ?longitudine ?nome ?cognome ?indirizzo ?telefono ?email ?cf"
                              +" WHERE { ?subject ws:è_proprietario ?immobile."
                              +" ?subject ws:NOME ?nome."
                              +" ?subject ws:COGNOME  ?cognome."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?subject ws:CF ?cf."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile ws:LATITUDINE ?latitudine."
                              +" ?immobile ws:LONGITUDINE ?longitudine."
                              +" ?subject ws:EMAIL ?email."
                              +" FILTER (?nome='Mario')."
                              +" FILTER (?cognome='Rossi').}";
                      json_query = dealer.queryProprietari_1(query);
                          break;
                  case "proprietari_3":
                      //Dettaglio informazione proprietario di un determinato immobile 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?immobile ?latitudine ?longitudine ?nome ?cognome ?indirizzo ?telefono ?email ?cf"
                              +" WHERE { ?subject ws:è_proprietario ?immobile."
                              +" ?subject ws:NOME ?nome."
                              +" ?subject ws:COGNOME  ?cognome."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?subject ws:EMAIL ?email."
                              +" ?subject ws:CF ?cf."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile ws:LATITUDINE ?latitudine."
                              +" ?immobile ws:LONGITUDINE ?longitudine."
                              +" FILTER (?latitudine='100000000004')."
                              +" FILTER (?longitudine='100000000005').}";
                      json_query = dealer.queryProprietari_1(query);
                          break;
                  case "proprietari_4":
                      //Dettaglio informazioni persona che ha venduto un immobile 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?immobile ?latitudine ?longitudine ?nome ?cognome ?indirizzo ?telefono ?email ?cf"
                              +" WHERE {?subject ws:ha_venduto ?immobile."
                              +" ?subject ws:NOME ?nome."
                              +" ?subject ws:COGNOME  ?cognome."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?subject ws:EMAIL ?email."
                              +" ?subject ws:CF ?cf."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile ws:LATITUDINE ?latitudine."
                              +" ?immobile ws:LONGITUDINE ?longitudine."
                              +"}";
                      json_query = dealer.queryProprietari_1(query);
                          break;
                  case "proprietari_5":
                      //Dettaglio informazioni persona che ha proposto in fitto un immobile 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?immobile ?latitudine ?longitudine ?nome ?cognome ?indirizzo ?telefono ?email ?cf"
                              +" WHERE {?subject ws:ha_proposto_in_fitto ?immobile."
                              +" ?subject ws:NOME ?nome."
                              +" ?subject ws:COGNOME  ?cognome."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?subject ws:EMAIL ?email."
                              +" ?subject ws:CF ?cf."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile ws:LATITUDINE ?latitudine."
                              +" ?immobile ws:LONGITUDINE ?longitudine.}";
                      json_query = dealer.queryProprietari_1(query);
                          break;
                  case "proprietari_6":
                      //Dettaglio informazioni persona che ha proposto in vendita un immobile 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?immobile ?latitudine ?longitudine ?nome ?cognome ?indirizzo ?telefono ?email ?cf"
                              +" WHERE { ?subject ws:ha_proposto_in_vendita ?immobile."
                              +" ?subject ws:NOME ?nome."
                              +" ?subject ws:COGNOME  ?cognome."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?subject ws:EMAIL ?email."
                              +" ?subject ws:CF ?cf."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile ws:LATITUDINE ?latitudine."
                              +" ?immobile ws:LONGITUDINE ?longitudine.}";
                      json_query = dealer.queryProprietari_1(query);
                          break;
                  case "proprietari_7":
                      //Dettaglio informazioni persona che ha acquistato un immobile 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?immobile ?latitudine ?longitudine ?nome ?cognome ?indirizzo ?telefono ?email ?cf"
                              +" WHERE { ?subject ws:ha_acquistato ?immobile."
                              +" ?subject ws:NOME ?nome."
                              +" ?subject ws:COGNOME  ?cognome."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?subject ws:EMAIL ?email."
                              +" ?subject ws:CF ?cf."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile  ws:LATITUDINE ?latitudine."
                              +" ?immobile  ws:LONGITUDINE ?longitudine."
                              +"}";
                      json_query = dealer.queryProprietari_1(query);
                          break;
                  case "proprietari_8":
                      //Dettaglio informazioni persona che ha preso in fitto un immobile 
                      query="PREFIX ws: <http://www.semanticweb.org/vincenzobevilacqua/ontologies/2018/5/untitled-ontology-70#>"
                              +" SELECT DISTINCT ?immobile ?latitudine ?longitudine ?nome ?cognome ?indirizzo ?telefono ?email ?cf"
                              +" WHERE { ?subject ws:ha_preso_in_fitto ?immobile."
                              +" ?subject ws:NOME ?nome."
                              +" ?subject ws:COGNOME  ?cognome."
                              +" ?subject ws:INDIRIZZO ?indirizzo."
                              +" ?subject ws:TELEFONO ?telefono."
                              +" ?subject ws:EMAIL ?email."
                              +" ?subject ws:CF ?cf."
                              +" ?immobile a ws:Immobile."
                              +" ?immobile ws:LATITUDINE ?latitudine."
                              +" ?immobile ws:LONGITUDINE ?longitudine.}";
                     json_query = dealer.queryProprietari_1(query);
                          break;
                  default:
                     System.out.print("Errore nell'id della query.");
                }
		
		
		return json_query;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}