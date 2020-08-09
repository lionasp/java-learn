package engine.service;

import engine.models.Quiz;
import engine.repo.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    @Autowired
    QuizRepository quizRepository;

    public Page<Quiz> getQuizzes(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return quizRepository.findAll(paging);

    }
}
