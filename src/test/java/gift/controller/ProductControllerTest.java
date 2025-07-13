package gift.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gift.dto.CreateProductRequest;
import gift.dto.CreateProductResponse;
import gift.dto.ProductResponse;
import gift.dto.UpdateProductRequest;
import gift.dto.UpdateProductResponse;
import gift.interceptor.MemberAuthInterceptor;
import gift.service.ProductService;
import gift.util.TokenProvider;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private MemberAuthInterceptor memberAuthInterceptor;

    @BeforeEach
    void setUp() throws Exception {
        given(memberAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
    }

    @Test
    void getAllProductsTest() throws Exception {
        // given
        List<ProductResponse> products = List.of(
            new ProductResponse(1L, "상품1", 1000, "image1"),
            new ProductResponse(2L, "상품2", 2000, "image2")
        );
        given(productService.getAllProducts()).willReturn(products);

        // when
        MockHttpServletResponse actual = mockMvc.perform(get("/api/products"))
            .andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<ProductResponse> result = Arrays.asList(objectMapper.readValue(
            actual.getContentAsString(),
            ProductResponse[].class
        ));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).name()).isEqualTo("상품1");
        assertThat(result.get(0).price()).isEqualTo(1000);
        assertThat(result.get(0).imageUrl()).isEqualTo("image1");
        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).name()).isEqualTo("상품2");
        assertThat(result.get(1).price()).isEqualTo(2000);
        assertThat(result.get(1).imageUrl()).isEqualTo("image2");
    }

    @Test
    void getProductByIdTest() throws Exception {
        // given
        Long productId = 1L;
        ProductResponse product = new ProductResponse(productId, "상품1", 1000, "image1");
        given(productService.getProductById(productId)).willReturn(product);

        // when
        MockHttpServletResponse actual = mockMvc.perform(get("/api/products/{id}", productId))
            .andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.OK.value());

        ProductResponse result = objectMapper.readValue(
            actual.getContentAsString(),
            ProductResponse.class
        );
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("상품1");
        assertThat(result.price()).isEqualTo(1000);
        assertThat(result.imageUrl()).isEqualTo("image1");
    }

    @Test
    void createProductTest() throws Exception {
        // given
        CreateProductRequest request = new CreateProductRequest("상품1", 1000, "image1");
        CreateProductResponse response = new CreateProductResponse(1L, "상품1", 1000, "image1");

        given(productService.createProduct(any())).willReturn(response);
        String content = objectMapper.writeValueAsString(request);

        // when
        MockHttpServletResponse actual = mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
        ).andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        CreateProductResponse result = objectMapper.readValue(
            actual.getContentAsString(),
            CreateProductResponse.class
        );
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("상품1");
        assertThat(result.price()).isEqualTo(1000);
        assertThat(result.imageUrl()).isEqualTo("image1");
    }

    @Test
    void updateProductTest() throws Exception {
        // given
        Long productId = 1L;
        UpdateProductRequest request = new UpdateProductRequest("상품1", 1500, "newimage");
        UpdateProductResponse response = new UpdateProductResponse(
            productId, "상품1", 1500, "newimage"
        );

        given(productService.updateProduct(any(), any())).willReturn(response);

        String content = objectMapper.writeValueAsString(request);

        // when
        MockHttpServletResponse actual = mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.OK.value());

        UpdateProductResponse result = objectMapper.readValue(
            actual.getContentAsString(),
            UpdateProductResponse.class
        );
        assertThat(result.id()).isEqualTo(productId);
        assertThat(result.name()).isEqualTo("상품1");
        assertThat(result.price()).isEqualTo(1500);
        assertThat(result.imageUrl()).isEqualTo("newimage");
    }

    @Test
    void deleteProductTest() throws Exception {
        // given
        Long productId = 1L;
        willDoNothing().given(productService).deleteProduct(productId);

        // when
        MockHttpServletResponse actual = mockMvc.perform(delete("/api/products/{id}", productId))
            .andReturn().getResponse();

        // then
        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
