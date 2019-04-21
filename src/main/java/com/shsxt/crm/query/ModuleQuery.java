package com.shsxt.crm.query;

import com.shsxt.crm.base.BaseQuery;

public class ModuleQuery extends BaseQuery {
    private String moduleName;
    private Integer parentId;
    private Integer grade;
    private Integer optValue;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getOptValue() {
        return optValue;
    }

    public void setOptValue(Integer optValue) {
        this.optValue = optValue;
    }
}
