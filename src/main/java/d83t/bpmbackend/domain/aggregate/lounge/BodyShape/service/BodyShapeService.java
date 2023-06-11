package d83t.bpmbackend.domain.aggregate.lounge.BodyShape.service;

import d83t.bpmbackend.domain.aggregate.lounge.BodyShape.dto.BodyShapeRequest;
import d83t.bpmbackend.domain.aggregate.lounge.BodyShape.dto.BodyShapeResponse;
import d83t.bpmbackend.domain.aggregate.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BodyShapeService {


    BodyShapeResponse createBoastArticle(User user, List<MultipartFile> files, BodyShapeRequest boastArticleRequest);

    List<BodyShapeResponse> getBodyShapes(User user, Integer limit, Integer offset);

    BodyShapeResponse getBodyShape(User user, Long bodyShapeId);

    BodyShapeResponse updateBodyShape(User user, List<MultipartFile> files, BodyShapeRequest bodyShapeRequest, Long bodyShapeId);

    void deleteBodyShape(User user, Long bodyShapeId);

}
