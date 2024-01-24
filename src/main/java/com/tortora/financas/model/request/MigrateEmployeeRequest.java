package com.tortora.financas.model.request;

import com.tortora.financas.model.Employee;

import java.util.Objects;

public class MigrateEmployeeRequest {

    private String url;

    private String dataType;

    public MigrateEmployeeRequest(String url, String dataType) {
        this.url = url;
        this.dataType = dataType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object m) {

        if (this == m)
            return true;
        if (!(m instanceof MigrateEmployeeRequest))
            return false;
        MigrateEmployeeRequest migrateEmployeeRequest = (MigrateEmployeeRequest) m;
        return Objects.equals(this.url, migrateEmployeeRequest.url)
                && Objects.equals(this.dataType, migrateEmployeeRequest.dataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.url, this.dataType);
    }

    @Override
    public String toString() {
        return "MigrateEmployeeRequest{"  + "url='" + this.url + '\'' + ", dataType='" + this.dataType + '\'' + '}';
    }

}
