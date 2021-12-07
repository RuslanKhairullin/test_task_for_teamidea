import com.google.gson.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        String urlString = "https://api.openweathermap.org/data/2.5/onecall?" +
                "lat=55.78&lon=49.12&units=metric&dt=1586468027&appid=9ecc8e71aea14207b1167a8c8260eabb";
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(result.toString());
            JsonElement daily = jsonObject.get("daily");
            JsonArray array = daily.getAsJsonArray();
            Iterator iterator = array.iterator();
            List<JsonElement> js = new ArrayList<>();
            while (iterator.hasNext()) {
                JsonElement jss = (JsonElement) iterator.next();
                js.add(jss);
            }
            Optional<JsonElement> temp = js.stream().min((p1, p2) -> Math.abs(p1.getAsJsonObject().get("feels_like").getAsJsonObject().get("night").getAsInt() - p2.getAsJsonObject().get("temp").getAsJsonObject().get("night").getAsInt()));
            String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(temp.get().getAsJsonObject().get("dt").getAsLong() * 1000));
            Float dif = Math.abs(temp.get().getAsJsonObject().get("feels_like").getAsJsonObject().get("night").getAsFloat() - temp.get().getAsJsonObject().get("temp").getAsJsonObject().get("night").getAsFloat());
            Optional<JsonElement> daylong = js.stream().limit(5).max((p1, p2) -> Math.abs(p1.getAsJsonObject().get("sunrise").getAsInt() - p2.getAsJsonObject().get("sunset").getAsInt()));
            String date2 = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(daylong.get().getAsJsonObject().get("dt").getAsLong() * 1000));
            System.out.println(date);
            System.out.println(dif);
            System.out.println(date2);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
