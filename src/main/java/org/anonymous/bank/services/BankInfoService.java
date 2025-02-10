package org.anonymous.bank.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.anonymous.bank.constants.BankName;
import org.anonymous.bank.controllers.BankSearch;
import org.anonymous.bank.controllers.RequestBank;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.entities.QBank;
import org.anonymous.bank.entities.QTransaction;
import org.anonymous.bank.exceptions.BankNotFountException;
import org.anonymous.bank.repositories.BankRepository;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.paging.Pagination;
import org.anonymous.member.Member;
import org.anonymous.member.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BankInfoService {

    private final Utils utils;

    private final MemberUtil memberUtil;

    private final ModelMapper modelMapper;

    private final BankRepository repository;

    private final HttpServletRequest request;

    private final JPAQueryFactory queryFactory;

    /**
     * 계좌 단일 조회
     *
     * @param seq
     * @return
     */
    public Bank get(Long seq) {

        Bank item = repository.findById(seq).orElseThrow(BankNotFountException::new);

        if (item.getDeletedAt() != null && !memberUtil.isAdmin()) throw new BadRequestException();

        return item;
    }

    /**
     * 계좌 수정시 필요한 커맨드 객체 RequestBank 로 변환해 반환
     *
     * @param seq
     * @return
     */
    public RequestBank getForm(Long seq) {

        return getForm(get(seq));
    }

    /**
     * 계좌 수정시 필요한 커맨드 객체 RequestBank 로 변환해 반환
     *
     * Base method
     *
     * @param item
     * @return
     */
    public RequestBank getForm(Bank item) {

        RequestBank form = modelMapper.map(item, RequestBank.class);

        form.setMode("edit");

        return form;
    }

    /**
     * 계좌 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Bank> getList(BankSearch search) {

        int page = Math.max(search.getPage(), 1);

        int rowsPerPage = 0;

        int limit = search.getLimit() > 0 ? search.getLimit() : rowsPerPage;

        int offset = (page - 1) * limit;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();

        QBank bank = QBank.bank;

        QTransaction transaction = QTransaction.transaction;

        // 은행명별 검색
        List<BankName> bankNames = search.getBankName();

        if (bankNames != null && !bankNames.isEmpty()) {

            andBuilder.and(bank.bankName.in(bankNames));
        }

        // 관리자가 아닐 경우 유저 삭제된 계좌는 제외하고 조회
        if (!memberUtil.isAdmin())andBuilder.and(bank.deletedAt.isNull());

        /**
         * 키워드 검색
         *
         * - sopt
         * ALL : 은행명 + 계좌 번호 + 예금주(이름 + 이메일)
         * ACCOUNTNUMBER : 계좌 번호
         * DEPOSITOR : 예금주(이름 + 이메일)
         */
        String sopt = search.getSopt();
        String skey = search.getSkey();

        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";

        if (StringUtils.hasText(skey)) {

            skey = skey.trim();

            StringExpression accountNumber = bank.accountNumber;
            StringExpression depositor = bank.name.concat(bank.createdBy);

            StringExpression condition = null;

            if (sopt.equals("ACCOUNTNUMBER")) { // 계좌 번호 검색

                condition = accountNumber;

            } else if (sopt.equals("DEPOSITOR")) { // 예금주 검색

                condition = depositor;

            } else { // 통합 검색

                condition = accountNumber.concat(depositor);
            }

            andBuilder.and(condition.contains(skey));
        }

        // 회원 이메일로 검색
        // OneToMany 안쓰는 이유 : Page 때문.. 생각보다 OneToMany 는 자주 쓰이지 않음
        List<String> emails = search.getEmail();

        if (emails != null && !emails.isEmpty()) {

            andBuilder.and(bank.createdBy.in(emails));
        }
        /* 검색 처리 E */

        JPAQuery<Bank> query = queryFactory.selectFrom(bank)
                .leftJoin(bank.transactions)
                .fetchJoin()
                .where(andBuilder)
                .offset(offset)
                .limit(limit);

        /* 정렬 조건 처리 S */
        // String sort = search.getSort();

        query.orderBy(bank.createdAt.desc());

//        if (StringUtils.hasText(sort)) {
//
//            // 0번째 : 필드명, 1번째 : 정렬 방향
//            String[] _sort = sort.split("_");
//
//            String field = _sort[0];
//
//            String direction = _sort[1];
//
//            if (field.equals("balance")) { // 계좌 잔액순 정렬
//
//                query.orderBy(direction.equalsIgnoreCase("DESC")
//                ? bank.balance.desc() : bank.balance.asc());
//
//            } else { // 기본 정렬 조건 - 최신순
//
//                query.orderBy(bank.createdAt.desc());
//            }
//        } else { // 기본 정렬 조건 - 최신순
//
//            query.orderBy(bank.createdAt.desc());
//        }
        /* 정렬 조건 처리 E */

        List<Bank> items = query.fetch();

        long total = repository.count(andBuilder);

        int ranges = utils.isMobile() ? 5 : 10;

        Pagination pagination = new Pagination(page, (int)total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 현재 로그인한 회원이 등록한 계좌 목록 조회
     *
     * MyPage 에서 연동
     *
     * @param search
     * @return
     */
    public ListData<Bank> getMyList(BankSearch search) {

        if (!memberUtil.isLogin()) return new ListData<>(List.of(), null);

        Member member = memberUtil.getMember();

        String email = member.getEmail();

        search.setEmail(List.of(email));

        return getList(search);
    }

    /**
     * 공통 처리
     *
     * @param item
     */
//    public void addInfo(Bank item) {
//
//    }
}