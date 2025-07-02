package gift.exception;

import org.springframework.http.HttpStatusCode;

public class CustomErrorResponse {

  private HttpStatusCode statusCode;
  private String message;

  public CustomErrorResponse(HttpStatusCode status, String message) {
    this.statusCode = status;
    this.message = message;
  }

  public HttpStatusCode getStatus() {
    return statusCode;
  }

  public void setStatus(HttpStatusCode status) {
    this.statusCode = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
