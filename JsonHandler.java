package Entity;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.JobName;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;

public class JsonHandler {
	public static List<Restaurant> DocFile(String fileName) {
		List<Restaurant> restaurant_list = new ArrayList<Restaurant>();
		Restaurant restaurant = null;
		Grade grades = null;
		try (JsonReader reader = Json.createReader(new FileReader(fileName))) {
			JsonArray ja = reader.readArray();
			for (JsonValue jv : ja) {
				JsonObject jo = (JsonObject) jv;
				if (jo != null) {
					restaurant = new Restaurant();
					restaurant.setRestaurant_id(jo.getString("restaurant_id"));
					restaurant.setIs_closed(jo.getBoolean("is_closed"));
					restaurant.setName(jo.getString("name"));

					// Tạo địa chỉ
					JsonObject joAdd = jo.getJsonObject("address");
					Address address = new Address();
					address.setBuilding(joAdd.getString("building"));
					JsonArray coord_arr = joAdd.getJsonArray("coord");
					List<Double> coord_list = new ArrayList<>();
					for (int i = 0; i < coord_arr.size(); i++) {
						coord_list.add(coord_arr.getJsonNumber(i).doubleValue());
					}

					address.setCoord(coord_list);

					address.setStreet(joAdd.getString("street"));
					address.setZipcode(joAdd.getString("zipcode"));

					restaurant.setAddress(address);

					restaurant.setBorough(jo.getString("borough"));
					restaurant.setCuisine(jo.getString("cuisine"));

					JsonArray grade_arr = jo.getJsonArray("grades");
					List<Grade> grade_list = new ArrayList<>();
					for (JsonValue jv1 : grade_arr) {
						JsonObject joGra = (JsonObject) jv1;
						grades = new Grade();

						// thêm obj date
						JsonObject joDate = joGra.getJsonObject("date");
						Date_3 date = new Date_3();
						date.setYear(joDate.getInt("year"));
						date.setMonth(joDate.getInt("month"));
						date.setDay(joDate.getInt("day"));

						grades.setDate(date);
						grades.setGrade(joGra.getString("grade"));
						grades.setScore(joGra.getInt("score"));
						// thêm vào list
						grade_list.add(grades);
					}
					restaurant.setGrades(grade_list);
					restaurant_list.add(restaurant);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return restaurant_list;

	}

	public static void toFile(List<Restaurant> restaurants, String fileName) {
		try (JsonWriter writer = Json.createWriter(new FileWriter(fileName));) {
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (Restaurant restaurant : restaurants) {
				JsonObject addressObject = Json.createObjectBuilder()
						.add("building", restaurant.getAddress().getBuilding())
						.add("coord",
								Json.createArrayBuilder().add(restaurant.getAddress().getCoord().get(0))
										.add(restaurant.getAddress().getCoord().get(1)))
						.add("street", restaurant.getAddress().getStreet())
						.add("zipcode", restaurant.getAddress().getZipcode()).build();

				JsonArrayBuilder gradesArrayBuilder = Json.createArrayBuilder();
				for (Grade grade : restaurant.getGrades()) {
					JsonObject gradeObject = Json.createObjectBuilder()
							.add("date", Json.createObjectBuilder().add("year", grade.getDate().getYear())
									.add("month", grade.getDate().getMonth()).add("day", grade.getDate().getDay()))
							.add("grade", grade.getGrade()).add("score", grade.getScore()).build();
					gradesArrayBuilder.add(gradeObject);
				}

				JsonObject restaurantObject = Json.createObjectBuilder()
						.add("restaurant_id", restaurant.getRestaurant_id()).add("is_closed", restaurant.isIs_closed())
						.add("name", restaurant.getName()).add("address", addressObject)
						.add("borough", restaurant.getBorough()).add("cuisine", restaurant.getCuisine())
						.add("grades", gradesArrayBuilder).build();

				arrayBuilder.add(restaurantObject);
			}

			writer.writeArray(arrayBuilder.build());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
