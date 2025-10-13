package com.vibevault.model;

public record Song(String title, String artist, int durationInSeconds) {

    /**
     * 将Song对象序列化为CSV格式的字符串。
     * 例如: "Bohemian Rhapsody,Queen,355"
     * 
     * 注意：当前实现不支持标题或艺术家中包含逗号的情况。
     * 如果需要支持，请考虑使用JSON格式或实现RFC 4180标准的CSV解析。
     * 
     * @return CSV格式的字符串
     */
    public String toCsvString() {
        return String.format("%s,%s,%d", title, artist, durationInSeconds);
    }

    /**
     * 从CSV格式的字符串反序列化，创建一个Song对象。
     * 这是一个静态工厂方法。
     * 
     * @param csvLine CSV格式的一行文本
     * @return 一个新的Song对象
     * @throws IllegalArgumentException 如果CSV格式不正确（字段数量不对或时长不是数字）
     */
    public static Song fromCsvString(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV行不能为空");
        }
        
        String[] fields = csvLine.split(",");
        
        if (fields.length != 3) {
            throw new IllegalArgumentException(
                "CSV格式错误：期望3个字段，实际得到 " + fields.length + " 个字段"
            );
        }
        
        String title = fields[0].trim();
        String artist = fields[1].trim();
        
        try {
            int duration = Integer.parseInt(fields[2].trim());
            return new Song(title, artist, duration);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "CSV格式错误：时长必须是数字，得到的是 '" + fields[2] + "'"
            );
        }
    }
}