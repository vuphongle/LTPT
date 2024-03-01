package Entity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class Demo {
	public static void main(String[] args) {
		List<Restaurant> r_list = JsonHandler.DocFile("data/Restaurant.json");

		for (Restaurant r : r_list) {
			System.out.println(r);
		}

//		try (BufferedWriter ghi = new BufferedWriter(new FileWriter("data/levuphong.json"))) {
//			for (Restaurant r1 : r_list) {
//				ghi.write(r1.toString());
//				ghi.newLine();
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		JsonHandler.toFile(r_list, "data/levuphong.json");
		
	}
}
