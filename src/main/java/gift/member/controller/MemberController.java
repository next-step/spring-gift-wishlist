package gift.member.controller;

import gift.member.dto.MemberCreateRequest;
import gift.member.dto.MemberDeleteRequest;
import gift.member.dto.MemberResponse;
import gift.member.dto.MemberUpdateRequest;
import gift.member.service.MemberService;
import gift.util.LocationGenerator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {

        List<MemberResponse> response = memberService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable UUID id) {

        MemberResponse response = memberService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable UUID id, @Valid @RequestBody MemberDeleteRequest memberDeleteRequest) {

        memberService.deleteMember(id,memberDeleteRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void>  updateMember(@PathVariable UUID id, @Valid @RequestBody MemberUpdateRequest memberUpdateRequest) {

        memberService.changePassword(id,memberUpdateRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
