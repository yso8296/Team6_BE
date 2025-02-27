package supernova.whokie.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supernova.whokie.question.Question;
import supernova.whokie.question.infrastructure.repository.QuestionRepository;

@Service
@RequiredArgsConstructor
public class QuestionWriterService {

    private final QuestionRepository questionRepository;

    @Transactional
    public void save(Question question) {
        questionRepository.save(question);
    }

    @Transactional
    public void delete(Long questionId) { questionRepository.deleteById(questionId); }
}
