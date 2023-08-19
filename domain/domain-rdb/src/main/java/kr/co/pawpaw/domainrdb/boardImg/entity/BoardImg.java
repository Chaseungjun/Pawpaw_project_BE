package kr.co.pawpaw.domainrdb.boardImg.entity;

import kr.co.pawpaw.domainrdb.board.Board;
import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "board")
public class BoardImg extends BaseTimeEntity implements Comparable<BoardImg> {

    @Id
    private String uuid;

    private String fileName;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private int ord;
    @Builder
    public BoardImg(String uuid, String fileName, Board board, int ord) {
        this.uuid = uuid;
        this.fileName = fileName;
        this.board = board;
        this.ord = ord;
    }

    @Override
    public int compareTo(BoardImg o) {
        return this.ord - o.ord;
    }

}
