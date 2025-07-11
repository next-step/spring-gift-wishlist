package gift.controller;

import gift.auth.LoginMember;
import gift.domain.Member;
import gift.dto.request.WishRequest;
import gift.dto.response.WishResponse;
import gift.service.WishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService){
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<WishResponse> add(@RequestBody WishRequest request,
                                           @LoginMember Member member){
        WishResponse response = wishService.add(member,request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(request.productId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
