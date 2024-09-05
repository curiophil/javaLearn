package com.curiophil.javalearn.json.extract;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;

import java.util.*;

public class ExtractMain {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        String json = "{ \"items\": [ { \"name\": \"Item 1\", \"age\":  \"${age}\"}, { \"name\": \"Item 2\" }, { \"name\": \"Item 3\" } ] }";

        Object names = JsonPath.read(json, "$.items[*].name");
        if (names instanceof List) {
            System.out.println(names);
        }

        Object age = JsonPath.read(json, "$.items[*]");
        if (age instanceof List) {
            System.out.println(age);
        }

        JsonNode jsonNode = mapper.readTree(json);
        replaceJsonNode(jsonNode, new HashMap<String, Object>() {{
            put("${age}", Arrays.asList(1, 2, 3));
        }});
        String result = mapper.writeValueAsString(jsonNode);
        System.out.println(result);

    }

    public static void replaceJsonNode(JsonNode node, Map<String, Object> placeholders) {
        if (node.isObject()) {
            // 如果是对象节点，遍历所有字段
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode childNode = entry.getValue();

                // 递归处理子节点
                replaceJsonNode(childNode, placeholders);

                // 如果子节点是文本类型，检查是否包含占位符
                if (childNode.isTextual()) {
                    String textValue = childNode.asText();
                    String name = extractPlaceholderName(textValue);
                    if (name != null && placeholders.containsKey(name)) {
                        Object replacementValue = placeholders.get(textValue);
                        if (replacementValue instanceof JsonNode) {
                            objectNode.set(entry.getKey(), (JsonNode) replacementValue);
                        } else if (replacementValue instanceof String) {
                            objectNode.put(entry.getKey(), (String) replacementValue);
                        } else if (replacementValue instanceof Integer) {
                            objectNode.put(entry.getKey(), (Integer) replacementValue);
                        } else if (replacementValue instanceof Boolean) {
                            objectNode.put(entry.getKey(), (Boolean) replacementValue);
                        } else {
                            // 将任意对象转换为 JSON 并替换
                            objectNode.set(entry.getKey(), mapper.valueToTree(replacementValue));
                        }
                    }
                }
            }
        } else if (node.isArray()) {
            // 如果是数组节点，遍历数组中的每个元素
            for (JsonNode arrayItem : node) {
                replaceJsonNode(arrayItem, placeholders);
            }
        }
    }

    // 从文本中提取占位符的名称
    private static String extractPlaceholderName(String text) {
        if (text.startsWith("${") && text.endsWith("}")) {
            return text.substring(2, text.length() - 1);
        }
        return null;
    }

}
