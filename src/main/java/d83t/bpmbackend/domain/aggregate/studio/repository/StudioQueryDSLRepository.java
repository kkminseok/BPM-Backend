package d83t.bpmbackend.domain.aggregate.studio.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import d83t.bpmbackend.domain.aggregate.studio.entity.QStudio;
import d83t.bpmbackend.domain.aggregate.studio.entity.QStudioKeyword;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudioQueryDSLRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // 필터된 값 가져오기
    public List<Studio> findAllByFilterStudio(List<Long> keyword){
        QStudioKeyword studioKeyword = QStudioKeyword.studioKeyword;
        QStudio studio = QStudio.studio;

        return jpaQueryFactory
                .select(studioKeyword.studio)
                .from(studioKeyword)
                .join(studioKeyword.studio, studio)
                .on(studioKeyword.studio.id.eq(studio.id))
                .where(studioKeyword.keyword.id.in(keyword))
                .groupBy(studioKeyword.studio)
                .having(studioKeyword.studio.count().eq((long) keyword.size()))
                .fetch();
    }

    //카운트가 제일 높은 3개의 keyword id값 가져오기
    public List<Tuple> getTopThreeKeyword(Long studioId){
        QStudioKeyword studioKeyword = QStudioKeyword.studioKeyword;
        QStudio studio = QStudio.studio;

        return jpaQueryFactory
                .select(studioKeyword.keyword.id, studioKeyword.counting)
                .from(studioKeyword)
                .join(studioKeyword.studio, studio)
                .on(studioKeyword.studio.id.eq(studioId))
                .groupBy(studioKeyword.studio.id, studioKeyword.keyword.id, studioKeyword.counting)
                .orderBy(studioKeyword.counting.desc())
                .limit(10)
                .fetch();


    }

}
