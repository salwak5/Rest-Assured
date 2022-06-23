package Testcases;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.IAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadAllproducts {
	
	
	@Test
	public void readAllProducts() {
		


		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json; charset=UTF-8")
				.auth().basic("demo@techfios.com", "abc123").log().all()
				.when().log().all()
				.get("/read.php")
				.then()
				.extract().response();
		int actualResponseStatus = response.getStatusCode();
		System.out.println("actaul response Status: " + actualResponseStatus);
		
		Assert.assertEquals(actualResponseStatus, 200);
		
		
		String actualResponseContentType = response.getContentType();
		System.out.println("actual Response ContentType : " + actualResponseContentType);
		Assert.assertEquals(actualResponseContentType,"application/json; charset=UTF-8");
		
		String actualResponseBody = response.getBody().asString();
		
		System.out.println("actual Response Body :" + actualResponseBody);
		
		 JsonPath jp = new JsonPath(actualResponseBody);
		String firstProductID = jp.get("records[0].id");
		
		if(firstProductID !=null) {
			System.out.println("product exists.");
		} else 
		{ 
			System.out.println("product does not exists!");
		}
		

	}

}
