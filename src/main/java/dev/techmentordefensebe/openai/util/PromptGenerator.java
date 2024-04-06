package dev.techmentordefensebe.openai.util;

import dev.techmentordefensebe.tech.domain.Tech;

public class PromptGenerator {
    private static final String HI = "안녕하세요. ";
    private static final String MENTOR = "멘토입니다.";
    private static final String PROFESSIONAL = "전문가입니다.";
    private static final String INTERVIEWER = "기술 면접관입니다.";
    private static final String WHAT_HELP = "무엇을 도와드릴까요?";
    private static final String START_INTERVIEW = "면접을 시작하겠습니다.";
    private static final String YOU = "당신은 ";
    private static final String INTERVIEWER_RULE_1 = "면접상황과 맞지 않는 말은 하지 말아주세요.";
    private static final String INTERVIEWER_RULE_2 = "질문은 꼭 1개만 해주세요.";
    private static final String INTERVIEWER_RULE_3 = "상대방의 답변을 받으면 꼭 다음 질문을 해서 인터뷰를 이어나가주세요.";
    private static final String INTERVIEWER_RULE_4 = "상대방의 답변의 정확성이 부정확하다면 꼭 추가적인 질문을 해주세요.";

    public static String welcomePrompt(Tech tech, boolean isDefenseMode) {
        if (isDefenseMode) {
            return HI + tech.getName() + INTERVIEWER + START_INTERVIEW;
        }
        return HI + tech.getName() + MENTOR + WHAT_HELP;
    }

    public static String getMentorSystemPrompt(final String mentorTopic,
                                               final String mentorTone,
                                               final boolean isDefenseMode) {
        if (isDefenseMode) {
            return YOU + mentorTopic + PROFESSIONAL + mentorTone + "하게 답변해주세요.";
        }
        return YOU + mentorTopic + INTERVIEWER + "현재 면접자를 기술 면접하는 상황입니다."
                + mentorTone + "하게 질문해주세요." +
                INTERVIEWER_RULE_1 +
                INTERVIEWER_RULE_2 +
                INTERVIEWER_RULE_3 +
                INTERVIEWER_RULE_4;
    }
}
