package gift.controller;

import gift.dto.ItemCreateDTO;
import gift.dto.ItemResponseDTO;
import gift.entity.Item;
import gift.repository.ItemRepository;
import gift.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AdminItemControllerTest {

    @Autowired
    private ItemRepository itemRepository;

    @LocalServerPort
    int port;

    @Autowired
    private ItemService itemService;

    private RestClient client = RestClient.builder().build();

    @Test
    void 카카오상품저장하기() {
        ItemCreateDTO dto = new ItemCreateDTO("카카오", 1500, "juice.png", true);
        itemService.saveItem(dto);
        assertThat(itemService.getAllItems())
                .anyMatch(item -> item.name().equals("카카오"));
    }

    @Test
    void 카카오상품_저장하기예외처리(){ // 의도와 다르게 작동함 -> 오류수정 필요
        ItemCreateDTO dto = new ItemCreateDTO("카카오", 1500, "juice.png", false);
        itemService.saveItem(dto);
        assertThat(itemService.getAllItems())
                .anyMatch(item -> item.name().equals("카카오"));
    }

}




