package roomescape.domain;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findByEmail(String email);

    void deleteById(Long id);
}
