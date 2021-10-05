package com.tmw.gradle.aspectj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AspectJExtension {

    private boolean enable = true;
    private boolean excludeAllJar = false;

    private List<String> includes = new ArrayList<>();
    private List<String> excludes = new ArrayList<>();

    public AspectJExtension include(String... filters) {
        if (filters != null) {
            this.includes.addAll(Arrays.asList(filters));
        }
        return this;
    }

    public AspectJExtension exclude(String... filters) {
        if (filters != null) {
            this.excludes.addAll(Arrays.asList(filters));
        }
        return this;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isExcludeAllJar() {
        return excludeAllJar;
    }

    public void setExcludeAllJar(boolean excludeAllJar) {
        this.excludeAllJar = excludeAllJar;
    }
}