package com.vibevault.model;

public record Song(String title, String artist, int durationInSeconds) {

    /**
     * 将Song对象序列化为CSV格式的字符串。
     * 例如: "Bohemian Rhapsody,Queen,355"
     * @return CSV格式的字符串
     */
    public String toCsvString() {
        return String.format("%s,%s,%d", title, artist, durationInSeconds);
    }

    /**
     * 从CSV格式的字符串反序列化，创建一个Song对象。
     * 这是一个静态工厂方法。
     * @param csvLine CSV格式的一行文本
     * @return 一个新的Song对象
     */
    public static Song fromCsvString(String csvLine) {
        String[] fields = csvLine.split(",");
        // 这里我们假设CSV格式总是正确的，后续会讨论异常处理
        String title = fields[0];
        String artist = fields[1];
        int duration = Integer.parseInt(fields[2]);
        return new Song(title, artist, duration);
    }
}