package gift.controller;

import gift.auth.LoginMember;
import gift.domain.Member;
import gift.dto.request.WishRequest;
import gift.dto.response.WishMsgResponse;
import gift.dto.response.WishResponse;
import gift.service.WishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService){
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<WishMsgResponse> add(@RequestBody WishRequest request,
                                               @LoginMember Member member){
        WishMsgResponse response = wishService.add(member,request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(request.productId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<WishResponse> getWishList(@LoginMember Member member) {
        return wishService.getWishList(member);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<WishMsgResponse> delete(@PathVariable Long productId,
                                                  @LoginMember Member member) {
        WishMsgResponse response = wishService.deleteByProductId(member, productId);
        return ResponseEntity.ok(response);
    }
}
