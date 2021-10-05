package com.tmw.gradle.aspectj;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * 利用Gradle Transform 来为Aspectj执行编织任务
 *
 * @author tanmingwu848
 * @since 2020/9/30
 */
public class AspectJPlugin implements Plugin<Project> {

    private static final String ASPECTJ = "org.aspectj:aspectjrt:1.9.5";

    @Override
    public void apply(@NotNull Project project) {
        System.out.println("AspectJPlugin");
        if (project.getPlugins().findPlugin(AppPlugin.class) == null) {
            System.out.println("not a android application project");
            return;
        }
        project.getExtensions().create("aspectj", AspectJExtension.class);
        AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
        if (appExtension != null) {
            appExtension.registerTransform(new AspectJTransform(project));
        }
        project.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, ASPECTJ);
    }
}