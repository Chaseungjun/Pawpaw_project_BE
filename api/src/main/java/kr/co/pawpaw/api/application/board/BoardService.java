package kr.co.pawpaw.api.application.board;

import kr.co.pawpaw.common.exception.common.board.BoardException;
import kr.co.pawpaw.common.exception.common.board.BoardException.BoardDeleteException;
import kr.co.pawpaw.common.exception.common.board.BoardException.BoardRegisterException;
import kr.co.pawpaw.common.exception.common.board.BoardException.BoardUpdateException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.board.Board;
import kr.co.pawpaw.domainrdb.board.dto.BoardDto;
import kr.co.pawpaw.domainrdb.board.dto.BoardDto.BoardRegisterDto;
import kr.co.pawpaw.domainrdb.board.dto.BoardDto.BoardUpdateDto;
import kr.co.pawpaw.domainrdb.board.repository.BoardRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardDto.RegisterResponseDto register(UserId userId, BoardRegisterDto registerDto) {
        if (StringUtils.hasText(registerDto.getTitle()) && StringUtils.hasText(registerDto.getContent())) {

            User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
            Board board = Board.createBoard(registerDto, user);

            boardRepository.save(board);

            return BoardDto.RegisterResponseDto.builder()
                    .title(board.getTitle())
                    .content(board.getContent())
                    .writer(user.getNickname())
                    .createDate(LocalDateTime.now())
                    .build();
        }else {
            throw new BoardRegisterException();
        }
    }

    @Transactional
    public BoardUpdateDto update(UserId userId, Long id, BoardUpdateDto updateDto) {

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (user.getUserId() != board.getUser().getUserId()) {
            throw new NotFoundUserException();
        }
        if (updateDto.getTitle() != null || updateDto.getContent() != null) {
            board.updateTitleAndContent(updateDto.getTitle(), updateDto.getContent());
        } else {
            throw new BoardUpdateException();
        }
        return BoardUpdateDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }


    @Transactional
    public boolean removeBoard(UserId userId, Long id) {

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (user.getUserId() != board.getUser().getUserId()) {
            throw new NotFoundUserException();
        }
        if (board == null){
            throw new BoardDeleteException();
        }
        board.remove();
        return true;
    }

}
