package hgs.app.prototype.dummy;

import java.util.ArrayList;
import java.util.Date;

import hgs.app.prototype.dummy.media.Media;

public class Schedule {
    // 일정 식별 번호
    private long identifier;
    // 일정 제목
    private String title;
    // 날짜 / 시간
    private Date date;
    // 장소
    private double latitude; // 위도
    private double longitude; // 경도
    private String location; // 상세 위치
    // 참여자 식별자 목록
    private ArrayList<Long> participants;
    // 미디어 목록
    private ArrayList<Media> mediaList;
}
