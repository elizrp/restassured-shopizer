public interface JsonStrings {

    String CREDENTIALS = "{\"username\": \"admin@shopizer.com\",\"password\": \"password\"}";
    String CREATE_CATEGORY_BODY = "{\n" +
            "  \"children\": [\n" +
            "    \n" +
            "  ],\n" +
            "  \"code\": \"Category-code%s\",\n" +
            "  \"depth\": 0,\n" +
            "  \"descriptions\": [\n" +
            "    {\n" +
            "      \"description\": \"description1\",\n" +
            "      \"friendlyUrl\": \"/someurl1\",\n" +
            "      \"highlights\": \"highlights1\",\n" +
            "      \"id\": 0,\n" +
            "      \"keyWords\": \"keyWords1\",\n" +
            "      \"language\": \"EN\",\n" +
            "      \"metaDescription\": \"metaDescription1\",\n" +
            "      \"name\": \"Category-name1\",\n" +
            "      \"title\": \"Category-title1\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"featured\": true,\n" +
            "  \"id\": 0,\n" +
            "  \"lineage\": \"lineage\",\n" +
            "  \"parent\": {\n" +
            "    \"code\": \"parent-code\",\n" +
            "    \"id\": 0\n" +
            "  },\n" +
            "  \"sortOrder\": 0,\n" +
            "  \"visible\": %s\n" +
            "}";
}
