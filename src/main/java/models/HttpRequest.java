package models;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
abstract class HttpRequest {
     final private RequestType reqType;
    final private String route;
    final private String host;
    final private String userAgent;
    final private String accept;
    final private ContentType contentType;

    public HttpRequest(RequestType reqType, String route, String host, String userAgent, String accept, String contentType) {
        this.reqType = reqType;
        this.route = route;
        this.host = host;
        this.userAgent = userAgent;
        this.accept = accept;
        this.contentType = mapContentType(contentType);
    }

    private ContentType mapContentType(String contentType) {
        switch (contentType) {
            case "application/json":
                return ContentType.APPLICATION_JSON;
            default:
                System.out.println("content type not defined");
                break;
        }
        return ContentType.NONE;
    }
}
