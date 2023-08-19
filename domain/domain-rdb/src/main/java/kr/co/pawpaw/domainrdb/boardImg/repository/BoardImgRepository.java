package kr.co.pawpaw.domainrdb.boardImg.repository;


import kr.co.pawpaw.domainrdb.boardImg.entity.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {
}
