package com.example.demo.dto;

import java.util.Map;

public class ErrorResponseDto {
  private String type;                  // 오류 타입 식별용 URI
  private String title;                 // 한 줄 오류 제목
  private int status;                   // HTTP 상태 코드
  private String detail;                // 추가적인 설명
  private String instance;              // 요청 경로
  private Map<String, String> errors;   // 필드별 상세 오류

  public ErrorResponseDto(String type, String title, int status, String detail, String instance, Map<String, String> errors) {
    this.type = type;
    this.title = title;
    this.status = status;
    this.detail = detail;
    this.instance = instance;
    this.errors = errors;
  }

  public String getType() {
    return type;
  }

  public String getTitle() {
    return title;
  }

  public int getStatus() {
    return status;
  }

  public String getDetail() {
    return detail;
  }

  public String getInstance() {
    return instance;
  }

  public Map<String, String> getErrors() {
    return errors;
  }
}
