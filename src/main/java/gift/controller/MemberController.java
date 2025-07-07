package gift.controller;

import gift.dto.*;
import gift.exception.InvalidMemberException;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> addMember(
            @Valid @RequestBody MemberAddRequestDto requestDto, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new InvalidMemberException(bindingResult.getFieldError().getDefaultMessage());
        }
        memberService.addMember(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> findMemberById(
            @PathVariable Long id
    ) {
        MemberResponseDto responseDto = memberService.findMemberById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMemberById(
            @PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequestDto requestDto, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new InvalidMemberException(bindingResult.getFieldError().getDefaultMessage());
        }
        memberService.updateMemberById(id, requestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberById(
            @PathVariable Long id
    ) {
        memberService.deleteMemberById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
