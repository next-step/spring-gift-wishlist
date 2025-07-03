package gift;

import gift.dto.ErrorMessageResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public abstract class AbstractControllerTest {

    @LocalServerPort
    protected int port;
    protected RequestSpecification spec;

     public static  final FieldDescriptor[] ERROR_MESSAGE_FIELDS = {
            fieldWithPath("message").description("에러 메시지").type(JsonFieldType.STRING),
            fieldWithPath("status").description("HTTP 상태 코드").type("Integer"),
            fieldWithPath("error").description("에러 이름").type(JsonFieldType.STRING),
            fieldWithPath("path").description("요청 경로").type(JsonFieldType.STRING),
            fieldWithPath("timestamp").description("에러 발생 시간").type(JsonFieldType.STRING),
            fieldWithPath("stackTrace").description("스택 트레이스 (개발 환경에서만 사용)").type(JsonFieldType.STRING).optional(),
            fieldWithPath("validationErrors").description("유효성 검사 오류 목록").type(JsonFieldType.ARRAY).optional(),
            fieldWithPath("validationErrors[].field").description("유효성 검사 오류 필드").type(JsonFieldType.STRING).optional(),
            fieldWithPath("validationErrors[].message").description("유효성 검사 오류 메시지").type(JsonFieldType.STRING).optional()
    };

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(provider))
                .build();
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
