package com.tortora.financas.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tortora.financas.model.Employee;
import com.tortora.financas.model.pojo.EmployeePOJO;
import com.tortora.financas.model.pojo.EmployeesPOJO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConverterUtils {

    private final HttpUtils httpUtils;

    public ConverterUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    public List<Employee> convertXMLEmployees(String url) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EmployeesPOJO.class, EmployeePOJO.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            EmployeesPOJO emps = (EmployeesPOJO) jaxbUnmarshaller.unmarshal(new URL(url));
            List<Employee> employeesList = new ArrayList<>();

            emps.getEmployeesPOJO().forEach(emp -> employeesList.add(new Employee(emp.getFirstName(), emp.getLastName(), emp.getRole())));

            return employeesList;
        } catch (JAXBException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Employee> convertJsonEmployees(String url) {
        HttpURLConnection conn = httpUtils.get(url);
        StringBuilder output = httpUtils.reader(conn);

        Type listType = new TypeToken<ArrayList<Employee>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(new String(output.toString().getBytes()), listType);
    }

}
