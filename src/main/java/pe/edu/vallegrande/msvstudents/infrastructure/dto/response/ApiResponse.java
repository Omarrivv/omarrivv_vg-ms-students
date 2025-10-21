package pe.edu.vallegrande.msvstudents.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String message;
    private T data;
    private Map<String, Object> extraData;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(message, data, null);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(message, null, null);
    }

    public static <T> ApiResponse<T> success(T data, String message, Map<String, Object> extraData) {
        return new ApiResponse<>(message, data, extraData);
    }
}