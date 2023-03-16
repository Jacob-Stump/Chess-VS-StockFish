import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
public class createJSON {

	@SuppressWarnings("unchecked")
	public static void createdJSON() throws IOException {

		URL url = new URL("https://127.0.0.1:5500");
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		
		JSONObject file = new JSONObject();
		JSONArray list = new JSONArray();
		list.add(Board.lastMove);
		
		file.put("moves", list);
		
		try(FileWriter jfile = new FileWriter("move.json")) {
			
			jfile.write(file.toString());
			jfile.flush();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(file);
	}
	
}
	