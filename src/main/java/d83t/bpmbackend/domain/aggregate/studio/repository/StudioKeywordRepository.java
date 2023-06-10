package d83t.bpmbackend.domain.aggregate.studio.repository;

import d83t.bpmbackend.domain.aggregate.studio.entity.StudioKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudioKeywordRepository extends JpaRepository<StudioKeyword, Long> {

    @Query("SELECT s FROM StudioKeyword s WHERE s.keyword.id =:keywordId")
    Optional<StudioKeyword> findByKeywordId(Long keywordId);
}
