package hello.core.member;

public interface MemberReository {

    void save (Member member);

    Member findById(Long memberId);
}
