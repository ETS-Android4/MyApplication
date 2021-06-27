import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.invocation.Gradle

class ConfigUtils {

    static init(Gradle gradle) {
        generateDep(gradle)
        addCommonGradle(gradle)
    }

    /**
     * 根据 depConfig 生成 dep
     */
    private static void generateDep(Gradle gradle) {
        def configs = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            def (name, config) = [entry.key, entry.value]
            if (config.pluginPath) {
                config.dep = config.pluginPath
            } else {
                config.dep = gradle.rootProject.findProject(config.path)
            }
            configs.put(name, config)
        }
        GLog.l("generateDep = ${GLog.object2String(configs)}")
    }

    private static addCommonGradle(Gradle gradle) {
        gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
            @Override
            void beforeEvaluate(Project project) {
                // 在 project 的 build.gradle 前 do sth.
                if (project.subprojects.isEmpty()) {
                    if (project.path.contains(":plugin:")) {
                        return
                    }
                    if (project.path.startsWith(":modules:module_")) {
                        project.apply {
                            from "${project.rootDir.path}/modules/buildApp.gradle"
                        }
                        GLog.d(project.toString() + " applies buildApp.gradle")
                    }
                }
            }

            @Override
            void afterEvaluate(Project project, ProjectState state) {
                // 在 project 的 build.gradle 末 do sth.
            }
        })
    }

    static getApplyPlugins() {
        def plugins = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            if (entry.value.isApply && entry.key.startsWith("plugin_")) {
                plugins.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyPlugins = ${GLog.object2String(plugins)}")
        return plugins
    }


    static getApplyModules() {
        def libs = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            if (entry.value.isApply && !entry.key.startsWith("plugin_")) {
                libs.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyModules = ${GLog.object2String(libs)}")
        return libs
    }

    static boolean isApply(Project project) {
        boolean isApplication = false
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            if (entry.value.isApply && entry.value.path.endsWith(project.name)) {
                isApplication = true
            }
        }
        return isApplication
    }
}