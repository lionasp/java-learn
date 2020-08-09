package engine.repo;

import engine.models.Completion;
import engine.models.Quiz;
import engine.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompletionRepository extends CrudRepository<Completion, Integer> {
    @Query("SELECT co FROM Completion co WHERE co.user = ?1")
    Page<Completion> findAllBySolvedBy(User user, Pageable pageable);

    List<Completion> findAllByQuiz(Quiz quiz);
}
