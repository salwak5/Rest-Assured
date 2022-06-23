package Testcases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DeleteProduct {
	SoftAssert softassert;
	Map<String, String> createPayloadMap;
	Map<String, String> deletePayloadMap;
	String ExpectedProductName;
	String ExpectedProductPrice;
	String ExpectedProductDescription;
	String firstProductId;

	public DeleteProduct() {
		softassert = new SoftAssert();
	}

	public Map<String, String> createPayloadMap() {

		createPayloadMap = new HashMap<String, String>();
		createPayloadMap.put("name", "Sk456s Amazing Pillow 2.0");
		createPayloadMap.put("price", "199");
		createPayloadMap.put("description", "The best pillow for amazing programmers.");
		createPayloadMap.put("category_id", "2");

		ExpectedProductName = createPayloadMap.get("name");
		System.out.println("Expected Product Name: " + ExpectedProductName);
		ExpectedProductPrice = createPayloadMap.get("price");
		System.out.println("ExpectedProductPrice: " + ExpectedProductPrice);
		ExpectedProductDescription = createPayloadMap.get("description");
		System.out.println("Expected Product Description: " + ExpectedProductDescription);

		return createPayloadMap;

	}
	public Map<String, String> deletePayloadMap() {


		deletePayloadMap = new HashMap<String, String>();

		deletePayloadMap.put("id", firstProductId);
		 
		return deletePayloadMap;
	}

	@Test(priority = 0)
	public void CreateNewProduct() {

		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json;charset=UTF-8").auth().preemptive()
				.basic("demo@techfios.com", "abc123").body(createPayloadMap()).when().post("/create.php").then()

				.extract().response();
		int actualResponseStatus = response.getStatusCode();
		System.out.println("actaul response Status: " + actualResponseStatus);
		softassert.assertEquals(actualResponseStatus, 201, "Status codes are not matching");

		String actualResponseContentType = response.getHeader("Content-Type");
		System.out.println("actual Response ContentType : " + actualResponseContentType);
		softassert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8",
				"ResponseContent-Types are not matching");

		String actualResponseBody = response.getBody().asString();
		System.out.println("actual Response Body :" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		String productMessage = jp.get("message");
		softassert.assertEquals(productMessage, "Product was created.", "Product Messages Are No Matching");

		softassert.assertAll();

	}

	@Test(priority = 1)
	public void readAllProducts() {
		   
		System.out.println("Expected Product Name Inside readAllProducts: " + ExpectedProductName);

		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json; charset=UTF-8").auth().basic("demo@techfios.com", "abc123")
				.log().all().when().log().all().get("/read.php").then().extract().response();
		int actualResponseStatus = response.getStatusCode();
		System.out.println("actaul response Status: " + actualResponseStatus);

		Assert.assertEquals(actualResponseStatus, 200);

		String actualResponseContentType = response.getContentType();
		System.out.println("actual Response ContentType : " + actualResponseContentType);
		Assert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8");

		String actualResponseBody = response.getBody().asString();

		// System.out.println("actual Response Body :" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		String firstProductID = jp.get("records[0].id");

		System.out.println("firstProductID: " + firstProductID);
		softassert.assertAll();

	}
	
	 @Test(priority=2)
		public void deleteProduct() {
//			 ExpectedProductName = PayloadMap.get("name");
//				System.out.println("Expected Product Name: " + ExpectedProductName);
//				ExpectedProductPrice = PayloadMap.get("price");
//				System.out.println("ExpectedProductPrice: " + ExpectedProductPrice);
//				ExpectedProductDescription = PayloadMap.get("description");
			// String ProductId = firstProductId;

			Response response = given().log().all().baseUri("https://techfios.com/api-prod/api/product")
					.header("Content-Type", "application/json; charset=UTF-8").auth().preemptive().basic("demo@techfios.com", "abc123")
					.body(deletePayloadMap).when().log().all().delete("/delete.php").then().log().all().extract()
					.response();
			int actualResponseStatus = response.getStatusCode();
			System.out.println("actaul response Status: " + actualResponseStatus);

			softassert.assertEquals(actualResponseStatus, 200);

			String actualResponseContentType = response.getContentType();
			System.out.println("actual Response ContentType : " + actualResponseContentType);

			softassert.assertEquals(actualResponseContentType, "application/json; charset=UTF-8");

			String actualResponseBody = response.getBody().asString();

			System.out.println("actual Response Body :" + actualResponseBody);

			JsonPath jp = new JsonPath(actualResponseBody);
			String ActualproductMessage = jp.get("message");

			softassert.assertEquals(ActualproductMessage, "Product Was deleted ", "Product Message not matching!");

			
			softassert.assertAll();

		}


	 @Test(priority=3)
	public void readDeletedProduct() {
		 


		// String ProductId = firstProductId;

		Response response = given().log().all().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json").auth().preemptive().basic("demo@techfios.com", "abc123")
				.queryParam("id", firstProductId).when().log().all().get("/read_one.php").then().log().all().extract()
				.response();
		int actualResponseStatus = response.getStatusCode();
		System.out.println("actaul response Status: " + actualResponseStatus);

		softassert.assertEquals(actualResponseStatus, 404,"Status codes are not matching");

		String actualResponseContentType = response.getContentType();
		System.out.println("actual Response ContentType : " + actualResponseContentType);

		softassert.assertEquals(actualResponseContentType, "application/json");

		String actualResponseBody = response.getBody().asString();

		//System.out.println("actual Response Body :" + actualResponseBody);

		JsonPath jp = new JsonPath(actualResponseBody);
		

		String ActaulProductMessage = jp.get("Message");

		softassert.assertEquals(ActaulProductMessage, "Product does not exist.", "Product Messages Are Not Matching!");
		
		// System.out.println("productPrice :" + ActaulproductPrice);
		softassert.assertAll();

	}

}
