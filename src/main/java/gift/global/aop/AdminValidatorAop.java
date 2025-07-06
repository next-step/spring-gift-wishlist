package gift.global.aop;

import gift.domain.Role;
import gift.global.MySecurityContextHolder;
import gift.member.dto.AuthMember;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Aspect
@Component
public class AdminValidatorAop {

    @Around("@annotation(gift.global.annotation.Admin)")
    public Object validateAdmin(ProceedingJoinPoint joinPoint) throws Throwable {

        AuthMember authMember = MySecurityContextHolder.get();
        if (authMember.getRole() != Role.ADMIN)
            throw new AccessDeniedException("권한 부족");

        return joinPoint.proceed();
    }
}
