package hgs.app.prototype.dummy.media;

import java.util.ArrayList;

public class VoteMedia implements Media {
    public static final int SINGLE_SELECTION = 0x0;
    public static final int MUTLIPLE_SELECTION = 0x1;
    public static final int REAL_NAME = 0x10;
    public static final int ANONYMOUS = 0x11;

    // 식별 번호
    private long identifier;
    // 투표 제목
    private String title;
    // 투표 설명
    private String description;
    // 투표 방식
    private boolean voteType;
    // 선택지
    private ArrayList<String> options;

    @Override
    public MediaType getMediaType() {
        return MediaType.VOTE;
    }

    @Override
    public long getIdentifier() {
        return identifier;
    }
}
