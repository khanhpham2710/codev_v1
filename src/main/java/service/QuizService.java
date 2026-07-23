package service;

import dto.response.QuizzesOverall;
import dto.response.QuizzesResponse;

import java.util.List;
import java.util.Map;

public class QuizService extends BaseService{
    private static final String BASE_URL = "https://quizapi.io/api/v1/quizzes";

    public QuizzesResponse getQuizzes(Map<String, String> params){
        return this.getWithToken(BASE_URL, QuizzesResponse.class, params).data();
    }
}
