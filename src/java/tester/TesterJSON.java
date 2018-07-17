package tester;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TesterJSON {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String received = "{\"query\":\"editors\", \"editor\":\"DC\"}";
		
		
		
		String query1 = "x", query2 = "y";
		
		String ritorno = "";
		try 
		{
			JSONParser parser = new JSONParser();
			Object object = parser.parse(received);
			JSONObject root = (JSONObject) object;
			//Ottengo l'operatore e lo schema selezionato
			String operazione = (String) root.get("query");

			if (operazione.equalsIgnoreCase("editors"))
				ritorno = query1;
			else
				ritorno = query2;
		} 
		catch (Exception e) 
		{	
			e.printStackTrace();
		}
		
		//DEBUG per il valore di ritorno
		System.out.println(ritorno);
	}

}
