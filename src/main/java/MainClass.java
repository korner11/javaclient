import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * Created by Michal on 30.10.2017.
 */
public class MainClass {
    public static class Pojo
    {
        String name;
        String image;
        //generate setter and getters
    }

    public static class Wifi{
        String ssid;
        String bssid;
        Double strenght;

        public Wifi(String ssid, String bssid, Double strenght) {
            this.ssid = ssid;
            this.bssid = bssid;
            this.strenght = strenght;
        }
    }

    public static void main(String[] args) {

        getImagesBySsid();

    }

    private static void getImagesBySsid(){

        try {


        Wifi wifi1 = new Wifi("bla","test2",1.00);
            Wifi wifi2 = new Wifi("bla","test5",1.00);
        List<Wifi> wifiList = Arrays.asList(wifi1,wifi2);

        String postUrl = "http://localhost:8080/images/getImagesByBssid/";// put in your url
        Gson gson = new Gson();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        String json = gson.toJson(wifiList);
        StringEntity postingString = new StringEntity(json);//gson.tojson() converts your pojo to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");

        HttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            JsonParser parser = new JsonParser();
            JsonArray tradeElement = parser.parse(responseString).getAsJsonArray();
            System.out.println(tradeElement);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void addImage() {
        try {
                File imageFile = new File("testImages/image.jpg");
                FileInputStream f = new FileInputStream(imageFile);

                byte b[] = new byte[f.available()];
                f.read(b);

                Pojo pojo = new Pojo();
                pojo.name = "imageTest";
                pojo.image =  Base64.getEncoder().encodeToString(b);

                String postUrl = "http://localhost:8080/images/";// put in your url
                Gson gson = new Gson();
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(postUrl);
                StringEntity postingString = new StringEntity(gson.toJson(pojo));//gson.tojson() converts your pojo to json
                post.setEntity(postingString);
                post.setHeader("Content-type", "application/json");

                HttpResponse response = httpClient.execute(post);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void writeFile(){

        try {

            String postUrl = "http://localhost:8080/images/";// put in your url

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(postUrl);

            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            JsonParser parser = new JsonParser();
            JsonArray tradeElement = parser.parse(responseString).getAsJsonArray();
            JsonObject obj = tradeElement.get(1).getAsJsonObject();

            JsonObject oimage = obj.get("image").getAsJsonObject();

            String base = oimage.get("data").getAsString();
            System.out.println(base);

        BufferedImage image = null;
        byte[] imageByte;

        BASE64Decoder decoder = new BASE64Decoder();
        imageByte = decoder.decodeBuffer(base);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        image = ImageIO.read(bis);
        bis.close();

        File outputfile = new File("testimages/imageResponse.jpg");
        ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
