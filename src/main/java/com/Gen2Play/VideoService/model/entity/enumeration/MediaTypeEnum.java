package com.Gen2Play.VideoService.model.entity.enumeration;

public enum MediaTypeEnum {
    OTHER("other"),
    MP4("mp4"),
    AVI("avi"),
    MKV("mkv"),
    MOV("mov"),
    FLV("flv"),
    WMV("wmv"),
    MPG("mpg"),
    MPEG("mpeg"),
    WEBM("webm"),
    OGV("ogv"),
    G3P("3gp"),
    TS("ts"),
    VOB("vob"),
    M4V("m4v"),
    QT("qt"),
    RM("rm"),
    RMVB("rmvb"),
    SWF("swf"),
    ASF("asf"),
    DIVX("divx"),
    XVID("xvid"),
    WTV("wtv");

    private final String format;

    MediaTypeEnum(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    // Phương thức để kiểm tra định dạng có hợp lệ không
    public static boolean isValidFormat(String format) {
        for (MediaTypeEnum vf : MediaTypeEnum.values()) {
            if (vf.format.equalsIgnoreCase(format)) {
                return true;
            }
        }
        return false;
    }
}
