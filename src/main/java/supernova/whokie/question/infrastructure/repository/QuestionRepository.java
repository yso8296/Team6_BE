package supernova.whokie.question.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import supernova.whokie.question.Question;
import supernova.whokie.question.QuestionStatus;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q WHERE q.groupId = 1 ORDER BY function('RAND')")
    List<Question> findRandomQuestions(Pageable pageable);

    @Query("SELECT q FROM Question q WHERE q.questionStatus = 'APPROVED' AND q.groupId = :groupId ORDER BY function('RAND')")
    List<Question> findRandomGroupQuestions(@Param("groupId") Long groupId, Pageable pageable);

    Optional<Question> findByIdAndGroupId(Long questionId, Long groupId);

    @Query("SELECT q FROM Question q WHERE q.questionStatus = :status AND q.groupId = :groupId")
    Page<Question> findAllByStatus(@Param("groupId") Long groupId, @Param("status") QuestionStatus status, Pageable pageable);

}
