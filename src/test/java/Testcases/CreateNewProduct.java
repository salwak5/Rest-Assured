package Testcases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateNewProduct {
	SoftAssert softassert;
	Map<String, String> PayloadMap;
	String ExpectedProductName;
	String ExpectedProductPrice;
	String ExpectedProductDescription;
	String firstProductId;

	public CreateNewProduct() {
		softassert = new SoftAssert();
	}

	public Map<String, String> createPayloadMap() {

		PayloadMap = new HashMap<String, String>();
		PayloadMap.put("name", "Sk456s Amazing Pillow 2.0");
		PayloadMap.put("price", "199");
		PayloadMap.put("description", "The best pillow for amazing programmers.");
		PayloadMap.put("category_id", "2");

		ExpectedProductName = PayloadMap.get("name");
		System.out.println("Expected Product Name: " + ExpectedProductName);
		ExpectedProductPrice = PayloadMap.get("price");
		System.out.println("ExpectedProductPrice: " + ExpectedProductPrice);
		ExpectedProductDescription = PayloadMap.get("description");
		System.out.println("Expected Product Description: " + ExpectedProductDescription);

		return PayloadMap;

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
//		    ExpectedProductName = PayloadMap.get("name");
//			System.out.println("Expected Product Name: " + ExpectedProductName);
//			ExpectedProductPrice = PayloadMap.get("price");
//			System.out.println("ExpectedProductPrice: " + ExpectedProductPrice);
//			ExpectedProductDescription = PayloadMap.get("description");
//			System.out.println("Expected Product Description: " + ExpectedProductDescription);
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
		 firstProductId = jp.get("records[0].id");

		System.out.println("firstProductID: " + firstProductId);
		softassert.assertAll();

	}

	@Test(priority=2)
	public void readOneProduct() {
	    //productID= firstProductId;

		Response response = given()
				.log().all()
				.baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json;")
				.auth().preemptive().basic("demo@techfios.com","abc123")
				.queryParam("id",firstProductId)
				.when()
				.log().all()
				.get("/read_one.php")
				.then()
				.log().all()
				.extract().response();
		int actualResponseStatus = response.getStatusCode();
		System.out.println("actaul response Status: " + actualResponseStatus);
		softassert.assertEquals(actualResponseStatus, 200,"Status Codes Are Not Matching");
		
		
		String actualResponseContentType = response.getContentType();
		System.out.println("actual Response ContentType : " + actualResponseContentType);
		softassert.assertEquals(actualResponseContentType,"application/json");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("actual Response Body :" + actualResponseBody);
		
	 JsonPath jp = new JsonPath(actualResponseBody);
		 String productId = jp.get("id");
		
		 softassert.assertEquals(productId,firstProductId,"Product ID is not matching!");
		 
		 String productName = jp.getString("name");
		 
		 softassert.assertEquals(productName,ExpectedProductName ,"Product Names Are not matching!");
		 
		 String productPrice = jp.getString("price");
		
		 softassert.assertEquals(productPrice,ExpectedProductPrice,"Product Priceis not matching!");
		 System.out.println("productPrice :" + productPrice);
		 softassert.assertAll();

		

	}

}
