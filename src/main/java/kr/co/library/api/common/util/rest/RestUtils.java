package kr.co.library.api.common.util.rest;

import org.springframework.http.HttpHeaders;

import java.net.URI;


public interface RestUtils {
    public void get();
    public <T, R> R post(URI url , HttpHeaders httpHeaders , T body, Class<R> responseType);
    public void patch();
    public void delete();
}
