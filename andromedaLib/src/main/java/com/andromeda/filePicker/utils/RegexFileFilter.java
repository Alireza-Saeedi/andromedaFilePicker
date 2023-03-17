package com.andromeda.filePicker.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.regex.Pattern;

public class RegexFileFilter implements FileFilter, Serializable {
    boolean m_allowHidden;
    boolean m_onlyDirectory;
    Pattern m_pattern;

    public RegexFileFilter() {
        this(null);
    }

    public RegexFileFilter(Pattern ptn) {
        this(false, false, ptn);
    }

    public RegexFileFilter(boolean dirOnly, boolean hidden, String ptn) {
        m_allowHidden = hidden;
        m_onlyDirectory = dirOnly;
        m_pattern = Pattern.compile(ptn, Pattern.CASE_INSENSITIVE);
    }

    public RegexFileFilter(boolean dirOnly, boolean hidden, String ptn, int flags) {
        m_allowHidden = hidden;
        m_onlyDirectory = dirOnly;
        m_pattern = Pattern.compile(ptn, flags);
    }

    public RegexFileFilter(boolean dirOnly, boolean hidden, Pattern ptn) {
        m_allowHidden = hidden;
        m_onlyDirectory = dirOnly;
        m_pattern = ptn;
    }

    @Override
    public boolean accept(File pathname) {
        if (!m_allowHidden) {
            if (pathname.isHidden()) {
                return false;
            }
        }

        if (m_onlyDirectory) {
            if (!pathname.isDirectory()) {
                return false;
            }
        }

        if (m_pattern == null) {
            return true;
        }

        if (pathname.isDirectory()) {
            return true;
        }

        String name = pathname.getName();
        if (m_pattern.matcher(name).matches()) {
            return true;
        }
        return false;
    }

}
