package models;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestBuilderTest {

    Vector<String> testVector = new Vector<>();
    RequestBuilder reqBTest = new RequestBuilder();
    @BeforeEach
    void initBefore() throws Exception {
        testVector.add("POST /tradings HTTP/1.1");
        testVector.add("Host: localhost:10001");
        testVector.add("User-Agent: curl/7.55.1");
        testVector.add("Accept: */*");
        testVector.add("Content-Type: application/json");
        testVector.add("Authorization: Basic kienboec-mtcgToken");
        testVector.add("Content-Length: 141");
        testVector.add("testData");


    }
    @Test
    public void testExtractHttpType() throws Exception {
    assertEquals(RequestType.POST,reqBTest.extractHttpType(testVector));
    }
    @Test
    public void testExtractRoute() {
        assertEquals("/tradings",reqBTest.extractRoute(testVector));
    }
    @Test
    public void testExtractUserAgent() {
        assertEquals("localhost:10001",reqBTest.extractUserAgent(testVector));

    }
    @Test
    public void testExtractAccept() {
        assertEquals("curl/7.55.1",reqBTest.extractAccept(testVector));

    }
    @Test
    public void testExtractContentType() {
        assertEquals("application/json",reqBTest.extractContentType(testVector));

    }
    @Test
    public void testExtractContentLength() {
        assertEquals(141,reqBTest.extractContentLength(testVector));
    }
    @Test
    public void testExtractData() {
        assertEquals("testData",reqBTest.extractData(testVector));
    }
@Test
    public void testExtractAuthToken() {
        assertEquals("Basic kienboec-mtcgToken",reqBTest.extractAuthToken(testVector));
    }
}