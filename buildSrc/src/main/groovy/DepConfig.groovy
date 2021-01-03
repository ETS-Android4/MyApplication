class DepConfig {

    boolean isApply     // 是否应用

    String path         // 依赖路径

    String pluginPath   // 插件路径

    def dep             // 根据条件生成项目最终的依赖项

    DepConfig() {
        isApply = true
    }

    DepConfig(boolean isApply, String path) {
        this.isApply = isApply
        this.path = path
    }

    String getPath() {
        if (pluginPath != null) return pluginPath
        return path
    }

    @Override
    String toString() {
        return "{ isApply = ${getFlag(isApply)}" +
                (dep == null ? ", path = " + path : (", dep = " + dep)) +
                " }"
    }

    static String getFlag(boolean b) {
        return b ? "Y" : "X"
    }
}