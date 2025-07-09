package gift.controller;

import gift.dto.member.MemberRequestDto;
import gift.entity.Token;
import gift.service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService service;

  public MemberController(MemberService service) {
    this.service = service;
  }

  @PostMapping("/register")
  public ResponseEntity<Token> register(@Valid @RequestBody MemberRequestDto requestDto) {
    Token token = service.register(requestDto);
    return new ResponseEntity<>(token, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<Token> login(@Valid @RequestBody MemberRequestDto requestDto) {
    Token token = service.login(requestDto);
    return new ResponseEntity<>(token, HttpStatus.OK);
  }
}
