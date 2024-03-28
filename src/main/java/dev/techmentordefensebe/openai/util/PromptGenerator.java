package dev.techmentordefensebe.openai.util;

public class PromptGenerator {
    public static final String HI = "안녕하세요. ";
    public static final String INTRODUCE = "멘토입니다.";
    public static final String WHAT_HELP = "무엇을 도와드릴까요?";

    public static String getMentorSystemPrompt(final String mentorTopic,
                                               final String mentorTone,
                                               final boolean isDefenseMode) {
        if (isDefenseMode) {
            return "당신은 " + mentorTopic + "전문가입니다." + mentorTone + "하게 답변해주세요.";
        }
        return "당신은 " + mentorTopic + " 기술 면접관입니다. 그리고 현재 면접자를 인터뷰하는 상황입니다."
                + mentorTone + "하게 질문해주세요." +
                "면접상황과 맞지 않는 말은 하지 말아주세요." +
                "질문은 꼭 1개만 해주세요." +
                "상대방의 답변을 받으면 꼭 다음 질문을 해서 인터뷰를 이어나가주세요." +
                "상대방의 답변의 정확성이 부정확하다면 꼭 추가적인 질문을 해주세요.";
    }
}
