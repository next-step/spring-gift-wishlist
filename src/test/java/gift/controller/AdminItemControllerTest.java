package gift.controller;

import gift.dto.ItemCreateDto;
import gift.repository.ItemRepository;
import gift.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

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
        ItemCreateDto dto = new ItemCreateDto("카카오", 1500, "juice.png", true);
        itemService.saveItem(dto);
        assertThat(itemService.getAllItems())
                .anyMatch(item -> item.name().equals("카카오"));
    }

    @Test
    void 카카오상품_저장하기예외처리(){ // 의도와 다르게 작동함 -> 오류수정 필요
        ItemCreateDto dto = new ItemCreateDto("카카오", 1500, "juice.png", false);
        itemService.saveItem(dto);
        assertThat(itemService.getAllItems())
                .anyMatch(item -> item.name().equals("카카오"));
    }

}




