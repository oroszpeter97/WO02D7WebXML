package wo02d7JSON.JSONParseWo02d7;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONReadWo02d7 
{
    public static void main(String[] args)
    {
        try(FileReader reader = new FileReader("orarendWO02D7.json"))
        {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject root = (JSONObject) jsonObject.get("OP_orarend");
            JSONArray lessons = (JSONArray) root.get("ora");

            System.out.println("WO02D7 Órarend 2025 Ősz:\n");
            
            for (int i = 0; i < lessons.size(); i++)
            {
                JSONObject lesson = (JSONObject) lessons.get(i);
                JSONObject time = (JSONObject) lessons.get("idopont");
                System.out.println("Tárgy: " + lessons.get("targy"));    
                System.out.println("Időpont: " + lessons.get("nap") + " " + lessons.get("tol") + "-" + lessons.get("nap"));   
                System.out.println("Helyszín: " + lessons.get("helyszin"));   
                System.out.println("Oktató: " + lessons.get("oktato"));   
                System.out.println("Szak: " + lessons.get("szak") + "\n");   
            }
        }
    }
}