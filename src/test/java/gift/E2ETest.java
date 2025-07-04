package gift;


import gift.product.dto.GetItemResponse;
import gift.product.dto.ItemRequest;
import gift.product.entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ETest {

	@LocalServerPort
	private int port;

	private RestClient restClient;

	@BeforeEach
	void setUp() {
		restClient = RestClient.builder()
			.baseUrl("http://localhost:" + port)
			.build();
	}

	@Test
	void 상품CRUD전체플로우() {
		ItemRequest request = new ItemRequest("콜라", 1500, "url");

		// 1. 상품생성
		Long createdId = restClient.post()
			.uri("/products")
			.body(request)
			.retrieve()
			.body(Long.class);

		assertThat(createdId).isNotNull();
		assertThat(createdId).isEqualTo(16L); // 초기화된 목업 데이터 15개

		// 2. 단건 조회
		GetItemResponse getItemResponse = restClient.get()
			.uri("/products/" + createdId)
			.retrieve()
			.body(GetItemResponse.class);

		assertThat(getItemResponse).isNotNull();

		assertThat(getItemResponse.name()).isEqualTo("콜라");
		assertThat(getItemResponse.price()).isEqualTo(1500);
		assertThat(getItemResponse.imageUrl()).isEqualTo("url");

		// 3. 수정
		Item updateItem = new Item( 16L,"사이다", 1500, "url");

		restClient.put()
			.uri("/products/" + createdId)
			.body(updateItem)
			.retrieve()
			.toBodilessEntity();

		GetItemResponse getUpdatedItem = restClient.get()
			.uri("/products/" + createdId)
			.retrieve()
			.body(GetItemResponse.class);

		assertThat(getUpdatedItem).isNotNull();
		assertThat(getUpdatedItem.name()).isEqualTo("사이다");


		// 4. 삭제 후 전체목록 조회해 개수체크
		restClient.delete()
			.uri("/products/" + createdId)
			.retrieve()
			.toBodilessEntity();

		List items = restClient.get()
			.uri("/products")
			.retrieve()
			.body(List.class);

		assertThat(items).isNotNull();
		assertThat(items.size()).isEqualTo(15);

	}


	@Test
	void 상품생성_검증() {

		ItemRequest kakaoNameItem = new ItemRequest("카카오테크캠퍼스", 1500, "url");

		assertThatThrownBy(() ->
			restClient.post()
			.uri("/products")
			.body(kakaoNameItem)
			.retrieve()
			.body(Long.class)
		).isInstanceOf(HttpClientErrorException.BadRequest.class);

	}

}
