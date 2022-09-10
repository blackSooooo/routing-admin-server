package worksmobile.intern.restapiserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {
    BAD_REQUEST(400, "잘못된 형식이나 내용입니다."),
    INVALID_PARAMETER(400, "잘못된 파라미터 입력입니다."),
    MISSING_PARAMETER(400, "필수 파라미터를 지정하지 않았습니다."),
    NOT_FOUND(404, "라우팅 정보 리소스를 찾을 수 없습니다."),
    RESOURCE_CONFLICT(409, "이미 존재하는 클라이언트 API입니다."),
    SYSTEM_ERROR(500, "서버 시스템 내부의 오류입니다.");

    private int code;
    private String errorMessage;
}
