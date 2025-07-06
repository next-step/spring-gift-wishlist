package gift.member.controller;

import gift.member.annotation.MyAuthenticalPrincipal;
import gift.member.dto.*;
import gift.member.service.MemberService;
import gift.util.LocationGenerator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping()
    public ResponseEntity<Void> joinMember(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {

        UUID id = memberService.save(memberCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).
                location(LocationGenerator.generate(id)).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@MyAuthenticalPrincipal AuthMember authMember) {

        memberService.deleteMember(authMember.getEmail());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateMember(@MyAuthenticalPrincipal AuthMember authMember, @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {

        memberService.changePassword(authMember.getEmail(),memberUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
