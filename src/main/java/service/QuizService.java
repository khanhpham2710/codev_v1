package service;

import dto.response.QuizDataResponse;
import dto.response.QuizzesResponse;

import java.util.*;

public class QuizService extends BaseService{
    private static final String BASE_URL = "https://quizapi.io/api/v1/quizzes";

    public QuizzesResponse getQuizzes(Map<String, String> params){
        return this.getWithToken(BASE_URL, QuizzesResponse.class, params).data();
    }

    public QuizDataResponse.QuizData getQuizById(String id) {
        return this.getWithToken(BASE_URL + "/" + id, QuizDataResponse.class).data().data();
    }
}
