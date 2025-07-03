package gift.exception;

import org.springframework.http.HttpStatusCode;

public class CustomErrorResponse {

  private HttpStatusCode statusCode;
  private String message;

  public CustomErrorResponse(HttpStatusCode status, String message) {
    this.statusCode = status;
    this.message = message;
  }
  
}
