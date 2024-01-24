package com.tortora.financas.model.pojo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeesPOJO {

    @XmlElement(name = "employee")
    private List<EmployeePOJO> employeesPOJO = null;

    public List<EmployeePOJO> getEmployeesPOJO() {
        return employeesPOJO;
    }

    public void setEmployeesPOJO(List<EmployeePOJO> employeesPOJO) {
        this.employeesPOJO = employeesPOJO;
    }

}
