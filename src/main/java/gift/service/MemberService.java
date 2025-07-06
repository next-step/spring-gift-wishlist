package gift.service;

import gift.config.NotMatchPasswordException;
import gift.domain.Member;
import gift.domain.Product;
import gift.dto.CreateMemberRequest;
import gift.dto.CreateMemberResponse;
import gift.dto.LoginMemberRequest;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public MemberService(MemberRepository repository, ProductRepository productRepository) {
        this.memberRepository = repository;
        this.productRepository = productRepository;
    }

    public CreateMemberResponse register(CreateMemberRequest request) {
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateKeyException("이미 존재하는 email입니다.");
        }
        Member member = memberRepository.save(request);
        return new CreateMemberResponse(member.getEmail());
    }

    public void login(LoginMemberRequest request) {
        Optional<Member> findMember = memberRepository.findByEmail(request.email());
        if (findMember.isEmpty()) {
            throw new NoSuchElementException("Email이 존재하지 않습니다.");
        }
        Member member = findMember.get();
        if (!member.getPassword().equals(request.password())) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    public List<Product> productList() {
        return productRepository.findAll();
    }
}
