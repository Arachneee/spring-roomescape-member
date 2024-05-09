package roomescape.controller.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.dto.MemberResponse;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.service.AuthService;
import roomescape.service.MemberService;

@Controller
public class LoginController {

    public static final String TOKEN_NAME = "token";

    private final MemberService memberService;
    private final AuthService authService;

    public LoginController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping("/login")
    public String page() {
        return "login";
    }

    @PostMapping("/login")
    public void login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        MemberResponse memberResponse = memberService.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword());
        String accessToken = authService.createToken(memberResponse);

        Cookie cookie = new Cookie(TOKEN_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> findMyInfo(@Login LoginMember loginMember) {
        MemberResponse memberResponse = loginMember.toMemberResponse();

        return ResponseEntity.ok()
                .body(memberResponse);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_NAME, null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
