package Testcases;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {
	
	
	SoftAssert softAssert;
	
	
	public ReadOneProduct() {
		
		
		softAssert = new SoftAssert();
	}
	
	@Test
	public void readOneProduct() {
//	    

		Response response = given()
				.log().all()
				.baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json;")
				.auth().preemptive().basic("demo@techfios.com","abc123")
				.queryParam("id","4078")
				.when()
				.log().all()
				.get("/read_one.php")
				.then()
				.log().all()
				.extract().response();
		int actualResponseStatus = response.getStatusCode();
		System.out.println("actaul response Status: " + actualResponseStatus);
		softAssert.assertEquals(actualResponseStatus, 200,"Status Codes Are Not Matching");
		
		
		String actualResponseContentType = response.getContentType();
		System.out.println("actual Response ContentType : " + actualResponseContentType);
		softAssert.assertEquals(actualResponseContentType,"application/json");
		
		String actualResponseBody = response.getBody().asString();
		System.out.println("actual Response Body :" + actualResponseBody);
		
	 JsonPath jp = new JsonPath(actualResponseBody);
		 String productId = jp.get("id");
		
		 softAssert.assertEquals(productId,"4078","Product ID is not matching!");
		 
		 String productName = jp.getString("name");
		 
		 softAssert.assertEquals(productName, "MD's Amazing Pillow 3.0","Product Names Are not matching!");
		 
		 String productPrice = jp.getString("price");
		
		 softAssert.assertEquals(productPrice,"299","Product Priceis not matching!");
		 System.out.println("productPrice :" + productPrice);
		 softAssert.assertAll();

		

	}

}
