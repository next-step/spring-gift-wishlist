package gift.item.service.unit;

import gift.global.exception.CustomException;
import gift.item.dto.CreateItemDto;
import gift.item.dto.ItemDto;
import gift.item.entity.Item;
import gift.item.repository.ItemRepository;
import gift.item.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class) // 자동으로 Mock객체들 초기화해줌
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void 아이템저장성공() {
        CreateItemDto dto = new CreateItemDto("상품A", 1000, "img.jpg");// 입력데이터
        Item savedItem = new Item(1L, "상품A", 1000, "img.jpg");// 결과데이터

        given(itemRepository.saveItem(any())).willReturn(savedItem);//

        ItemDto result = itemService.saveItem(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("상품A");
    }

    @Test
    void 아이템조회성공() {
        Item item = new Item(1L, "상품B", 2000, "img2.jpg");
        given(itemRepository.findItem(1L)).willReturn(item);

        ItemDto result = itemService.findItem(1L);

        assertThat(result.getName()).isEqualTo("상품B");
    }

    @Test
    void 아이템조회_실패_예외발생() {
        given(itemRepository.findItem(1L)).willReturn(null);

        assertThrows(CustomException.class, () -> itemService.findItem(1L));
    }

}
