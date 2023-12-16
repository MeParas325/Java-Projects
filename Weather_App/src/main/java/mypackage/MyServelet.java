package mypackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServelet
 */
public class MyServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServelet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String city = request.getParameter("city");
		System.out.println("City name is: " + city);
		
		// API setup and integration
		String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&APPID=65f7bc7bd6f48e432c80620edb7f7406";
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		
//		// read data
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
//		
		StringBuilder responseContent = new StringBuilder();
		Scanner scanner = new Scanner(reader);
//		
		while(scanner.hasNext()) {
			System.out.println("Inside hasnext function.");
			responseContent.append(scanner.nextLine());
			System.out.println("End of loop");
		}
//		
//		System.out.println("Response conteent is: " + responseContent);
		scanner.close();
		
		// convert string data to json
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
		System.out.println(jsonObject);
		
		// date time
		long dateTimeStamp = jsonObject.getAsJsonArray("list").get(0).getAsJsonObject().get("dt").getAsLong() * 1000;
		String date = new Date(dateTimeStamp).toString();
		
		// temperature
		double temperatureKelvin = jsonObject.getAsJsonArray("list").get(0).getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsDouble();
		int temperatureCelcius = (int) (temperatureKelvin - 273.15);
//		
//		// humidity
		int humidity = jsonObject.getAsJsonArray("list").get(0).getAsJsonObject().get("main").getAsJsonObject().get("humidity").getAsInt();
		
//		// wind speed
		double windSpeed = jsonObject.getAsJsonArray("list").get(0).getAsJsonObject().get("wind").getAsJsonObject().get("speed").getAsDouble();
		
		// weather condition 
		String weatherCondition = jsonObject.getAsJsonArray("list").get(0).getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperatureCelcius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}

}
