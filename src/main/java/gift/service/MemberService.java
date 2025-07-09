package gift.service;

import gift.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import gift.repository.MemberRepository;

import java.util.Base64;
import java.util.StringTokenizer;

@Service
public class MemberService {
    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public void register (Member member) {
        repository.save(member);
    }

    public boolean login(String email, String password) {
        return repository.findByEmail(email)
                .map(m -> m.getPassword().equals(password))
                .orElse(false);
    }

    public boolean tokenvalidation(String memberHeader) {

        String base64Credentials = memberHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        StringTokenizer tokenizer = new StringTokenizer(credentials, ":");

        String email = tokenizer.nextToken();
        String password = tokenizer.nextToken();
        boolean valid = login(email, password);
        return valid;
    }

}
