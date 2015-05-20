import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class mytesting {

	public static void main(String[] args) {

        URL url;

        try {
            // get URL content

            String a="http://google.com";
            url = new URL(a);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                    System.out.println(inputLine);
            }
            br.close();

            System.out.println("Done");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
