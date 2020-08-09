package engine;

import engine.dto.CompletionDto;
import engine.models.Completion;

public final class Utils {

    private Utils() {
    }

    public static CompletionDto convertCompletionEntityToDto(Completion completion) {
        var completionDto = new CompletionDto();
        completionDto.setId(completion.getQuiz().getId());
        completionDto.setCompletedAt(completion.getCompletedAt());
        return completionDto;
    }
}
