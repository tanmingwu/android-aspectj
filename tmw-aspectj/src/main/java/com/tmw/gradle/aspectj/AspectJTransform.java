package com.tmw.gradle.aspectj;

import com.android.annotations.NonNull;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.AppExtension;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import org.apache.commons.io.FileUtils;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 利用Transform 来处理AspectJ的任务
 * 核心就是利用Transform API来获取所有的class文件和jar文件的路径
 * 将需要处理的class或jar配置到AspectJ的任务并执行
 * 实现上参考了美团 的roboaspectj
 *
 * @author tanmingwu848
 * @since 2020/9/30
 */
public class AspectJTransform extends Transform {

    private final AspectJExtension extension;
    private final AppExtension appExtension;

    public AspectJTransform(Project project) {
        extension = project.getExtensions().getByType(AspectJExtension.class);
        appExtension = project.getExtensions().findByType(AppExtension.class);
    }

    @NonNull
    @Override
    public String getName() {
        return "aspectJ";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws IOException {
        doTransform(transformInvocation.getInputs(), transformInvocation.getOutputProvider(),
                transformInvocation.isIncremental());
    }

    private void doTransform(Collection<TransformInput> inputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException {
        System.out.println("transform start");
        System.out.println("exclude = " + extension.getExcludes());
        if (!isIncremental) {
            outputProvider.deleteAll();
        }
        //开关关闭的情况下不处理，但是依然要讲class文件原封不动的拷贝
        if (!extension.isEnable()) {
            copyFileDefault(inputs, outputProvider);
            return;
        }

//        String classpath = initClasspath(inputs);
        List<File> inPathFiles = new ArrayList<>();
        List<File> classpathFiles = new ArrayList<>();
        for (TransformInput input : inputs) {
            //自己工程的class文件，包含java class目录，kotlin class目录，以及katp生成的class目录
            //这里默认都处理
            // todo include 逻辑待添加
            for (DirectoryInput folder : input.getDirectoryInputs()) {
                File file = folder.getFile();
                System.out.println("DirectoryInput = " + file.getAbsolutePath());
                inPathFiles.add(file);
//                String inPath = Joiner.on(File.pathSeparator).join(inPathFiles);
//                String replace = classpath.replace(":" + file.getAbsolutePath(), "");
//                File output = outputProvider.getContentLocation(file.getAbsolutePath(), folder.getContentTypes(),
//                        folder.getScopes(), Format.DIRECTORY);
//                executeAspect(inPath, replace, output);
            }

            //第三方jar包的文件路径，这里根据配置项来决定要不要处理。
            //不需要Aspectj处理的，需要默认拷贝，否则会报错。
            for (JarInput jar : input.getJarInputs()) {
                if (extension.getIncludes().isEmpty()) {
                    if (extension.isExcludeAllJar() || isExclude(jar)) {
                        copyDefaultJarInput(outputProvider, jar);
                        classpathFiles.add(jar.getFile());
                    } else {
                        inPathFiles.add(jar.getFile());
//                        AspectJarInput(outputProvider, jar, classpath);
                    }
                } else {
                    if (isInclude(jar)) {
                        inPathFiles.add(jar.getFile());
//                        AspectJarInput(outputProvider, jar, classpath);
                    } else {
                        copyDefaultJarInput(outputProvider, jar);
                        classpathFiles.add(jar.getFile());
                    }
                }
            }
        }

        //处理Aspectj注入
        final String inPath = Joiner.on(File.pathSeparator).join(inPathFiles);
        List<String> strings = new ArrayList<>();
        for (File file : classpathFiles) {
            strings.add(file.getAbsolutePath());
        }
        String classpath = Joiner.on(File.pathSeparator).join(strings);
        File output = outputProvider.getContentLocation("main", getOutputTypes(),
                Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT), Format.DIRECTORY);
        executeAspect(inPath, classpath, output);
    }

    /**
     * class 和 jar文件的默认拷贝处理
     *
     * @param inputs         Collection<TransformInput>
     * @param outputProvider TransformOutputProvider
     * @throws IOException e
     */
    private void copyFileDefault(Collection<TransformInput> inputs, TransformOutputProvider outputProvider) throws IOException {
        File output;
        for (TransformInput input : inputs) {
            for (DirectoryInput it : input.getDirectoryInputs()) {
                output = outputProvider.getContentLocation(it.getFile().getAbsolutePath(), it.getContentTypes(),
                        it.getScopes(), Format.DIRECTORY);
                FileUtils.copyDirectory(it.getFile(), output);
            }
            for (JarInput it : input.getJarInputs()) {
                output = outputProvider.getContentLocation(it.getFile().getAbsolutePath(), it.getContentTypes(),
                        it.getScopes(), Format.JAR);
                FileUtils.copyFile(it.getFile(), output);
            }
        }
    }

    /**
     * 判断是否是排除的jar，排除的不需要Aspect编织，提高性能
     * 取出Jar包中的class库，根据配置的包名来比对
     *
     * @return boolean
     */
    private boolean isExclude(JarInput jarInput) throws IOException {
        if (jarInput.getFile().getAbsolutePath().endsWith(".jar")) {
            JarFile jarFile = new JarFile(jarInput.getFile());
            Enumeration<JarEntry> enumeration = jarFile.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement();
                String entryName = jarEntry.getName();
                for (String exclude : extension.getExcludes()) {
                    if (entryName.contains(exclude.replace('.', '/'))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isInclude(JarInput jarInput) throws IOException {
        if (jarInput.getFile().getAbsolutePath().endsWith(".jar")) {
            JarFile jarFile = new JarFile(jarInput.getFile());
            Enumeration<JarEntry> enumeration = jarFile.entries();
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement();
                String entryName = jarEntry.getName();
                for (String exclude : extension.getIncludes()) {
                    if (entryName.contains(exclude.replace('.', '/'))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void AspectJarInput(TransformOutputProvider outputProvider, JarInput jarInput, String classpath) {
        File jarFile = jarInput.getFile();
        System.err.println("AspectJarInput() handle jar = " + jarFile.getName());
        List<File> inPathFiles = new ArrayList<>();
        inPathFiles.add(jarInput.getFile());
        String inPath = Joiner.on(File.pathSeparator).join(inPathFiles);
        String replace = classpath.replace(":" + jarFile.getAbsolutePath(), "");
        File output = outputProvider.getContentLocation(jarFile.getAbsolutePath(), jarInput.getContentTypes(),
                jarInput.getScopes(), Format.DIRECTORY);
        executeAspect(inPath, replace, output);
    }

    private void copyDefaultJarInput(TransformOutputProvider outputProvider, JarInput jarInput) throws IOException {
        File file = jarInput.getFile();
        System.out.println("Jar [" + file.getName() + "] has been excluded.");
        File output = outputProvider.getContentLocation(file.getAbsolutePath(), jarInput.getContentTypes(),
                jarInput.getScopes(), Format.JAR);
        FileUtils.copyFile(file, output);
    }

    /**
     * 执行Aspectj编织任务，这里分开处理，避免引起冲突
     *
     * @param inPath    需要处理的路径
     * @param classpath 依赖包路径
     * @param output    输出路径
     */
    private void executeAspect(String inPath, String classpath, File output) {
        System.out.println("executeAspect inPath = " + inPath);
        //处理Aspectj注入
        String bootClasspath = Joiner.on(File.pathSeparator).join(appExtension.getBootClasspath());
        String[] args = new String[]{
                "-showWeaveInfo",
                "-inpath", inPath,
                "-aspectpath", classpath,
                "-d", output.getAbsolutePath(),
                "-classpath", classpath,
                "-bootclasspath", bootClasspath};
        System.out.println("-d = " + output.getAbsolutePath());
        MessageHandler handler = new MessageHandler(true);
        new Main().run(args, handler);
    }

    public String initClasspath(Collection<TransformInput> inputs) {
        List<String> classpath = new ArrayList<>();
        for (TransformInput input : inputs) {
            for (DirectoryInput folder : input.getDirectoryInputs()) {
                classpath.add(folder.getFile().getAbsolutePath());
            }
            for (JarInput jar : input.getJarInputs()) {
                classpath.add(jar.getFile().getAbsolutePath());
            }
        }

        return Joiner.on(File.pathSeparator).join(classpath);
    }
}