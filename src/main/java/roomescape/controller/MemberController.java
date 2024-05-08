package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.MemberResponse;
import roomescape.service.MemberService;
import roomescape.service.dto.MemberSaveRequest;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> save(@RequestBody @Valid final MemberSaveRequest memberRequest) {
        MemberResponse memberResponse = memberService.save(memberRequest);

        return ResponseEntity.created(URI.create("/members/" + memberResponse.id()))
                .body(memberResponse);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAll() {
        List<MemberResponse> memberResponses = memberService.findAll();

        return ResponseEntity.ok()
                .body(memberResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") final Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
