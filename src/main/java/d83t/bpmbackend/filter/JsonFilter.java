package d83t.bpmbackend.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JsonFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper((HttpServletResponse) response);

        filterChain.doFilter(request, responseWrapper);

        responseWrapper.setCharacterEncoding("UTF-8");
        byte[] responseArray = responseWrapper.getContentAsByteArray();
        String responseStr = new String(responseArray, responseWrapper.getCharacterEncoding());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseStr);
        ObjectNode dataNode = objectMapper.createObjectNode();
        if(jsonNode.get("errors") == null) {
            dataNode.set("response", jsonNode);
        }else{
            dataNode = (ObjectNode) jsonNode;
        }

        String modifiedJson = objectMapper.writeValueAsString(dataNode);
        //한글이 포함되어 있어서 사이즈를 재계산해줘야함.
        byte[] utf8Bytes = modifiedJson.getBytes(StandardCharsets.UTF_8);
        int length = utf8Bytes.length;

        response.setContentType(responseWrapper.getContentType());
        response.setContentLength(length);
        response.getOutputStream().write(modifiedJson.getBytes());
    }

}
