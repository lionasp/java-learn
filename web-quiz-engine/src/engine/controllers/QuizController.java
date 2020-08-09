package engine.controllers;

import engine.dto.CompletionDto;
import engine.QuizAnswer;
import engine.models.Answer;
import engine.models.Quiz;
import engine.models.User;
import engine.Response;
import engine.Utils;
import engine.repo.CompletionRepository;
import engine.repo.QuizRepository;
import engine.repo.UserRepository;
import engine.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static engine.models.Completion.createCompletion;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompletionRepository completionRepository;


    @Autowired
    private QuizService quizService;

    public QuizController() {
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public class QuizNotFoundException extends RuntimeException {
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class IncorrectQuizData extends RuntimeException {

    }

    @GetMapping
    public Page<Quiz> getQuizzes(
            @RequestParam(defaultValue = "0") Integer page
    ) {
        return quizService.getQuizzes(page, 10, "id");
    }

    @GetMapping("/completed")
    public Page<CompletionDto> getCompletedQuizzes(
            @RequestParam(defaultValue = "0") Integer page
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.findByEmail(username).get();

        Pageable paging = PageRequest.of(page, 10, Sort.by("id").descending());
        return completionRepository.findAllBySolvedBy(user, paging).map(Utils::convertCompletionEntityToDto);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getQuiz(@PathVariable int id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isPresent()) {
            return quiz.get().serialize();

        }
        throw new QuizNotFoundException();
    }

    @PostMapping(consumes = "application/json")
    public Map<String, Object> add(@RequestBody Quiz quiz) {
        boolean wrongTitle = "".equals(quiz.getTitle()) || quiz.getTitle() == null;
        boolean wrongText = "".equals(quiz.getText()) || quiz.getText() == null;
        boolean wrongOptions = quiz.getOptions() == null || quiz.getOptions().size() < 2;
        if (wrongTitle || wrongText || wrongOptions) {
            throw new IncorrectQuizData();
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        quiz.setAuthor(username);

        quizRepository.save(quiz);
        return quiz.serialize();
    }

    public  boolean equalLists(List<Integer> one, List<Integer> two){
        if (one == null && two == null){
            return true;
        }

        if((one == null && two != null)
                || one != null && two == null
                || one.size() != two.size()){
            return false;
        }

        //to avoid messing the order of the lists we will use a copy
        //as noted in comments by A. R. S.
        one = new ArrayList<Integer>(one);
        two = new ArrayList<Integer>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }

    @PostMapping(value = "/{id}/solve", consumes = "application/json")
    public Response answer(@RequestBody QuizAnswer ans, @PathVariable int id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isPresent()) {
            Quiz realQuiz = quiz.get();
            ArrayList<Integer> rightAnswerIndexes = new ArrayList<>();
            for (Answer answer:
                    realQuiz.getAnswer()) {
                rightAnswerIndexes.add(answer.getIndex());
            }
            boolean rightAnswer = equalLists(ans.getAnswer(), rightAnswerIndexes);

            if (rightAnswer) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username;
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }
                User user = userRepository.findByEmail(username).get();
                completionRepository.save(createCompletion(user, realQuiz));
            }

            return new Response(
                    rightAnswer,
                    rightAnswer ? "Congratulations, you're right!" : "Wrong answer! Please, try again."
            );

        }
        throw new QuizNotFoundException();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Optional<Quiz> quiz = quizRepository.findById(id);

        if (quiz.isEmpty()) {
            throw new QuizNotFoundException();
        }

        if (quiz.get().getAuthor().equals(username)) {
            completionRepository.deleteAll(completionRepository.findAllByQuiz(quiz.get()));
            quizRepository.deleteById(id);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
    }
}
