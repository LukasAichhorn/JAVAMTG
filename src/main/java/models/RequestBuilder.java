package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collector;
import java.util.stream.Collectors;
@Data
@NoArgsConstructor
public class RequestBuilder {
    RequestType type;
    String route;
    String userAgent;
    String authToken;
    String contentType;
    String accept;
    int contentLength;
    String data;

    public RequestBuilder(Vector<String> input) throws Exception {
        this.type = extractHttpType(input);
        this.route = extractRoute(input);
        this.userAgent = extractUserAgent(input);
        this.authToken = extractAuthToken(input);
        this.accept = extractAccept(input);
        this.contentLength = extractContentLength(input);
        this.contentType = extractContentType(input);
        this.data = extractData(input);
    }
    RequestType extractHttpType(Vector<String> input) throws Exception {
       if(input.get(0).contains("GET")) return RequestType.GET;
       if(input.get(0).contains("POST")) return RequestType.POST;
       if(input.get(0).contains("PUT")) return RequestType.PUT;
       if(input.get(0).contains("DELETE")) return RequestType.DELETE;
       throw new Exception("no Http type in request");

    }
    String extractRoute(Vector<String> input){
         var temp = Arrays.stream(input.get(0).split(" ")).toList();
        if(temp.isEmpty()) return null;
         return temp.get(1);
    }
    String extractUserAgent(Vector<String> input){
        var temp = Arrays.stream(input.get(1).split(" ")).toList();
        if(temp.isEmpty()) return null;
        return temp.get(1);
    }
    String extractAccept(Vector<String> input){
        var temp = Arrays.stream(input.get(2).split(" ")).toList();
        if(temp.isEmpty()) return null;
        return temp.get(1);
    }
    String extractContentType(Vector<String> input){
        List<String> temp= input.stream().filter(s-> s.contains("Content-Type")).collect(Collectors.toList());
        if(temp.isEmpty()) return null;
        return Arrays.stream(temp.get(0).split(" ")).toList().get(1);
    }
    int extractContentLength(Vector<String> input){
        List<String> temp= input.stream().filter(s-> s.contains("Content-Length")).collect(Collectors.toList());
        if(temp.isEmpty()) return 0;
        return Integer.parseInt( Arrays.stream(temp.get(0).split(" ")).toList().get(1));
    }
    String extractData(Vector<String> input){
        if(input.stream().filter(s-> s.contains("Content-Length")).collect(Collectors.toList()).isEmpty()){
            return null;
        }
        return input.lastElement();
    }
    String extractAuthToken(Vector<String> input){
        List<String> temp= input.stream().filter(s-> s.contains("Authorization:")).collect(Collectors.toList());
        if(temp.isEmpty()) return null;
        var temp2 =  Arrays.stream(temp.get(0).split(" ")).toList();
        return temp2.get(1) + ' ' + temp2.get(2);
    }


}


