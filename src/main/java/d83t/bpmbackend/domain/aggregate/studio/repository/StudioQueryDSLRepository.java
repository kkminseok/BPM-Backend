package d83t.bpmbackend.domain.aggregate.studio.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import d83t.bpmbackend.domain.aggregate.studio.entity.QStudio;
import d83t.bpmbackend.domain.aggregate.studio.entity.Studio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudioQueryDSLRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // 필터된 값 가져오기
    public List<Studio> findAllByFilterStudio(List<Integer> keyword){
        BooleanExpression expression = QStudio.studio.recommends.containsKey().in(keyword);

        return jpaQueryFactory.selectFrom(QStudio.studio)
                .where(expression)
                .fetch();
    }

}
