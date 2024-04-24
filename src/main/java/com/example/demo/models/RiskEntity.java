package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("riskentity")
public class RiskEntity {
    @Id
    private String id;

    private String processId;

    private String taskId;

    private Date createdDate;

    private Date updateDate;

    public final static class FieldNames {
        public final static String processId = "processId";
        public final static String taskId = "taskId";
        public final static String createdDate = "createdDate";
        public final static String updateDate = "updateDate";
    }

    public static void validatePartialUpdate(String fieldName, Object data) {
        switch (fieldName) {
            case FieldNames.processId, FieldNames.taskId -> {
                if (!(data instanceof String)) {
                    throw new RuntimeException(invalidDataTypeMessage(fieldName, String.class, data));
                }
            }
            case FieldNames.updateDate, FieldNames.createdDate -> {
                if (!(data instanceof Date)) {
                    throw new RuntimeException(invalidDataTypeMessage(fieldName, String.class, data));
                }
            }
            default -> throw new RuntimeException("Field " + fieldName + " does not exist or not allow to update");
        }
    }

    private static String invalidDataTypeMessage(String fieldName, Class<?> clazz, Object data) {
        return "Invalid data type for field " + fieldName + " expect " + clazz.getTypeName() +
                " but got " + data.getClass().getTypeName();
    }


}
