package org.anonymous.bank.services.transaction;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.anonymous.bank.constants.BankName;
import org.anonymous.bank.controllers.TransactionSearch;
import org.anonymous.bank.entities.Bank;
import org.anonymous.bank.entities.QBank;
import org.anonymous.bank.entities.QTransaction;
import org.anonymous.bank.entities.Transaction;
import org.anonymous.bank.exceptions.TransactionNotFoundException;
import org.anonymous.bank.repositories.TransactionRepository;
import org.anonymous.global.exceptions.BadRequestException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.paging.ListData;
import org.anonymous.global.paging.Pagination;
import org.anonymous.member.MemberUtil;
import org.aspectj.weaver.MemberUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class TransactionInfoService {

    private final TransactionRepository transactionRepository;

    private final MemberUtil memberUtil;

    private final JPAQueryFactory queryFactory;

    private final HttpServletRequest request;

    private final Utils utils;

    /**
     * 거래 내역 단일 조회
     *
     * @param seq
     * @return
     */
    public Transaction get(Long seq) {

        Transaction item = transactionRepository.findBySeq(seq).orElseThrow(TransactionNotFoundException::new);

        if (item.getDeletedAt() != null && !memberUtil.isAdmin()) throw new BadRequestException();

        return item;
    }

    public List<Transaction> getByDate() {
        QTransaction transaction = QTransaction.transaction;
        QBank bank = QBank.bank;
        BooleanBuilder builder = new BooleanBuilder();
        LocalDate today = LocalDate.now();
        LocalDate minus3L = today.minusMonths(3L);
        DateTimePath<LocalDateTime> condition = transaction.createdAt;
        builder.and(condition.after(minus3L.atStartOfDay()));
        builder.and(condition.before(today.atTime(LocalTime.of(23,59,59))));


        JPAQuery<Transaction> query = queryFactory.selectFrom(transaction)
                .leftJoin(transaction.bank, bank)
                .fetchJoin()
                .where(builder);
        query.orderBy(transaction.createdAt.desc());

        return query.fetch();
    }

    /**
     * 거래 내역 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Transaction> getList(TransactionSearch search) {
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
        
        // 금액 처리

        Long transactionLimitMax = search.getPayAmountMax() > 0 ? search.getPayAmountMax() : 0;
        Long transactionLimitMin = search.getPayAmountMin() > 0 ? search.getPayAmountMax() : 0;

        transactionLimitMax = transactionLimitMax < 1 ? 100000000L : transactionLimitMax;
        transactionLimitMin = transactionLimitMin < 1 ? 0 : transactionLimitMin;

        // andBuilder.and(transaction.payAmount.between(transactionLimitMin, transactionLimitMax));
        andBuilder.and(transaction.payAmount.goe(transactionLimitMin));
        andBuilder.and(transaction.payAmount.loe(transactionLimitMax));

        JPAQuery<Transaction> query = queryFactory.selectFrom(transaction)
                .leftJoin(transaction.bank)
                .fetchJoin()
                .where(andBuilder)
                .offset(offset)
                .limit(limit);

        query.orderBy(transaction.createdAt.desc());

        // region 안할듯

        /* 정렬 조건 처리 S */
        // String sort = search.getSort();

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

        // endregion

        List<Transaction> items = query.fetch();

        long total = transactionRepository.count(andBuilder);

        int ranges = utils.isMobile() ? 5 : 10;

        Pagination pagination = new Pagination(page, (int)total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }
}
