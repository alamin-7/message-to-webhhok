package com.webhook5.webhok.ok;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.net.URI;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

	private static final String URL = "https://chat.googleapis.com/v1/spaces/AAAAOw3GRJE/messages?key=AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI&token=JJeY70_vTVejAeElsFVIn7CUdou5Z-ScdDXVdHNSdqQ";
	private static final Gson gson = new Gson();
	private static final HttpClient client = HttpClient.newHttpClient();

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(Application.class, args);

		try {

			//String apiUrl = "http://13.208.128.137:19999/api/v2/alerts?options=summary,instances,values,minify&status=raised";

			String apiUrl = "http://52.77.4.211:19999/api/v2/alerts?options=summary,instances,values,minify&status=raised";

			URL url = new URL(apiUrl);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			System.out.println("Response Code: " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Print the response data
			System.out.println("Response Data: " + response);
			JSONObject jsonResponse = new JSONObject(response.toString());

          /*  for (Iterator it = jsonResponse.keys(); it.hasNext(); ) {
                String key = (String) it.next();
                System.out.println("Key: " + key);

                // Get the value associated with the key
                Object value = jsonResponse.get(key);

                // Check the type of the value
                if (value instanceof JSONArray) {
                    // If the value is an array, iterate through its elements
                    JSONArray jsonArray = (JSONArray) value;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        System.out.println("  Value with array: " + jsonArray.get(i));
                    }
                } else {
                    // If the value is not an array, simply print it
                    System.out.println("  Value: " + value);
                }
            }*/

			JSONArray alertInstances = jsonResponse.getJSONArray("alert_instances");
			JSONArray alertNodes = jsonResponse.getJSONArray("nodes");

           /* System.out.println("Key: alert_instances");
            System.out.println("Value: " + alertInstances.toString());*/

			for (int i = 0; i < alertInstances.length(); i++) {
				JSONObject instance = alertInstances.getJSONObject(i);

				System.out.println("alert " + (i + 1) + ": " + instance.toString());
				System.out.println("alert " + (i + 1) + " ID: " + instance.getString("st"));
				System.out.println("alert " + (i + 1) + " Name: " + instance.getString("ch"));

				System.out.println("alert " + (i + 1) + " Instance_ID: " + instance.getInt("ni"));

				int nodeNum = instance.getInt("ni");

				JSONObject nodeName = alertNodes.getJSONObject(nodeNum);

				String node = nodeName.getString("nm");
				String status = instance.getString("st");
				String alert =  instance.getString("ch_n");
				System.out.println(nodeName.getString("nm"));


				String key = "text";
				String value =  " " + node + " is in " + status + " for "+ alert + " ";
				String message = gson.toJson(Map.of(key,value));

				HttpRequest request = HttpRequest.newBuilder(
								URI.create(URL))
						.header("accept", "application/json; charset=UTF-8")
						.POST(HttpRequest.BodyPublishers.ofString(message))
						.build();

				HttpResponse<String> response1 = client.send(request, HttpResponse.BodyHandlers.ofString());

			}
			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

/*
		String key = "text";
		String value = "Hello!";
		String message = gson.toJson(Map.of(key,value));

		HttpRequest request = HttpRequest.newBuilder(
						URI.create(URL))
				.header("accept", "application/json; charset=UTF-8")
				.POST(HttpRequest.BodyPublishers.ofString(message))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
*/

		//System.out.println(response.body());


		System.out.print("hello");
	}

}
