package gift;

import gift.product.dto.*;
import gift.product.commons.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ETest {

	@LocalServerPort
	private int port;

	private RestClient restClient;
	@Autowired
	private JwtUtil jwtUtil;


	@BeforeEach
	void setUp() {
		restClient = RestClient.builder()
			.baseUrl("http://localhost:" + port)
			.build();
	}

	@Test
	@DisplayName("인증인가 적용된 상품 e2e 테스트")
	void userAndItemE2ETest() {

		// 로그인
		LoginRequest loginRequest = new LoginRequest("s@s", "1");
		LoginResponse loginResponse = restClient.post()
			.uri("/users/login")
			.body(loginRequest)
			.retrieve()
			.body(LoginResponse.class);

		String accessToken = loginResponse.token();
		assertThat(accessToken).isNotNull();

		// 상품 생성
		ItemRequest createItemRequest = new ItemRequest("테스트콜라", 1500, "url");
		Long createdId = restClient.post()
			.uri("/products")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.body(createItemRequest)
			.retrieve()
			.body(Long.class);

		assertThat(createdId).isEqualTo(16L); // 초기화된 목업 데이터 15개

		// 단건 조회
		GetItemResponse getItemResponse = restClient.get()
			.uri("/products/" + createdId)
			.retrieve()
			.body(GetItemResponse.class);

		assertThat(getItemResponse.name()).isEqualTo("테스트콜라");
		assertThat(getItemResponse.authorId()).isEqualTo(1L);

		// 수정
		ItemRequest updateItemRequest = new ItemRequest("테스트사이다", 2000, "url2");
		GetItemResponse updatedItemResponse = restClient.put()
			.uri("/products/" + createdId)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.body(updateItemRequest)
			.retrieve()
			.body(GetItemResponse.class);

		assertThat(updatedItemResponse.name()).isEqualTo("테스트사이다");
		assertThat(updatedItemResponse.price()).isEqualTo(2000);

		// 삭제
		restClient.delete()
			.uri("/products/" + createdId)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.toBodilessEntity();

		// 삭제 후 전체 목록 조회
		List items = restClient.get()
			.uri("/products")
			.retrieve()
			.body(List.class);

		assertThat(items.size()).isEqualTo(15);

		// 위시리스트 추가
		CreateWishListRequest wishListRequest = new CreateWishListRequest(2L);
		Long createdWishListId = restClient.post()
			.uri("/wishlists")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.body(wishListRequest)
			.retrieve()
			.body(Long.class);

		assertThat(createdWishListId).isEqualTo(2L);

		// 위시리스트 수량 삭제 추가 테스트
		assertThatThrownBy(() ->
			restClient.patch()
				.uri(("/wishlists/" + createdWishListId) +"/decrease")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.retrieve()
				.toBodilessEntity()
		).isInstanceOf(HttpClientErrorException.BadRequest.class);

		restClient.patch()
				.uri(("/wishlists/" + createdWishListId) +"/increase")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.retrieve()
				.toBodilessEntity();
		List myWishList = restClient.get()
			.uri("/wishlists")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.body(List.class);
		Map<String, Object> insertedItem = (Map<String, Object>) myWishList.get(1);
		assertThat(insertedItem.get("amount")).isEqualTo(2);

		// 위시리스트 삭제 후 목록조회
		restClient.delete()
			.uri("/wishlists/" + createdWishListId)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.toBodilessEntity();

		List afterDeleteWishList = restClient.get()
			.uri("/wishlists")
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.body(List.class);
		assertThat(afterDeleteWishList.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("비인가 유저 에러테스트")
	void userBadRequestTest() {

		LoginRequest loginRequest = new LoginRequest("s@s", "1");
		LoginResponse loginResponse = restClient.post()
			.uri("/users/login")
			.body(loginRequest)
			.retrieve()
			.body(LoginResponse.class);

		String accessToken = loginResponse.token();
		assertThat(accessToken).isNotNull();

		// 상품 수정 실패
		ItemRequest updateRequest = new ItemRequest("사이다", 2000, "url");
		assertThatThrownBy(() ->
			restClient.put()
				.uri("/products/14")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.body(updateRequest)
				.retrieve()
				.toBodilessEntity()
		).isInstanceOf(HttpClientErrorException.BadRequest.class);

		// 상품 삭제 실패
		assertThatThrownBy(() ->
			restClient.delete()
				.uri("/products/14")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.retrieve()
				.toBodilessEntity()
		).isInstanceOf(HttpClientErrorException.BadRequest.class);
	}
}
