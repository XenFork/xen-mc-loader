package union.xenfork.xenmc.gradle.util;

public enum OsSys {
    WIN("win"),
    MAC("mac")
    ;
    private final String name;
    OsSys(String name) {
        this.name = name;
    }

    public String getOsName() {
        return name;
    }

    public static boolean isWin() {
        return GradleDir.getOsName().contains(WIN.getOsName());
    }

    public static boolean isMac() {
        return GradleDir.getOsName().contains(MAC.getOsName());
    }

    public static boolean isOther() {
        return !isWin() && !isMac();
    }
}
