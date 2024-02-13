package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor //final 붙은 생성자를 만들어주는 어노테이션
public class OrderServiceImpl implements OrderService {

    //필드 주입 -> 사용하지말자! 테스트는o
     private final MemberRepository memberRepository;
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy(); -> 구현부를 의존하고있어 ocp, dip위반
     private final DiscountPolicy discountPolicy; //DIP 잘 지켜지고 있는 케이스 -> 구현체는 전혀 모르고 인터페이스(추상화)에만 의존하고있음

    // 수정자 주입(setter 주입)-> final없이 사용해야함
//    @Autowired(required = false) //@Autowired는 주입할 대상이 없으면 오류 발생 -> (required = false) 넣으면 주입할 대상이 없어도 동작o
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }


    //생성자 주입 -> new OrderServiceImpl(memberRepository,discountPolicy);
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository,DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    //일반 메서드 주입 -> final없애고 사용해야함
//    @Autowired
//    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
