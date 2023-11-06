package kr.co.pawpaw.mysql.place.service.query;

import kr.co.pawpaw.mysql.place.dto.PlaceReviewResponse;
import kr.co.pawpaw.mysql.place.repository.PlaceReviewCustomRepository;
import kr.co.pawpaw.mysql.place.repository.PlaceReviewRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceReviewQuery {
    private final PlaceReviewCustomRepository placeReviewCustomRepository;
    private final PlaceReviewRepository placeReviewRepository;

    public PlaceReviewResponse findByPlaceIdAndReviewerUserId(
        final Long placeId,
        final UserId userId
    ) {
        return placeReviewCustomRepository.findByPlaceIdAndReviewerUserId(placeId, userId);
    }

    public Slice<PlaceReviewResponse> findByPlaceIdAndIdBefore(
        final UserId userId,
        final Long placeId,
        final Long beforeReviewId,
        final int size
    ) {
        return placeReviewCustomRepository.findByPlaceIdAndIdBefore(userId, placeId, beforeReviewId, size);
    }
}
