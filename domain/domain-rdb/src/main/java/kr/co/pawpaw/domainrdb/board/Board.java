package kr.co.pawpaw.domainrdb.board;

import kr.co.pawpaw.domainrdb.board.dto.BoardDto;
import kr.co.pawpaw.domainrdb.boardImg.entity.BoardImg;
import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Where(clause = "is_Removed = false")
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    private boolean isRemoved = false;

    @Column(name = "liked_count")
    private int likedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private Set<BoardImg> imgSet = new HashSet<>();

    @Builder
    public Board(String title, String content, User user, String writer) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.writer = writer;
    }

    public static Board createBoard(BoardDto.BoardRegisterDto registerDto, User user){
        Board board = Board.builder()
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .writer(user.getNickname())
                .user(user)
                .build();
        return board;
    }

    // 나중에 비관적 락, 낙관적 락, 스프링 스케줄러, 배치 등을 적용해서 수정
    public void plusLikedCount(){
        this.likedCount = likedCount +1;
    }
    public void minusLikedCount(){
        this.likedCount = likedCount - 1;
    }


    public void updateTitleAndContent(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void remove() {
        this.isRemoved = true;
    }


    public void addImage(String uuid, String fileName){
        BoardImg boardImg = BoardImg.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imgSet.size())
                .build();
        imgSet.add(boardImg);
    }

    public void clearImages(){
        this.imgSet.clear();
    }

    protected Board() {
    }
}
